package com.tienda.gomez.raul.tiendaservicioproductos.tests.ean.repository;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.Ean;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.EanPK;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EanRepositoryTest {

    private final Integer TOTAL_EANS = 21;
    private final String EXISTING_EAN = "8437008000031";
    private final String NON_EXISTING_EAN = "8437008000091";

    private final String EXISTING_PROVIDER = "8437008";
    private final String EXISTING_PRODUCTO = "00001";

    @Autowired
    private EanRepository eanRepository;

    @Test
    public void findAll(){
        List<Ean> providersList = this.eanRepository.findAll();

        Assertions.assertThat(providersList).isNotNull();
        Assertions.assertThat(providersList.size()).isEqualTo(TOTAL_EANS);
    }

    @Test 
    public void FindByIdMustReturnSavedEan(){

        
        try {
            EanPK eanPK;
            eanPK = new EanPK(EXISTING_EAN);
        
            Optional<Ean> ean = this.eanRepository.findById(eanPK);

            Assertions.assertThat(ean.isPresent()).isTrue();
            Assertions.assertThat(ean.get().getCodigoEan().getCodigoProveedor()).isEqualTo(eanPK.getCodigoProveedor());
            Assertions.assertThat(ean.get().getCodigoEan().getCodigoProducto()).isEqualTo(eanPK.getCodigoProducto());
            Assertions.assertThat(ean.get().getCodigoEan().getCodigoDestino()).isEqualTo(eanPK.getCodigoDestino());
        } catch (InvalidFormatException e) {
            fail("Excepcion que no deberia ocurrir: " + e.getMessage());
        }

    }

    @Test
    public void saveEanMustReturnSavedEan() {
        
        try {
            Ean ean = new Ean();
            EanPK eanPK;
            eanPK = new EanPK(NON_EXISTING_EAN);
            
            ean.setCodigoEan(eanPK);
            ean.setHidratos(9L);
            ean.setGrasas(10L);
            ean.setProteinas(11L);

            ean.setPrecio(100L);

            Ean savedEan = eanRepository.save(ean);

            Assertions.assertThat(savedEan).isNotNull();
            
            Assertions.assertThat(savedEan.getCodigoEan().getCodigoProveedor()).isEqualTo(ean.getCodigoEan().getCodigoProveedor());
            Assertions.assertThat(savedEan.getCodigoEan().getCodigoProducto()).isEqualTo(ean.getCodigoEan().getCodigoProducto());
            Assertions.assertThat(savedEan.getCodigoEan().getCodigoDestino()).isEqualTo(ean.getCodigoEan().getCodigoDestino());

            Assertions.assertThat(savedEan.getGrasas()).isEqualTo(ean.getGrasas());
            Assertions.assertThat(savedEan.getHidratos()).isEqualTo(ean.getHidratos());
            Assertions.assertThat(savedEan.getProteinas()).isEqualTo(ean.getProteinas());
            Assertions.assertThat(savedEan.getPrecio()).isEqualTo(ean.getPrecio());
        } catch (InvalidFormatException e) {
            fail("Excepcion que no deberia ocurrir: " + e.getMessage());
        }
    }

    @Test
    public void editEanMustEditEan(){
        try {
            EanPK eanPK = new EanPK(EXISTING_EAN);
        
            Optional<Ean> eanEncontrado = eanRepository.findById(eanPK);

            eanEncontrado.ifPresentOrElse((ean) -> {
                ean.setHidratos(9L);
                ean.setGrasas(10L);
                ean.setProteinas(11L);
                ean.setPrecio(100L);

                Ean editedProducto = eanRepository.save(ean);

                Assertions.assertThat(editedProducto).isNotNull();
            }, ()->{
                fail("Ean no encontrado");
            });

        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    @Test
    public void deleteEanMustDeleteEan() {
        try {
            EanPK eanPK = new EanPK(EXISTING_EAN);
            eanRepository.deleteById(eanPK);

            Optional<Ean> producto = eanRepository.findById(eanPK);

            Assertions.assertThat(producto).isEmpty();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Test
    public void existsByCodigoEanCodigoProductoWithExistingProductMustReturnTrue(){
        Boolean ok = this.eanRepository.existsByCodigoEanCodigoProducto(EXISTING_PRODUCTO);

        Assertions.assertThat(ok).isTrue();
    }

    @Test
    public void existsByCodigoEanCodigoProveedoroWithExistingProveedorMustReturnTrue(){
        Boolean ok = this.eanRepository.existsByCodigoEanCodigoProveedor(EXISTING_PROVIDER);

        Assertions.assertThat(ok).isTrue();
    }

}
