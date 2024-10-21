package com.example.riccoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riccoapp.adapter.ProductAdapterAdmin;
import com.example.riccoapp.api.Product;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements ProductAdapterAdmin.OnProductoClickListener {  // Implementa la interfaz

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

        // Configura el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProductos);
        productoAdapter = new ProductAdapterAdmin(new ArrayList<>(), this); // Pasa 'this' para la interfaz
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productoAdapter);

        // Configura el ViewModel
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);

        productoViewModel.getProductos().observe(this, products -> {
            productoAdapter.updateList(products);
        });

        // Evento del botón agregar
        btnAgregar.setOnClickListener(view -> {
            String nombre = edtNombre.getText().toString();
            String descripcion = edtDescripcion.getText().toString();
            double precio;
            try {
                precio = Double.parseDouble(edtPrecio.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(AdminActivity.this, "Precio inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            Product product = new Product(nombre, descripcion, precio);
            productoViewModel.addProducto(product);
            edtNombre.setText("");
            edtDescripcion.setText("");
            edtPrecio.setText("");
        });
    }

    @Override
    public void onEditarClick(int position) {
        // La lógica de editar ya está implementada en el adaptador
    }

    @Override
    public void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio) {
        Product updatedProduct = productoAdapter.getProductAt(position); // Obtén el producto desde el adaptador
        if (updatedProduct != null) {
            updatedProduct.setNombre_producto(nuevoNombre);
            updatedProduct.setDescripcion(nuevaDescripcion);
            updatedProduct.setPrecio(nuevoPrecio);
            productoViewModel.updateProducto(updatedProduct.getId_producto(), updatedProduct); // Usa el ID correcto
        }
    }

    @Override
    public void onBorrarClick(int position) {
        Product productToDelete = productoAdapter.getProductAt(position); // Obtén el producto desde el adaptador
        if (productToDelete != null) {
            productoViewModel.deleteProducto(productToDelete.getId_producto()); // Usa el ID correcto
        }
    }
}
