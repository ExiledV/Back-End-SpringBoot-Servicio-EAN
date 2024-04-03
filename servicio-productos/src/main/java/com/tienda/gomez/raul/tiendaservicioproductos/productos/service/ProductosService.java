package com.tienda.gomez.raul.tiendaservicioproductos.productos.service;

import java.util.List;

import javax.naming.InvalidNameException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;

public interface ProductosService {

    List<Producto> findAll();

    Producto findByCodigo(String codigo) throws InvalidFormatException;

    Producto editProducto(String codigo, Producto producto) throws InvalidFormatException, InvalidNameException;

    Producto createProducto(Producto producto) throws InvalidFormatException, InvalidNameException;

    void delete(String codigo) throws InvalidFormatException;

    

}
