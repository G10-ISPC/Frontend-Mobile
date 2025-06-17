package com.example.riccoapp.api;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id_producto")
    private int id_producto;

    @SerializedName("nombre_producto")
    private String nombre_producto;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("precio")
    private double precio;

    // Si la imagen viene por URL, cambiá el tipo aquí (String). Por ahora lo dejo int como tenías.
    private int imagenId;

    @SerializedName("visible")
    private boolean visible;

    @SerializedName("stock")
    private int stock;

    private int cantidad = 1;

    // Constructor completo
    public Product(int id_producto, String nombre_producto, String descripcion, double precio, int imagenId, boolean visible, int stock) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenId = imagenId;
        this.visible = visible;
        this.stock = stock;
    }

    // Constructor simplificado (si querés)
    public Product(String nombre_producto, String descripcion, double precio) {
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.visible = true;
        this.stock = 0;
    }

    // Getters y setters
    public int getId() {
        return id_producto;
    }

    public void setId(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}