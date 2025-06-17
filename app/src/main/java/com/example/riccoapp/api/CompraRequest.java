package com.example.riccoapp.api;

import java.util.List;

public class CompraRequest {
    private String descripcion;
    private double precioTotal;
    private int user;
    private List<DetalleCompra> detalles;

    // Constructor
    public CompraRequest(String descripcion, double precioTotal, int user, List<DetalleCompra> detalles) {
        this.descripcion = descripcion;
        this.precioTotal = precioTotal;
        this.user = user;
        this.detalles = detalles;
    }

    // Getters
    public int getUser() { return user; }
    public String getDescripcion() { return descripcion; }
    public double getPrecioTotal() { return precioTotal; }
    public List<DetalleCompra> getDetalles() { return detalles; }
}