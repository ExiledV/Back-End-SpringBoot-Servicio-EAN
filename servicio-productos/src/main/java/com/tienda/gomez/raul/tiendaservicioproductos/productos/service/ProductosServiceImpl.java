package com.tienda.gomez.raul.tiendaservicioproductos.productos.service;

import java.util.List;
import java.util.Optional;

import javax.naming.InvalidNameException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.constants.Utils;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.repository.ProductosRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductosServiceImpl implements ProductosService {

    @Autowired
    ProductosRepository productoRepository;

    @Autowired 
    EanRepository eanRepository;

    @Cacheable(cacheNames = "productosCache")
    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Producto findByCodigo(String codigo) throws InvalidFormatException {
        //Comprobamos el formato
        Utils.verifyCodigoProducto(codigo);
        
        Optional<Producto> optionalProducto = this.productoRepository.findById(codigo);

        return optionalProducto.orElseThrow(() ->  new EntityNotFoundException("Producto no encontrado con el codigo " + codigo));
    }

    @Transactional()
    @Override
    public Producto editProducto(String codigo, Producto producto) throws InvalidFormatException, InvalidNameException {
        //Comprobamos el formato
        Utils.verifyCodigoProducto(codigo);
        Utils.verifyProductoDataExceptCodigo(producto);

        Producto productoEncontrado = productoRepository.findById(codigo)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con el codigo " + codigo));

        //Actualizamos los datos del producto
        productoEncontrado.setNombre(producto.getNombre());

        if(producto.getDescripcion()!=null){
            productoEncontrado.setDescripcion(producto.getDescripcion());
        }

        return this.productoRepository.saveAndFlush(productoEncontrado);
    }

    @Transactional()
    @Override
    public Producto createProducto(Producto producto) throws InvalidFormatException, InvalidNameException {
        //Comprobamos el formato
        Utils.verifyCodigoProducto(producto.getCodigo());
        Utils.verifyProductoDataExceptCodigo(producto);

        if(productoRepository.existsById(producto.getCodigo()))
            throw new EntityExistsException("Producto encontrado con el código: " + producto.getCodigo());
        
        return this.productoRepository.saveAndFlush(producto);
    }

    @Transactional()
    @Override
    public void delete(String codigo) throws InvalidFormatException {
        //Comprobamos el formato
        Utils.verifyCodigoProducto(codigo);
        
        Producto productoEncontrado = productoRepository.findById(codigo)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con el codigo " + codigo));

        if(eanRepository.existsByCodigoEanCodigoProducto(codigo)){
            throw new DataIntegrityViolationException("Debe borrar todos los EAN asociados con el producto "+ codigo + " antes de eliminarlo");
        }

        this.productoRepository.delete(productoEncontrado);

    }
    
}
