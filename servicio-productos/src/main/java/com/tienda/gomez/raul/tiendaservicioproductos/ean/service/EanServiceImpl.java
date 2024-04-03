package com.tienda.gomez.raul.tiendaservicioproductos.ean.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.constants.Utils;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.Ean;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.EanPK;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.repository.ProductosRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.repository.ProveedoresRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EanServiceImpl implements EanService{

    @Autowired
    EanRepository eanRepository;

    @Autowired
    ProductosRepository productosRepository;

    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Cacheable(cacheNames = "eansCache")
    @Transactional(readOnly = true)
    @Override
    public List<Ean> findAll() {
        return this.eanRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Ean findByCodigoEan(String codigoEan) throws InvalidFormatException {
        Utils.verifyCodigoEAN(codigoEan);
        //Usamos el constructor para que verifique la validez del codigo
        Ean nuevoEan = new Ean(codigoEan);
        return this.eanRepository.findById(nuevoEan.getCodigoEan())
                    .orElseThrow(() -> new EntityNotFoundException("El EAN " + codigoEan + " no ha sido encontrado"));     
    }

    @Override
    public Ean crearEan(Ean ean) throws InvalidFormatException {

        //Verificamos
        Utils.verifyCodigoEAN(ean.getCodigoEan().toString());
        if(eanRepository.existsById(ean.getCodigoEan())){
            throw new EntityExistsException("El EAN " + ean.getCodigoEan().toString() + " ya existe");
        }

        String codigoProveedor = ean.getCodigoEan().getCodigoProveedor();
        String codigoProducto = ean.getCodigoEan().getCodigoProducto();

        Producto productoEncontrado = productosRepository.findById(codigoProducto)
                                        .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con el codigo " + codigoProducto));

        Proveedor proveedorEncontrado = proveedoresRepository.findById(codigoProveedor)
                                        .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con el codigo " + codigoProveedor));

        //Guardamos
        ean.setProducto(productoEncontrado);
        ean.setProveedor(proveedorEncontrado);
        
        return this.eanRepository.save(ean);
    }

    @Override
    public Ean editarEan(String codigoEan, Ean ean) throws InvalidFormatException {
        //Validamos el formato del código
        Utils.verifyCodigoEAN(codigoEan);
        EanPK eanPK = new EanPK(codigoEan);
        Ean eanEncontrado = this.eanRepository.findById(eanPK).orElseThrow(() -> new EntityNotFoundException("El EAN " + codigoEan + " no ha sido encontrado"));

        //Editamos los valores
        eanEncontrado.setGrasas(ean.getGrasas());
        eanEncontrado.setHidratos(ean.getHidratos());
        eanEncontrado.setProteinas(ean.getProteinas());
        eanEncontrado.setPrecio(ean.getPrecio());

        return this.eanRepository.saveAndFlush(eanEncontrado);
    }

    @Override
    public void delete(String codigoEan) throws InvalidFormatException {
        //Validamos el formato del código
        Utils.verifyCodigoEAN(codigoEan);

        EanPK eanPK = new EanPK(codigoEan);
        Ean eanEncontrado = this.eanRepository.findById(eanPK).orElseThrow(() -> new EntityNotFoundException("El EAN " + codigoEan + " no ha sido encontrado"));

        this.eanRepository.delete(eanEncontrado);
    }

}
