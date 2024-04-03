package com.tienda.gomez.raul.tiendaservicioproductos.ean.service;

import java.util.List;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.Ean;

public interface EanService {

    List<Ean> findAll();

    Ean findByCodigoEan(String codigoEan) throws InvalidFormatException;

    Ean crearEan(Ean ean) throws InvalidFormatException;

    Ean editarEan(String codigoEan, Ean ean) throws InvalidFormatException;

    void delete(String codigoEan) throws InvalidFormatException;

}
