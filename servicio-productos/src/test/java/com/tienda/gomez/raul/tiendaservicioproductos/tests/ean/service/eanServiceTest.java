package com.tienda.gomez.raul.tiendaservicioproductos.tests.ean.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.Ean;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.EanPK;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.service.EanServiceImpl;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.repository.ProductosRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.repository.ProveedoresRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class eanServiceTest {

    private final String ALPHABETIC_EAN = "0000a00a000a1";
    private final String LARGER_LENGHT_EAN = "84370080000311111";
    private final String SMALLER_LENGHT_EAN = "8437001";

    private final String EXISTING_EAN = "8437008000031";
    private final String NON_EXISTING_EAN = "8437008000091";

    

    @Mock
    EanRepository eanRepository;

    @Mock
    ProductosRepository productosRepository;

    @Mock
    ProveedoresRepository proveedoresRepository;

    @InjectMocks EanServiceImpl eanService;

    @Test
    public void findAllShouldReturnListEANs(){
        List<Ean> listEansMock = Mockito.mock(ArrayList.class);

        when(eanRepository.findAll()).thenReturn(listEansMock);

        List<Ean> listEans = eanService.findAll();

        Assertions.assertThat(listEans).isNotNull();
        Assertions.assertThat(listEans).isEqualTo(listEansMock);
    }

    /*
     * FIND BY CODIGO EAN
     */
    @Test
    public void findByCodigoEanShouldReturnEan(){
        Ean ean = Mockito.mock(Ean.class);

        when(eanRepository.findById(Mockito.any(EanPK.class))).thenReturn(Optional.of(ean));

        try {
            Ean eanEncontrado = eanService.findByCodigoEan(EXISTING_EAN);
            Assertions.assertThat(eanEncontrado).isEqualTo(ean);
        } catch (InvalidFormatException e) {
            fail("Validacion de código falló");
        }
    }

    @Test
    public void findByCodigoEanWithLargerLenghtEanShouldThrowInvalidFormatException(){
        try {
            eanService.findByCodigoEan(LARGER_LENGHT_EAN);
            fail("Incorrect EAN accepted");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void findByCodigoEanWithSmallerLenghtEanShouldThrowInvalidFormatException(){
        try {
            eanService.findByCodigoEan(SMALLER_LENGHT_EAN);
            fail("Incorrect EAN accepted");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void findByCodigoEanWithAlphabeticEanShouldThrowInvalidFormatException(){
        try {
            eanService.findByCodigoEan(ALPHABETIC_EAN);
            fail("Incorrect EAN accepted");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void findByCodigoEanWithNotExistingEanShouldThrowEntityNotFoundException(){

        try {
            when(this.eanRepository.findById(Mockito.any(EanPK.class))).thenReturn(Optional.empty());
            Ean eanEncontrado = eanService.findByCodigoEan(NON_EXISTING_EAN);
            fail("Excepción no lanzada");
        } catch (EntityNotFoundException e) {
            
        } catch (Exception e) {
            fail("Excepcion inesperada lanzada: " + e.getMessage());
        }
    }

    /*
     * CREAR EAN
     */
    @Test
    public void crearEanShouldCreateEan(){        
        try {
            EanPK eanPK = new EanPK(NON_EXISTING_EAN);

            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            Producto mockProducto = Mockito.mock(Producto.class);
            Proveedor mockProveedor = Mockito.mock(Proveedor.class);

            when(eanRepository.existsById(Mockito.any(EanPK.class))).thenReturn(false);
            when(productosRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockProducto));
            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockProveedor));
            when(this.eanRepository.save(Mockito.any(Ean.class))).thenReturn(ean);

            Ean eanCreado = eanService.crearEan(ean);
            Assertions.assertThat(eanCreado).isEqualTo(ean);

        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void crearEanWithExistingEanShouldThrowEntityExistsException(){
        try {
            EanPK eanPK = new EanPK(NON_EXISTING_EAN);
            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            Producto mockProducto = Mockito.mock(Producto.class);
            Proveedor mockProveedor = Mockito.mock(Proveedor.class);

            when(eanRepository.existsById(Mockito.any(EanPK.class))).thenReturn(true);

            Ean eanCreado = eanService.crearEan(ean);
            Assertions.assertThat(eanCreado).isEqualTo(ean);
            fail("Excepción no lanzada");
        } catch (EntityExistsException e) {
            
        } catch (Exception e){
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void crearEanWithNonExistingProductCodeShouldThrowEntityNotFoundException(){
        try {
            EanPK eanPK = new EanPK(NON_EXISTING_EAN);
            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            Producto mockProducto = Mockito.mock(Producto.class);
            Proveedor mockProveedor = Mockito.mock(Proveedor.class);

            when(eanRepository.existsById(Mockito.any(EanPK.class))).thenReturn(false);
            when(productosRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            Ean eanCreado = eanService.crearEan(ean);
            Assertions.assertThat(eanCreado).isEqualTo(ean);
            fail("Excepción no lanzada");
        } catch (EntityNotFoundException e) {
            
        } catch (Exception e){
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void crearEanWithNonExistingProviderCodeShouldThrowEntityNotFoundException(){
        try {
            EanPK eanPK = new EanPK(NON_EXISTING_EAN);
            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            Producto mockProducto = Mockito.mock(Producto.class);
            Proveedor mockProveedor = Mockito.mock(Proveedor.class);

            when(eanRepository.existsById(Mockito.any(EanPK.class))).thenReturn(false);
            when(productosRepository.findById(Mockito.anyString())).thenReturn(Optional.of(mockProducto));
            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            Ean eanCreado = eanService.crearEan(ean);
            Assertions.assertThat(eanCreado).isEqualTo(ean);
            fail("Excepción no lanzada");
        } catch (EntityNotFoundException e) {
            
        } catch (Exception e){
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void crearEanWithLargerLenghtEanShouldThrowInvalidFormatException(){
        

        try {
            EanPK eanPK = new EanPK(LARGER_LENGHT_EAN);
            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            eanService.crearEan(ean);
            fail("Incorrect EAN accepted");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void crearEanWithSmallerLenghtEanShouldThrowInvalidFormatException(){
        try {
            EanPK eanPK = new EanPK(SMALLER_LENGHT_EAN);
            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            eanService.crearEan(ean);
            fail("Incorrect EAN accepted");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void crearEanWithAlphabeticEanShouldThrowInvalidFormatException(){
        try {
            EanPK eanPK = new EanPK(ALPHABETIC_EAN);
            Ean ean = new Ean();
            ean.setCodigoEan(eanPK);

            eanService.crearEan(ean);
            fail("Incorrect EAN accepted");
        } catch (InvalidFormatException e) {
            
        }
    }

    /*
     * EDIT EAN
     */
    @Test
    public void editEanShouldEditEan(){
        try {
            EanPK eanPK = new EanPK(EXISTING_EAN);
            Ean ean = Mockito.mock(Ean.class);
            Ean eanExistente = Mockito.mock(Ean.class);
            when(eanRepository.findById(Mockito.any(EanPK.class))).thenReturn(Optional.of(eanExistente));
            when(eanRepository.saveAndFlush(Mockito.any(Ean.class))).thenReturn(ean);
            Ean eanEditado = eanService.editarEan(EXISTING_EAN, ean);

            Assertions.assertThat(ean).isEqualTo(eanEditado);
        } catch (InvalidFormatException e) {
            fail("Excepción inesperada: " + e.getMessage());
        }
        
    }

    @Test
    public void editEanWithNotExistingEanShouldThrowEntityNotFoundException(){
        try {
            Ean ean = Mockito.mock(Ean.class);
            when(eanRepository.findById(Mockito.any(EanPK.class))).thenReturn(Optional.empty());

            Ean eanEditado = eanService.editarEan(EXISTING_EAN, ean);

            fail("Excepcion no lanzada");
        } catch(EntityNotFoundException e){

        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void editEanWithLargerLenghtEanShouldThrowInvalidFormatException(){
        try {
            Ean ean = Mockito.mock(Ean.class);
            Ean eanEditado = eanService.editarEan(LARGER_LENGHT_EAN, ean);

            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void editEanWithSmallerLenghtEanShouldThrowInvalidFormatException(){
        try {
            Ean ean = Mockito.mock(Ean.class);
            Ean eanEditado = eanService.editarEan(SMALLER_LENGHT_EAN, ean);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void editEanWithAlphabeticEanShouldThrowInvalidFormatException(){
        try {
            Ean ean = Mockito.mock(Ean.class);
            Ean eanEditado = eanService.editarEan(ALPHABETIC_EAN, ean);

            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    /**
     * ELIMINAR EAN
     */
    @Test
    public void deleteEanShouldDeleteEan(){
        try {
            Ean ean = Mockito.mock(Ean.class);
            when(eanRepository.findById(Mockito.any(EanPK.class))).thenReturn(Optional.of(ean));
            eanService.delete(EXISTING_EAN);
        } catch (Exception e) {
            fail("Excepción inesperada: " + e.getMessage());
        }
    }

    @Test
    public void deleteEanWithLargerEanShouldThrowInvalidFormatException(){
        try {
            eanService.delete(LARGER_LENGHT_EAN);
            fail("Excepción no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void deleteEanWithSmallerEanShouldThrowInvalidFormatException(){
        try {
            eanService.delete(SMALLER_LENGHT_EAN);
            fail("Excepción no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void deleteEanWithAlphabeticEanShouldThrowInvalidFormatException(){
        try {
            eanService.delete(ALPHABETIC_EAN);
            fail("Excepción no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }
}
