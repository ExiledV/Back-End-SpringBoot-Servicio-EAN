package com.tienda.gomez.raul.tiendaservicioproductos.tests.proveedores.service;

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
import org.springframework.dao.DataIntegrityViolationException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.repository.ProveedoresRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.service.ProveedoresServiceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class proveedorServiceTest {

    private final String ALPHABETIC_PROVIDER = "8437a08";
    private final String LARGER_LENGHT_PROVIDER = "84370088";
    private final String SMALLER_LENGHT_PROVIDER = "843700";

    private final String EXISTING_PROVIDER = "8437008";
    private final String NON_EXISTING_PROVIDER = "8466008";

    @Mock
    ProveedoresRepository proveedoresRepository;

    @Mock
    EanRepository eanRepository;

    @InjectMocks
    ProveedoresServiceImpl proveedoresService;

    @Test
    public void findAllShouldReturnAllProveedores(){
        List<Proveedor> listProveedoresMock = Mockito.mock(ArrayList.class);

        when(proveedoresRepository.findAll()).thenReturn(listProveedoresMock);

        List<Proveedor> listProveedores = proveedoresService.findAll();

        Assertions.assertThat(listProveedores).isNotNull();
        Assertions.assertThat(listProveedores).isEqualTo(listProveedoresMock);
    }

    /*
     * FIND BY ID
     */
    @Test
    public void findByIdShouldFindProveedor(){
        Proveedor provider = Mockito.mock(Proveedor.class);

        when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.of(provider));

        try {
            Proveedor proveedorEncontrado = proveedoresService.findByCodigo(EXISTING_PROVIDER);

            Assertions.assertThat(proveedorEncontrado).isEqualTo(provider);
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void findByIdWithLargerCodeMustThrowInvalidFormatException(){
        try {
            proveedoresService.findByCodigo(LARGER_LENGHT_PROVIDER);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void findByIdWithSmallerCodeMustThrowInvalidFormatException(){
        try {
            proveedoresService.findByCodigo(SMALLER_LENGHT_PROVIDER);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void findByIdWithAlphabeticCodeMustThrowInvalidFormatException(){
        try {
            proveedoresService.findByCodigo(ALPHABETIC_PROVIDER);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    /*
     * CREATE PROVEEDOR
     */
    @Test
    public void createProveedorShouldCreateProveedor(){
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setCodigo(NON_EXISTING_PROVIDER);
            proveedor.setNombre("Paco");

            when(proveedoresRepository.existsById(NON_EXISTING_PROVIDER)).thenReturn(false);
            when(proveedoresRepository.save(proveedor)).thenReturn(proveedor);

            Proveedor createdProveedor = proveedoresService.createProveedor(proveedor);

            Assertions.assertThat(proveedor.getCodigo()).isEqualTo(createdProveedor.getCodigo());
            Assertions.assertThat(proveedor.getNombre()).isEqualTo(createdProveedor.getNombre());
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void createProveedorWithExistingIdMustThrowEntityExistsException(){
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setCodigo(EXISTING_PROVIDER);
            proveedor.setNombre("Paco");

            when(proveedoresRepository.existsById(EXISTING_PROVIDER)).thenReturn(true);

            Proveedor createdProveedor = proveedoresService.createProveedor(proveedor);
            fail("Excepcion no lanzada");
        } catch (EntityExistsException e) {
            
        } catch(Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void createProveedorWithLargerCodeMustThrowInvalidFormatException(){
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setCodigo(LARGER_LENGHT_PROVIDER);
            proveedor.setNombre("Paco");

            proveedoresService.createProveedor(proveedor);

            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void createProveedorWithSmallerCodeMustThrowInvalidFormatException(){
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setCodigo(SMALLER_LENGHT_PROVIDER);
            proveedor.setNombre("Paco");

            proveedoresService.createProveedor(proveedor);

            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    @Test
    public void createProveedorWithAlphabeticCodeMustThrowInvalidFormatException(){
        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setCodigo(ALPHABETIC_PROVIDER);
            proveedor.setNombre("Paco");

            proveedoresService.createProveedor(proveedor);

            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        }
    }

    /*
     * EDITAR PROVEEDOR
     */
    @Test
    public void editProveedorShouldEditProveedor(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);

            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.of(proveedor));
            when(proveedoresRepository.saveAndFlush(proveedor)).thenReturn(proveedor);

            Proveedor createdProveedor = proveedoresService.editProveedor(EXISTING_PROVIDER, proveedor);

            Assertions.assertThat(proveedor.getCodigo()).isEqualTo(createdProveedor.getCodigo());
            Assertions.assertThat(proveedor.getNombre()).isEqualTo(createdProveedor.getNombre());
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void editProveedorWithNonExistingCodeShouldThrowEntityNotFoundException(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);

            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            proveedoresService.editProveedor(NON_EXISTING_PROVIDER, proveedor);
            fail("Excepcion no lanzada");
        } catch (EntityNotFoundException e) {
            
        } catch (Exception e){
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void editProveedorWithLargerCodeShouldThrowInvalidFormatException(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);

            proveedoresService.editProveedor(LARGER_LENGHT_PROVIDER, proveedor);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        } catch (Exception e){
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void editProveedorWithSmallerCodeShouldThrowInvalidFormatException(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);

            proveedoresService.editProveedor(SMALLER_LENGHT_PROVIDER, proveedor);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        } catch (Exception e){
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void editProveedorWithAlphabeticCodeShouldThrowInvalidFormatException(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);

            proveedoresService.editProveedor(ALPHABETIC_PROVIDER, proveedor);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            
        } catch (Exception e){
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    /*
     * DELETE PROVEEDOR
     */
    @Test
    public void deleteProveedorMustDeleteProveedor(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);

            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.of(proveedor));
            when(eanRepository.existsByCodigoEanCodigoProveedor(EXISTING_PROVIDER)).thenReturn(false);
            proveedoresService.delete(EXISTING_PROVIDER);
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void deleteProveedorWithNoExistingIdMustThrowEntityNotFoundException(){
        try {
            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
            
            proveedoresService.delete(EXISTING_PROVIDER);

            fail("Excepcion no lanzada");
        } catch (EntityNotFoundException e) {
            // Pasa
        } catch (Exception e){
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void deleteProveedorWithEansAssociatedMustThrowDataIntegrityViolationException(){
        try {
            Proveedor proveedor = Mockito.mock(Proveedor.class);
            when(proveedoresRepository.findById(Mockito.anyString())).thenReturn(Optional.of(proveedor));
            when(eanRepository.existsByCodigoEanCodigoProveedor(EXISTING_PROVIDER)).thenReturn(true);
            proveedoresService.delete(EXISTING_PROVIDER);

            fail("Excepcion no lanzada");
        } catch (DataIntegrityViolationException e) {
            // Pasa
        } catch (Exception e) {
            fail("Excepcion inesperada " + e.getMessage());
        }
    }

    @Test
    public void deleteProveedorWithLargerCodeMustThrowInvalidFormatException(){
        try {
            proveedoresService.delete(LARGER_LENGHT_PROVIDER);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            //Pasa
        }
    }

    @Test
    public void deleteProveedorWithSmallerCodeMustThrowInvalidFormatException(){
        try {
            proveedoresService.delete(SMALLER_LENGHT_PROVIDER);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            //Pasa
        }
    }

    @Test
    public void deleteProveedorWithAlphabeticCodeMustThrowInvalidFormatException(){
        try {
            proveedoresService.delete(ALPHABETIC_PROVIDER);
            fail("Excepcion no lanzada");
        } catch (InvalidFormatException e) {
            //Pasa
        }
    }
}
