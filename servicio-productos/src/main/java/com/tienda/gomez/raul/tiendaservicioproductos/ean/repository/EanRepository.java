package com.tienda.gomez.raul.tiendaservicioproductos.ean.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.Ean;
import com.tienda.gomez.raul.tiendaservicioproductos.ean.models.EanPK;

public interface EanRepository extends JpaRepository<Ean, EanPK>{

    public boolean existsByCodigoEanCodigoProducto(String codigoProducto);
    public boolean existsByCodigoEanCodigoProveedor(String codigoProducto);
}
