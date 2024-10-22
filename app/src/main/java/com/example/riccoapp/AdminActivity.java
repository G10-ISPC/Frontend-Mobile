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

public class AdminActivity extends AppCompatActivity implements ProductAdapterAdmin.OnProductoClickListener {

    private ProductoViewModel productoViewModel;
    private ProductAdapterAdmin productoAdapter;
    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inicialización de los campos de texto para nombre, descripción y precio
        edtNombre = findViewById(R.id.nombre_producto);
        edtDescripcion = findViewById(R.id.descripcion_producto);
        edtPrecio = findViewById(R.id.precio_producto);
        btnAgregar = findViewById(R.id.btnAddProduct);

        // Configura el RecyclerView con un LinearLayoutManager
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProductos);
        productoAdapter = new ProductAdapterAdmin(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productoAdapter);

        // Configura el ViewModel para la gestión de productos
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);

        // Observa los productos y actualiza la lista del RecyclerView cuando cambien
        productoViewModel.getProductos().observe(this, products -> {
            productoAdapter.updateList(products);
        });

        // Evento de clic del botón "Agregar Producto"
        btnAgregar.setOnClickListener(view -> {
            String nombre = edtNombre.getText().toString().trim(); // Obtiene el nombre del producto
            String descripcion = edtDescripcion.getText().toString().trim(); // Obtiene la descripción del producto
            String precioStr = edtPrecio.getText().toString().trim(); // Obtiene el precio como cadena de texto

            // Validación: todos los campos deben estar llenos
            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(AdminActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return; // Detiene la ejecución si falta algún campo
            }

            double precio;
            // Validación: el precio debe ser un número válido
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                Toast.makeText(AdminActivity.this, "Precio inválido", Toast.LENGTH_SHORT).show();
                return; // Detiene la ejecución si el precio no es un número válido
            }

            // Si todas las validaciones pasan, crea un nuevo producto
            Product product = new Product(nombre, descripcion, precio);
            productoViewModel.addProducto(product); // Agrega el producto a través del ViewModel

            // Limpia los campos de texto después de agregar el producto
            edtNombre.setText("");
            edtDescripcion.setText("");
            edtPrecio.setText("");

            // Muestra un mensaje de éxito
            Toast.makeText(AdminActivity.this, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show();
        });
    }

    // Este método es parte de la implementación de la interfaz OnProductoClickListener
    @Override
    public void onEditarClick(int position) {
        // No es necesario implementar nada aquí; la lógica de edición está en el adaptador
    }

    // Este método maneja la acción de guardar un producto editado
    @Override
    public void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio) {
        Product updatedProduct = productoAdapter.getProductAt(position); // Obtiene el producto desde el adaptador
        if (updatedProduct != null) {
            // Actualiza el producto con los nuevos valores
            updatedProduct.setNombre_producto(nuevoNombre);
            updatedProduct.setDescripcion(nuevaDescripcion);
            updatedProduct.setPrecio(nuevoPrecio);
            // Informa al ViewModel para actualizar el producto en la base de datos o almacenamiento
            productoViewModel.updateProducto(updatedProduct.getId_producto(), updatedProduct); // Usa el ID correcto
        }
    }

    // Este método maneja la acción de borrar un producto
    @Override
    public void onBorrarClick(int position) {
        Product productToDelete = productoAdapter.getProductAt(position); // Obtiene el producto desde el adaptador
        if (productToDelete != null) {
            // Informa al ViewModel para eliminar el producto
            productoViewModel.deleteProducto(productToDelete.getId_producto()); // Usa el ID correcto
        }
    }
}
