package com.tienda.gomez.raul.tiendaservicioproductos.proveedores.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.gomez.raul.tiendaservicioproductos.constants.ApiError;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.service.ProveedoresService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/proveedores")
public class ProveedoresController {

    @Autowired
    ProveedoresService proveedoresService;

    @GetMapping()
    public List<Proveedor> findAll() {
        return this.proveedoresService.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> findByCodigoProveedor(@PathVariable String codigo){
        
        try{
            return new ResponseEntity<Proveedor>(proveedoresService.findByCodigo(codigo), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> editProveedor(@PathVariable String codigo, @RequestBody Proveedor proveedor){
        try{
            return new ResponseEntity<Proveedor>(proveedoresService.editProveedor(codigo, proveedor), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
        
    }

    @PostMapping()
    public ResponseEntity<?> createProveedor(@RequestBody Proveedor proveedor){
        try{
            return new ResponseEntity<Proveedor>(proveedoresService.createProveedor(proveedor), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> delete(@PathVariable String codigo){
        try{        
            proveedoresService.delete(codigo);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }
    
}
