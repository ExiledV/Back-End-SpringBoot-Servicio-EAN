package com.tienda.gomez.raul.tiendaservicioproductos.tests.productos.repository;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.repository.ProductosRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductoRepositoryTests {

    private final int NUM_INICIAL_PRODUCTOS = 20;
    private final String EXISTING_PRODUCTO = "00001";
    private final String NOT_EXISTING_PRODUCTO = "10001";
    
    @Autowired 
    ProductosRepository productosRepository;

    @Test
    public void findAllMustReturnListOfProductos(){
        List<Producto> productosList = this.productosRepository.findAll();

        Assertions.assertThat(productosList).isNotNull();
        Assertions.assertThat(productosList.size()).isEqualTo(NUM_INICIAL_PRODUCTOS);
    }

    @Test 
    public void FindByIdMustReturnSavedProducto(){

        Optional<Producto> producto = this.productosRepository.findById(EXISTING_PRODUCTO);

        Assertions.assertThat(producto.isPresent()).isTrue();
        Assertions.assertThat(producto.get().getCodigo()).isEqualTo(EXISTING_PRODUCTO);

    }

    @Test
    public void saveProductoMustReturnSavedProducto() {
        Producto producto = new Producto();

        producto.setCodigo(NOT_EXISTING_PRODUCTO);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descr");

        Producto savedProducto = productosRepository.save(producto);

        Assertions.assertThat(savedProducto).isNotNull();
        Assertions.assertThat(savedProducto.getCodigo()).isEqualTo(producto.getCodigo());
        Assertions.assertThat(savedProducto.getNombre()).isEqualTo(producto.getNombre());
        Assertions.assertThat(savedProducto.getDescripcion()).isEqualTo(producto.getDescripcion());
        
        
    }

    @Test
    public void editProductoMustEditProducto(){
        Optional<Producto> productoEncontrado = productosRepository.findById(EXISTING_PRODUCTO);

        productoEncontrado.ifPresentOrElse((prod) -> {
            prod.setNombre("Prueba edicion");
            prod.setDescripcion("Edit");

            Producto editedProducto = productosRepository.save(prod);

            Assertions.assertThat(editedProducto).isNotNull();
            Assertions.assertThat(editedProducto.getNombre()).isEqualTo(prod.getNombre());
            Assertions.assertThat(editedProducto.getDescripcion()).isEqualTo(prod.getDescripcion());
        }, ()->{
            fail("Producto no encontrado");
        });
        
    }

    @Test
    public void deleteProductoMustDeleteProducto() {
        productosRepository.deleteById(EXISTING_PRODUCTO);

        Optional<Producto> producto = productosRepository.findById(EXISTING_PRODUCTO);

        Assertions.assertThat(producto).isEmpty();
    }


}
