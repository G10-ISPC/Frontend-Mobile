package com.example.riccoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class ProductoViewModelActivity extends ViewModel {
    private MutableLiveData<List<ProductoActivity>> listaProductos = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<ProductoActivity>> obtenerProductos() {
        return listaProductos;
    }

    public void agregarProducto(ProductoActivity producto) {
        List<ProductoActivity> productosActuales = listaProductos.getValue();
        productosActuales.add(producto);
        listaProductos.setValue(productosActuales);
    }

    public void actualizarProducto(int position, ProductoActivity productoActualizado) {
        List<ProductoActivity> productosActuales = listaProductos.getValue();
        productosActuales.set(position, productoActualizado);
        listaProductos.setValue(productosActuales);
    }
}

