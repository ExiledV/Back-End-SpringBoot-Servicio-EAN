package com.tienda.gomez.raul.tiendaservicioproductos.tests.productos.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.InvalidNameException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.repository.ProductosRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.service.ProductosServiceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    private final String EXISTING_PRODUCTO = "00001";
    private final String NOT_EXISTING_PRODUCTO = "10001";
    private final String LARGER_LENGHT_PRODUCTO = "000001";
    private final String SMALLER_LENGHT_PRODUCTO = "0001";
    private final String ALPHABETIC_CODE = "0001a";

    @Mock
    private ProductosRepository productoRepository;

    @Mock
    private EanRepository eanRepository;

    @InjectMocks
    private ProductosServiceImpl productosService;

    @Test
    public void findAllShouldReturnListProductos(){
        List<Producto> listProductosMock = Mockito.mock(ArrayList.class);

        when(productoRepository.findAll()).thenReturn(listProductosMock);

        List<Producto> listProductos = productosService.findAll();

        Assertions.assertThat(listProductos).isNotNull();
        Assertions.assertThat(listProductos).isEqualTo(listProductosMock);
    }

    /*
     * FIND BY CODIGO
     */
    @Test
    public void findByCodigoWithExistingCodigoShouldReturnProducto(){
        Producto producto = new Producto();

        producto.setCodigo(EXISTING_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.of(producto));

        Producto productoEncontrado;
        try {
            productoEncontrado = productosService.findByCodigo(EXISTING_PRODUCTO);
            Assertions.assertThat(productoEncontrado).isEqualTo(producto);
        } catch (InvalidFormatException e) {
            fail("Validacion de código falló");
        }

    }

    @Test
    public void findByCodigoWithNonExistingCodigoShouldReturnProducto(){

        when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        try{
            productosService.findByCodigo(NOT_EXISTING_PRODUCTO);

            fail("Producto encontrado");
        } catch (EntityNotFoundException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    @Test
    public void findByCodigoWithCodigoWithLenghtLessThanFiveShouldThrowInvalidFormatException(){

        try{
            productosService.findByCodigo(SMALLER_LENGHT_PRODUCTO);
            fail("Pasó la validación");
        } catch (InvalidFormatException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    @Test
    public void findByCodigoWithCodigoWithLenghtGreaterThanFiveShouldThrowInvalidFormatException(){

        try{
            productosService.findByCodigo(LARGER_LENGHT_PRODUCTO);
            fail("Pasó la validación");
        } catch (InvalidFormatException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    @Test
    public void findByCodigoWithAlphabeticCodeShouldThrowInvalidFormatException(){

        try{
            productosService.findByCodigo(ALPHABETIC_CODE);
            fail("Pasó la validación");
        } catch (InvalidFormatException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    /*
     * CREATE PRODUCTO
     */
    @Test
    public void createProductoMustCreateProducto() {
        Producto producto = new Producto();

        producto.setCodigo(NOT_EXISTING_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        when(productoRepository.saveAndFlush(Mockito.any(Producto.class))).thenReturn(producto);
        when(productoRepository.existsById(Mockito.anyString())).thenReturn(false);
        try {
            Producto savedProducto = productosService.createProducto(producto);

            Assertions.assertThat(savedProducto).isNotNull();
            Assertions.assertThat(savedProducto.getCodigo()).isEqualTo(producto.getCodigo());
            Assertions.assertThat(savedProducto.getNombre()).isEqualTo(producto.getNombre());
            Assertions.assertThat(savedProducto.getDescripcion()).isEqualTo(producto.getDescripcion()); 
        } catch (Exception e) {
            fail("Las validadiones fallaron por la razon: " + e.getMessage());
        }
    }

    @Test
    public void createProductoWithExistingCodeMustThrowExistingEntityException(){
        Producto producto = new Producto();

        producto.setCodigo(EXISTING_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        when(productoRepository.existsById(Mockito.anyString())).thenReturn(true);
        try {
            productosService.createProducto(producto);
            fail("Las validaciones fallaron por alguna razon");
        } catch (InvalidFormatException | InvalidNameException e) {
            fail("Saltó la excepcion incorrecta");
        } catch (EntityExistsException e) {
            
        }
    }

    @Test
    public void createProductoWithInvalidProductoDataMustThrowInvalidFormatException(){
        Producto producto = new Producto();

        producto.setCodigo(EXISTING_PRODUCTO);
        producto.setNombre(null);
        producto.setDescripcion("Descr");

        try {
            productosService.createProducto(producto);
            fail("Las validaciones fallaron por alguna razon");
        } catch (InvalidFormatException | InvalidNameException e) {
            
        } catch (EntityExistsException e) {
            fail("Saltó la excepcion incorrecta");
        }
    }

    @Test
    public void createProductoWithNullCodeMustThrowNullPointerException(){
        Producto producto = new Producto();

        producto.setCodigo(null);
        producto.setNombre("Nombre de prueba");
        producto.setDescripcion("Descr");

        try {
            productosService.createProducto(producto);
            fail("Las validaciones fallaron por alguna razon");
        } catch (NullPointerException e) {
            
        } catch (Exception e) {
            fail("Saltó la excepcion incorrecta " + e.getMessage());
        }
    }

    @Test
    public void createProductoWithCodigoWithLenghtLessThanFiveShouldThrowInvalidFormatException(){
        Producto producto = new Producto();

        producto.setCodigo(SMALLER_LENGHT_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        try{
            productosService.createProducto(producto);
            fail("Pasó la validación");
        } catch (InvalidFormatException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    @Test
    public void createProductoWithCodigoWithLenghtGreaterThanFiveShouldThrowInvalidFormatException(){

        Producto producto = new Producto();

        producto.setCodigo(LARGER_LENGHT_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        try{
            productosService.createProducto(producto);
            fail("Pasó la validación");
        } catch (InvalidFormatException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    @Test
    public void createProductoWithAlphabeticCodeShouldThrowInvalidFormatException(){

        Producto producto = new Producto();

        producto.setCodigo(ALPHABETIC_CODE);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        try{
            productosService.createProducto(producto);
            fail("Pasó la validación");
        } catch (InvalidFormatException e){
            
        } catch (Exception e){
            fail("Excepcion inesperada encontrada: " + e.getMessage());
        }
    }

    /*
     * EDIT PRODUCTO
     */
    @Test
    public void editProductoMustEditProducto() {
        Producto prodIni = new Producto();
        prodIni.setCodigo(EXISTING_PRODUCTO);
        prodIni.setNombre("Ini state");
        prodIni.setDescripcion("descr ini state");

        Producto producto = new Producto();

        producto.setCodigo(EXISTING_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");


        when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.of(prodIni));
        when(productoRepository.saveAndFlush(Mockito.any(Producto.class))).thenReturn(producto);
        
        Producto editProducto;
        try {
            editProducto = this.productosService.editProducto(EXISTING_PRODUCTO, producto);

            Assertions.assertThat(editProducto).isNotNull();
            Assertions.assertThat(editProducto.getCodigo()).isEqualTo(producto.getCodigo());
            Assertions.assertThat(editProducto.getNombre()).isEqualTo(producto.getNombre());
            Assertions.assertThat(editProducto.getDescripcion()).isEqualTo(producto.getDescripcion());
        } catch (InvalidFormatException | InvalidNameException e) {
            fail("Las validadiones fallaron por la razon: " + e.getMessage());
        }
    }

    @Test
    public void editProductoWithNonExistingProductoMustThrowEntityNotFoundException() {
        Producto producto = new Producto();

        producto.setCodigo(NOT_EXISTING_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        
        try {
            this.productosService.editProducto(EXISTING_PRODUCTO, producto);
            fail("Las validadiones fallaron porque ha editado correctamente");
        } catch (InvalidFormatException | InvalidNameException e) {
            fail("Las validadiones fallaron por la razon: " + e.getMessage());
        } catch (EntityNotFoundException e) {

        }
    }

    @Test
    public void editProductoWithInvalidProductoDataMustThrowInvalidFormatException() {
        Producto producto = new Producto();

        producto.setCodigo(EXISTING_PRODUCTO);
        producto.setNombre(null);
        producto.setDescripcion("Descr");
        
        try {
            this.productosService.editProducto(EXISTING_PRODUCTO, producto);
            fail("Las validadiones fallaron porque ha editado correctamente");
        } catch (InvalidFormatException | InvalidNameException e) {
            
        } catch (EntityNotFoundException e) {
            fail("Las validadiones fallaron por la razon: " + e.getMessage());
        }

        Producto producto2 = new Producto();

        producto2.setCodigo(EXISTING_PRODUCTO);
        producto2.setNombre("");
        producto2.setDescripcion("Descr");
        
        try {
            this.productosService.editProducto(EXISTING_PRODUCTO, producto2);
            fail("Las validadiones fallaron porque ha editado correctamente");
        } catch (InvalidFormatException | InvalidNameException e) {

        } catch (EntityNotFoundException e) {
            fail("Las validadiones fallaron por la razon: " + e.getMessage());
        }
    }

    @Test
    public void editProductoWithCodigoWithLenghtLessThanFiveShouldThrowInvalidFormatException(){
        Producto producto = new Producto();

        producto.setCodigo(SMALLER_LENGHT_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");
        
        try {
            this.productosService.editProducto(SMALLER_LENGHT_PRODUCTO, producto);

            fail("Validacion incorrecta");
        } catch (InvalidFormatException | InvalidNameException e) {
            
        }
    }

    @Test
    public void editProductoWithCodigoWithLenghtGreaterThanFiveShouldThrowInvalidFormatException(){

        Producto producto = new Producto();

        producto.setCodigo(LARGER_LENGHT_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");
        
        try {
            this.productosService.editProducto(LARGER_LENGHT_PRODUCTO, producto);

            fail("Validacion incorrecta");
        } catch (InvalidFormatException | InvalidNameException e) {
            
        }
    }

    @Test
    public void editProductoWithAlphabeticCodeShouldThrowInvalidFormatException(){

        Producto producto = new Producto();

        producto.setCodigo(ALPHABETIC_CODE);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");
        
        try {
            this.productosService.editProducto(ALPHABETIC_CODE, producto);

            fail("Validacion incorrecta");
        } catch (InvalidFormatException | InvalidNameException e) {
            
        }
    }

    /*
     * ELIMINAR PRODUCTO
     */

     @Test
    public void deleteProductoMustDeleteProducto(){
        try {
            Producto producto = Mockito.mock(Producto.class);

            when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.of(producto));
            when(eanRepository.existsByCodigoEanCodigoProducto(EXISTING_PRODUCTO)).thenReturn(false);
            productosService.delete(EXISTING_PRODUCTO);
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void deleteProductoWithNoExistingIdMustThrowEntityNotFoundException(){
        try {
            when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
            
            productosService.delete(EXISTING_PRODUCTO);

            fail("Excepcion no lanzada");
        } catch (EntityNotFoundException e) {
            // Pasa
        } catch (Exception e){
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void deleteProductoWithEansAssociatedMustThrowDataIntegrityViolationException(){
        try {
            Producto Producto = Mockito.mock(Producto.class);
            when(productoRepository.findById(Mockito.anyString())).thenReturn(Optional.of(Producto));
            when(eanRepository.existsByCodigoEanCodigoProducto(EXISTING_PRODUCTO)).thenReturn(true);
            productosService.delete(EXISTING_PRODUCTO);

            fail("Excepcion no lanzada");
        } catch (DataIntegrityViolationException e) {
            // Pasa
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void deleteProductoWithLargerCodeMustThrowInvalidFormatException(){
        try {
            productosService.delete(LARGER_LENGHT_PRODUCTO);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            //Pasa
        }
    }

    @Test
    public void deleteProductoWithSmallerCodeMustThrowInvalidFormatException(){
        try {
            productosService.delete(SMALLER_LENGHT_PRODUCTO);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            //Pasa
        }
    }

    @Test
    public void deleteProductoWithAlphabeticCodeMustThrowInvalidFormatException(){
        try {
            productosService.delete(ALPHABETIC_CODE);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            //Pasa
        }
    }
}