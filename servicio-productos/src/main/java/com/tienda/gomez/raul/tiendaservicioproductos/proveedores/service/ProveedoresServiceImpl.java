package com.tienda.gomez.raul.tiendaservicioproductos.proveedores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.constants.Utils;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.repository.EanRepository;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.repository.ProveedoresRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProveedoresServiceImpl implements ProveedoresService{

    @Autowired
    ProveedoresRepository proveedoresRepository;

    @Autowired
    EanRepository eanRepository;

    @Cacheable(cacheNames = "proveedoresCache")
    @Transactional(readOnly = true)
    @Override
    public List<Proveedor> findAll() {
        
        return this.proveedoresRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Proveedor findByCodigo(String codigo) throws InvalidFormatException {
        //Proveedor se valida con un codigo válido
        Utils.verifyCodigoProveedor(codigo);

        return this.proveedoresRepository.findById(codigo).orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con el codigo " + codigo));
    }

    @Override
    public Proveedor editProveedor(String codigo, Proveedor proveedor) throws InvalidFormatException {
        //Proveedor se valida con un codigo válido
        Utils.verifyCodigoProveedor(codigo);
        
        Proveedor proveedorEncontrado = this.proveedoresRepository.findById(codigo)
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con el codigo " + codigo));
        
        //Modificamos el proveedor
        proveedorEncontrado.setNombre(proveedor.getNombre());

        return this.proveedoresRepository.saveAndFlush(proveedorEncontrado);
    }

    @Override
    public Proveedor createProveedor(Proveedor proveedor) throws InvalidFormatException {
        //Proveedor se valida con un codigo válido
        Utils.verifyCodigoProveedor(proveedor.getCodigo());

        if(proveedoresRepository.existsById(proveedor.getCodigo()))
            throw new EntityExistsException("Producto encontrado con el código: " + proveedor.getCodigo());
            
        return this.proveedoresRepository.save(proveedor);
    }

    @Override
    public void delete(String codigo) throws InvalidFormatException {
        //Proveedor se valida con un codigo válido
        Utils.verifyCodigoProveedor(codigo);
        Proveedor proveedorEncontrado = this.proveedoresRepository
                                            .findById(codigo).orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con el codigo " + codigo));
        if(eanRepository.existsByCodigoEanCodigoProveedor(codigo)){
            throw new DataIntegrityViolationException("Debe borrar todos los EAN asociados con el proveedor " + codigo + " antes de eliminarlo");
        }
        this.proveedoresRepository.delete(proveedorEncontrado);
    }
}
