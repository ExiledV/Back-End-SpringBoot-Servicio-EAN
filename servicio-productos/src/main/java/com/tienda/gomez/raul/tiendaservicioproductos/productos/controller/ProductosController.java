package com.tienda.gomez.raul.tiendaservicioproductos.productos.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tienda.gomez.raul.tiendaservicioproductos.constants.ApiError;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.service.ProductosService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/productos")
public class ProductosController {

    @Autowired
    private ProductosService productoService;

    @GetMapping("")
    public List<Producto> findAll() {
        return productoService.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> findByCodigoProducto(@PathVariable String codigo){
        try{
            return new ResponseEntity<Producto>(productoService.findByCodigo(codigo), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> editProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        try{            
            return new ResponseEntity<Producto>(productoService.editProducto(codigo, producto), HttpStatus.OK);
        } catch (Exception ex){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createProducto(@RequestBody Producto producto){
        try{
            return new ResponseEntity<Producto>(productoService.createProducto(producto), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }

    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> delete(@PathVariable String codigo) {
        try{           
            productoService.delete(codigo);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }
    
}
