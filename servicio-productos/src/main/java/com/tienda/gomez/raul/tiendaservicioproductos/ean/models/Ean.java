package com.tienda.gomez.raul.tiendaservicioproductos.ean.models;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tienda.gomez.raul.tiendaservicioproductos.constants.Utils;
import com.tienda.gomez.raul.tiendaservicioproductos.destino.models.Destino;
import com.tienda.gomez.raul.tiendaservicioproductos.productos.models.Producto;
import com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models.Proveedor;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ean")
public class Ean {
    @EmbeddedId
    private EanPK codigoEan;

    @ManyToOne
    @JoinColumn(name = "codigoProducto", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "codigoProveedor", referencedColumnName = "codigo", insertable = false, updatable = false)
    private Proveedor proveedor;

    @Transient
    private String nombreDestino;

    @Column(length = 4, nullable = false)
    private Long hidratos;

    @Column(length = 4, nullable = false)
    private Long proteinas;

    @Column(length = 4, nullable = false)
    private Long grasas;

    @Column(nullable = false)
    private Long precio;    

    public Ean(){}

    /**
     * Constructor que gestiona la creacion del EAN a partir de su codigo completo
     * @param codigoEan
     * @throws InvalidFormatException 
     */
    public Ean(String codigoEan) throws InvalidFormatException{
        this.codigoEan = new EanPK(codigoEan);
        this.setNombreDestino();
    }

    @PostLoad
    public void preLoad() throws InvalidFormatException {
        if (codigoEan != null) {
            setNombreDestino();
        }
    }

    //Getters and Setters
    public EanPK getCodigoEan() {
        return codigoEan;
    }

    public void setCodigoEan(EanPK codigoEan) throws InvalidFormatException {
        Utils.verifyCodigoProducto(codigoEan.getCodigoProducto());
        Utils.verifyCodigoProveedor(codigoEan.getCodigoProveedor());

        this.codigoEan = codigoEan;
        this.setNombreDestino();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Long getHidratos() {
        return hidratos;
    }

    public void setHidratos(Long hidratos) {
        this.hidratos = hidratos;
    }

    public Long getProteinas() {
        return proteinas;
    }

    public void setProteinas(Long proteinas) {
        this.proteinas = proteinas;
    }

    public Long getGrasas() {
        return grasas;
    }

    public void setGrasas(Long grasas) {
        this.grasas = grasas;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public String getNombreDestino() throws InvalidFormatException {
        if(nombreDestino == null){
            Destino destino = Destino.fromNumero(codigoEan.getCodigoDestino());
            Utils.verifyCodigoDestino(destino);

            return this.nombreDestino = destino.getNombreDestino();
        }
        return this.nombreDestino;
    }

    private void setNombreDestino() throws InvalidFormatException{
        Destino destino = Destino.fromNumero(codigoEan.getCodigoDestino());
        Utils.verifyCodigoDestino(destino);

        this.nombreDestino = destino.getNombreDestino();
    }  
    

}
