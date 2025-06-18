package com.example.riccoapp.api;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;

public class CartManager {

    private static CartManager instance;
    private List<Product> carrito;

    private CartManager() {
        carrito = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void agregarProducto(Product producto) {
        boolean yaExiste = false;
        for (Product p : carrito) {
            if (p.getId() == producto.getId()) {
                // Si ya existe, aumentamos la cantidad
                p.setCantidad(p.getCantidad() + 1);
                yaExiste = true;
                break;
            }
        }

        if (!yaExiste) {
            producto.setCantidad(1); // Primera vez, cantidad 1
            carrito.add(producto);
        }
    }

    public List<Product> getCarrito() {
        return carrito;
    }

    public void vaciarCarrito() {
        carrito.clear();
    }

    public double calcularTotal() {
        double total = 0;
        for (Product p : carrito) {
            total += p.getPrecio() * p.getCantidad();
        }
        return total;
    }
    public int getCantidadTotalProductos() {
        int total = 0;
        for (Product p : carrito) {
            total += p.getCantidad();
        }
        return total;
    }
    public void setCarrito(List<Product> productos) {
        carrito.clear();
        if (productos != null) {
            carrito.addAll(productos);
        }
    }

}