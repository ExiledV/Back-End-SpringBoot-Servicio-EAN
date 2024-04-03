package com.tienda.gomez.raul.tiendaservicioproductos.tests.proveedores.repository;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.repository.ProveedoresRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class proveedorRepositoryTest {

    private final Integer TOTAL_PROVEEDORES = 8;
    private final String EXISTING_PROVIDER = "8437008";
    private final String NON_EXISTING_PROVIDER = "8466008";

    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Test
    public void findAll(){
        List<Proveedor> providersList = this.proveedoresRepository.findAll();

        Assertions.assertThat(providersList).isNotNull();
        Assertions.assertThat(providersList.size()).isEqualTo(TOTAL_PROVEEDORES);
    }

    @Test 
    public void FindByIdMustReturnSavedProveedor(){

        Optional<Proveedor> proveedor = this.proveedoresRepository.findById(EXISTING_PROVIDER);

        Assertions.assertThat(proveedor.isPresent()).isTrue();
        Assertions.assertThat(proveedor.get().getCodigo()).isEqualTo(EXISTING_PROVIDER);

    }

    @Test
    public void saveProveedorMustReturnSavedProveedor() {
        Proveedor proveedor = new Proveedor();
        proveedor.setCodigo(NON_EXISTING_PROVIDER);
        
        proveedor.setNombre("Proveedor de prueba");

        Proveedor savedProveedor = proveedoresRepository.save(proveedor);

        Assertions.assertThat(savedProveedor).isNotNull();
        Assertions.assertThat(savedProveedor.getCodigo()).isEqualTo(proveedor.getCodigo());
        Assertions.assertThat(savedProveedor.getNombre()).isEqualTo(proveedor.getNombre());
        
    }

    @Test
    public void editProveedorMustEditProveedor(){
        Optional<Proveedor> proveedorEncontrado = proveedoresRepository.findById(EXISTING_PROVIDER);

        proveedorEncontrado.ifPresentOrElse((proveedor) -> {
            proveedor.setNombre("Prueba edicion");

            Proveedor editedProducto = proveedoresRepository.save(proveedor);

            Assertions.assertThat(editedProducto).isNotNull();
            Assertions.assertThat(editedProducto.getNombre()).isEqualTo(proveedor.getNombre());
        }, ()->{
            fail("Proveedor no encontrado");
        });
        
    }

    @Test
    public void deleteProveedorMustDeleteProveedor() {
        proveedoresRepository.deleteById(EXISTING_PROVIDER);

        Optional<Proveedor> producto = proveedoresRepository.findById(EXISTING_PROVIDER);

        Assertions.assertThat(producto).isEmpty();
    }

}
