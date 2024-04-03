package com.tienda.gomez.raul.tiendaservicioproductos.ean.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tienda.gomez.raul.tiendaservicioproductos.constants.ApiError;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.Ean;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.service.EanService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/ean")
public class EanController {

    @Autowired
    EanService eanService;

    @GetMapping()
    public List<Ean> findAll() {
        return eanService.findAll();
    }

    @GetMapping("/{codigoEan}")
    public ResponseEntity<?> findByCodigoEan(@PathVariable String codigoEan){
        try {
            return new ResponseEntity<Ean>(eanService.findByCodigoEan(codigoEan) ,null, HttpStatus.OK);

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }        
    }

    @PostMapping()
    public ResponseEntity<?> crearEan(@RequestBody Ean ean) {
        try{
            return new ResponseEntity<Ean>(eanService.crearEan(ean), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        } 

    }

    @PutMapping("/{codigoEan}")
    public ResponseEntity<?> editarEan(@PathVariable String codigoEan, @RequestBody Ean ean){
        try{
            return new ResponseEntity<Ean>(eanService.editarEan(codigoEan, ean), HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        }
    }

    @DeleteMapping("/{codigoEan}")
    public ResponseEntity<?> delete(@PathVariable String codigoEan){
        //Validamos el formato del código
        try{
            this.eanService.delete(codigoEan);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
        } 

    }
    
}
