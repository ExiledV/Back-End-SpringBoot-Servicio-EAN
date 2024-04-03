package com.tienda.gomez.raul.tiendaservicioproductos.proveedores.service;

import java.util.List;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;

public interface ProveedoresService {

    List<Proveedor> findAll();

    Proveedor findByCodigo(String codigo) throws InvalidFormatException;

    Proveedor editProveedor(String codigo, Proveedor proveedor) throws InvalidFormatException;

    Proveedor createProveedor(Proveedor proveedor) throws InvalidFormatException;

    void delete(String codigo) throws InvalidFormatException;

}
