package com.example.riccoapp.api;

import com.google.gson.annotations.SerializedName;

public class Compra {
    @SerializedName("id")
    private int id;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("precio")
    private double precio;

    @SerializedName("estado")
    private String estado;

    // Constructor
    public Compra(int id, String fecha, String descripcion, double precio, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.precio = precio;
        this.estado = estado;
    }

    // Getters
    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getEstado() { return estado; }
}
