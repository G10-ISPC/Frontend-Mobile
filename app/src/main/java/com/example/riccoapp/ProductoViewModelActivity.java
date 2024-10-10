package com.example.riccoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

// Clase ViewModel para manejar los productos
public class ProductoViewModelActivity extends ViewModel {
    // LiveData que almacena la lista de productos
    private MutableLiveData<List<Producto>> listaProductos = new MutableLiveData<>(new ArrayList<>());

    // Método para obtener la lista de productos
    public LiveData<List<Producto>> obtenerProductos() {
        return listaProductos;
    }

    // Método para agregar un producto a la lista
    public void agregarProducto(Producto producto) {
        List<Producto> productosActuales = listaProductos.getValue();
        productosActuales.add(producto);
        listaProductos.setValue(productosActuales); // Notifica que la lista ha cambiado
    }

    // Método para actualizar un producto en una posición específica
    public void actualizarProducto(int position, Producto productoActualizado) {
        List<Producto> productosActuales = listaProductos.getValue();
        productosActuales.set(position, productoActualizado); // Actualiza el producto en la posición correcta
        listaProductos.setValue(productosActuales); // Notifica el cambio
    }
}

