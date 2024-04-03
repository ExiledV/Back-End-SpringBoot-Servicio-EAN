package com.tienda.gomez.raul.tiendaservicioproductos.destino.models;

public enum Destino {
    TIENDA_ESP(1, 5, "Tiendas tienda España"),
    TIENDA_POR(6, "Tiendas tienda Porugal"),
    ALMACENES(8, "Almacenes"),
    OFICINAS(9, "Oficinas tienda"),
    COLMENAS(0, "Colmenas");

    private final int numeroDesde;
    private final int numeroHasta;
    private final String nombreDestino;

    Destino(int numeroDesde, int numeroHasta, String nombreDestino) {
        this.numeroDesde = numeroDesde;
        this.numeroHasta = numeroHasta;
        this.nombreDestino = nombreDestino;
    }

    Destino(int numero, String nombreDestino) {
        this.numeroDesde = numero;
        this.numeroHasta = numero;
        this.nombreDestino = nombreDestino;
    }

    public int getNumeroDesde() {
        return numeroDesde;
    }

    public int getNumeroHasta() {
        return numeroHasta;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public static Destino fromNumero(int numero) {
        for (Destino destino : Destino.values()) {
            if (destino.getNumeroDesde() <= numero && destino.getNumeroHasta() >= numero) {
                return destino;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nombreDestino;
    }
}
