package com.tienda.gomez.raul.tiendaservicioproductos.ean.models;

import java.util.Objects;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.constants.Utils;
import com.tienda.gomez.raul.tiendaservicioproductos.destino.models.Destino;

import jakarta.persistence.Embeddable;

import static com.tienda.gomez.raul.tiendaservicioproductos.constants.Constants.*;

@Embeddable
public class EanPK {
    private String codigoProveedor;
    private String codigoProducto;
    private Integer codigoDestino;

    //Constructores
    public EanPK(){}
    
    public EanPK(String codigoProveedor, String codigoProducto, Integer destino) {

        //TODO COMPROBAR FORMATO
        this.codigoProveedor = codigoProveedor;
        this.codigoProducto = codigoProducto;
        this.codigoDestino = destino;
    }

    public EanPK(String codigoEan) throws InvalidFormatException{
        Utils.verifyCodigoEAN(codigoEan);
        //Comprobamos que el destino sea válido, al no tener BBDD de destino lo podemos controlar aquí
        Integer codigoDestino = Integer.parseInt(codigoEan.substring(EAN_CODIGO_DESTINO));

        Utils.verifyCodigoDestino(Destino.fromNumero(codigoDestino));
        this.codigoDestino = codigoDestino;
        
        //Parseamos los codigos de cada
        this.codigoProveedor = codigoEan.substring(EAN_INICIO_CODIGO_PROVEEDOR, EAN_FIN_CODIGO_PROVEEDOR);
        this.codigoProducto = codigoEan.substring(EAN_INICIO_CODIGO_PRODUCTO, EAN_FIN_CODIGO_PRODUCTO);
    }

    //Getters and setters
    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) throws InvalidFormatException {
        Utils.verifyCodigoProducto(codigoProducto);

        this.codigoProducto = codigoProducto;
    }

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(String codigoProveedor) throws InvalidFormatException {
        Utils.verifyCodigoProveedor(codigoProveedor);

        this.codigoProveedor = codigoProveedor;
    }

    public Integer getCodigoDestino() {
        return codigoDestino;
    }

    public void setCodigoDestino(Integer destino) {
        this.codigoDestino = destino;
    }

    @Override
    public String toString() {
        
        return this.getCodigoProveedor()+this.getCodigoProducto()+this.getCodigoDestino();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EanPK eanPK = (EanPK) o;
        return Objects.equals(codigoProducto, eanPK.codigoProducto) &&
                Objects.equals(codigoProveedor, eanPK.codigoProveedor) &&
                codigoDestino == eanPK.codigoDestino;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoProducto, codigoProveedor, codigoDestino);
    }
    
}
