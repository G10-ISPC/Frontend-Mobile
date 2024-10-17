package com.example.riccoapp.api;

public class Product {
    private int id_producto;
    private String nombre_producto; // Cambia el nombre de la variable
    private String descripcion;
    private double precio;

    // Constructor
    public Product(String nombre_producto, String descripcion, double precio) {
        this.nombre_producto = nombre_producto; // Asignar el nombre del producto
        this.descripcion = descripcion;
        this.precio = precio;
    }



    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }


}
