package com.example.riccoapp.api;

public class Product {
    private String nombre_producto; // Cambia el nombre de la variable
    private String descripcion;
    private double precio;

    // Constructor
    public Product(String nombre_producto, String descripcion, double precio) {
        this.nombre_producto = nombre_producto; // Asignar el nombre del producto
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Métodos getters
    public String getNombre() {
        return nombre_producto; // Cambia a `nombre_producto`
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }
}
