package com.example.riccoapp.api;

public class DetalleCompra {
    private int cantidad;
    private double precio_calculado;
    private String id_producto;
    private String nombre_producto;

    public DetalleCompra(int cantidad, double precioCalculado, String idProducto, String nombreProducto) {
        this.cantidad = cantidad;
        this.precio_calculado = precioCalculado;
        this.id_producto = idProducto;
        this.nombre_producto = nombreProducto;
    }

    public int getCantidad() { return cantidad; }
    public double getPrecioCalculado() { return precio_calculado; }
    public String getIdProducto() { return id_producto; }
    public String getNombreProducto() { return nombre_producto; }
}
