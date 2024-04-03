package com.tienda.gomez.raul.tiendaservicioproductos.proveedores.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @Column(length = 7)
    private String codigo;

    @Column(length = 50, nullable = false)
    private String nombre;

    //Getters and setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
