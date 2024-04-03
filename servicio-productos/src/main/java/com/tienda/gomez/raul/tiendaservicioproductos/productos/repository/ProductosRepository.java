package com.tienda.gomez.raul.tiendaservicioproductos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;

public interface ProductosRepository extends JpaRepository<Producto, String>{

}
