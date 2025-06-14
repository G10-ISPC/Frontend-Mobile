package com.example.riccoapp.api;

public class Product {
    private int id_producto;
    private String nombre_producto; // Cambia el nombre de la variable
    private String descripcion;
    private double precio;
    private int imagenId;
    private boolean visible; // Nuevo campo para estado de stock

    private int cantidad = 1; // <-- AGREGADO AQUÍ

    // Getters y setters de 'cantidad'
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    // Constructor
    public Product(String nombre_producto, String descripcion, double precio) {
        this.nombre_producto = nombre_producto; // Asignar el nombre del producto
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenId = imagenId;
        this.visible = true; // Por defecto, el producto está en stock
    }

    public int getId() {
        return id_producto;
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

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
