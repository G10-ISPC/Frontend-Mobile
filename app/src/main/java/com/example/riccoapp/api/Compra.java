package com.example.riccoapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Compra {
    @SerializedName("id_compra")  // Cambiado a id_compra
    private int id;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("precio_total")  // Nuevo campo
    private double precioTotal;

    @SerializedName("estado")
    private String estado;

    @SerializedName("cancelable_hasta")
    private Date cancelableHasta;

    // Constructor
    public Compra(int id, String fecha, String descripcion, double precioTotal, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.precioTotal = precioTotal;
        this.estado = estado;
    }

    // Getters
    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precioTotal; }  // Usa precioTotal
    public String getEstado() { return estado; }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}