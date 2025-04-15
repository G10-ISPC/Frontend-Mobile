package com.example.riccoapp;

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

public class AdminActivity extends BaseActivity implements ProductAdapterAdmin.OnProductoClickListener {

    private ProductoViewModel productoViewModel;
    private ProductAdapterAdmin productoAdapter;
    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupToolbar(); // Barra de navegación
        userNameTextView = findViewById(R.id.userNameTextView); // Asignación de TextView específico de esta Activity
        loadUserName(); // Carga y muestra el nombre del usuario

        edtNombre = findViewById(R.id.nombre_producto);
        edtDescripcion = findViewById(R.id.descripcion_producto);
        edtPrecio = findViewById(R.id.precio_producto);
        btnAgregar = findViewById(R.id.btnAddProduct);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewProductos);
        productoAdapter = new ProductAdapterAdmin(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productoAdapter);

        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        productoViewModel.getProductos().observe(this, products -> {
            productoAdapter.updateList(products);
        });

        btnAgregar.setOnClickListener(view -> {
            String nombre = edtNombre.getText().toString().trim();
            String descripcion = edtDescripcion.getText().toString().trim();
            String precioStr = edtPrecio.getText().toString().trim();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(AdminActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AdminActivity.this, "Precio inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(nombre, descripcion, precio);
            productoViewModel.addProducto(product);

            edtNombre.setText("");
            edtDescripcion.setText("");
            edtPrecio.setText("");
            Toast.makeText(AdminActivity.this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onEditarClick(int position) {
        // Implementación para editar el producto
    }

    @Override
    public void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio) {
        Product updatedProduct = productoAdapter.getProductAt(position);
        if (updatedProduct != null) {
            updatedProduct.setNombre_producto(nuevoNombre);
            updatedProduct.setDescripcion(nuevaDescripcion);
            updatedProduct.setPrecio(nuevoPrecio);
            productoViewModel.updateProducto(updatedProduct.getId_producto(), updatedProduct);
        }
    }

    @Override
    public void onBorrarClick(int position) {
        Product productToDelete = productoAdapter.getProductAt(position);
        if (productToDelete != null) {
            productoViewModel.deleteProducto(productToDelete.getId_producto());
        }
    }

    @Override
    public void onStockChangeClick(int position, boolean isInStock) {
        Product product = productoAdapter.getProductAt(position);
        if (product != null) {
            product.setVisible(isInStock);
            productoViewModel.updateStockStatus(product.getId_producto(), product);

            // Recargar lista después de la actualización
            productoViewModel.getProductos(); // Actualizar productos desde ViewModel
        }
    }

}
