package com.example.riccoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements ProductoAdapterActivity.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductoAdapterActivity productoAdapter;
    private ArrayList<ProductoActivity> listaProductos;
    private EditText editNombre, editDescripcion, editPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listaProductos = new ArrayList<>();
        listaProductos.add(new ProductoActivity("Producto 1", "Descripción 1", 10.0));
        listaProductos.add(new ProductoActivity("Producto 2", "Descripción 2", 15.0));

        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productoAdapter = new ProductoAdapterActivity(listaProductos, this);
        recyclerView.setAdapter(productoAdapter);

        editNombre = findViewById(R.id.nombre_producto);
        editDescripcion = findViewById(R.id.descripcion_producto);
        editPrecio = findViewById(R.id.precio_producto);

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v -> agregarProducto());
    }

    private void agregarProducto() {
        String nombre = editNombre.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();
        String precioStr = editPrecio.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        ProductoActivity nuevoProducto = new ProductoActivity(nombre, descripcion, precio);
        listaProductos.add(nuevoProducto);
        productoAdapter.notifyItemInserted(listaProductos.size() - 1);

        editNombre.setText("");
        editDescripcion.setText("");
        editPrecio.setText("");

        Toast.makeText(this, "Producto añadido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditarClick(int position) {
        ProductoActivity producto = listaProductos.get(position);
        producto.setEditando(true);
        productoAdapter.notifyItemChanged(position);
    }

    @Override
    public void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio) {
        ProductoActivity producto = listaProductos.get(position);
        producto.setNombre(nuevoNombre);
        producto.setDescripcion(nuevaDescripcion);
        producto.setPrecio(nuevoPrecio);
        producto.setEditando(false);

        productoAdapter.notifyItemChanged(position);
        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBorrarClick(int position) {
        listaProductos.remove(position);
        productoAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }
}
