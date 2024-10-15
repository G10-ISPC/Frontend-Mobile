package com.example.riccoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riccoapp.adapter.ProductAdapterAdmin;
import com.example.riccoapp.api.Product;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private ProductoViewModel productoViewModel;
    private ProductAdapterAdmin productoAdapter;
    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        edtNombre = findViewById(R.id.nombre_producto);
        edtDescripcion = findViewById(R.id.descripcion_producto);
        edtPrecio = findViewById(R.id.precio_producto);
        btnAgregar = findViewById(R.id.btnAddProduct);

        //Se configura el Recycler
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProductos);
        productoAdapter = new ProductAdapterAdmin(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productoAdapter);

        // Se configura el Viewmodel
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        productoViewModel.getProductos().observe(this, products -> {
            productoAdapter.updateList(products);
        });

        // Evento del boton

        btnAgregar.setOnClickListener(view -> {

            String nombre = edtNombre.getText().toString();
            String descripcion = edtDescripcion.getText().toString();
            double precio = Double.parseDouble(edtPrecio.getText().toString());

            Product product = new Product(nombre, descripcion, precio);

            product.setNombre_producto(edtNombre.getText().toString());
            product.setDescripcion(edtDescripcion.getText().toString());
            product.setPrecio(Double.parseDouble(edtPrecio.getText().toString()));

            productoViewModel.addProducto(product);

            edtNombre.setText("");
            edtDescripcion.setText("");
            edtPrecio.setText("");
        });
    }
}
