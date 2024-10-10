package com.example.riccoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements EditarProductoDialog.OnProductoEditadoListener, ProductoAdapter.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductoAdapter productoAdapter;
    private ArrayList<Producto> listaProductos;
    private EditText editNombre, editDescripcion, editPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inicializa la lista de productos
        listaProductos = new ArrayList<>();
        // Añade algunos productos a la lista (ejemplo)
        listaProductos.add(new Producto("Producto 1", "Descripción 1", 10.0));
        listaProductos.add(new Producto("Producto 2", "Descripción 2", 15.0));

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProductos);  // Asegúrate de que el ID coincida con el XML
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productoAdapter = new ProductoAdapter(listaProductos, this);
        recyclerView.setAdapter(productoAdapter);

        // Referencias a los campos de texto para agregar productos
        editNombre = findViewById(R.id.nombre_producto);
        editDescripcion = findViewById(R.id.descripcion_producto);
        editPrecio = findViewById(R.id.precio_producto);

        // Configura el botón de agregar producto
        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v -> agregarProducto());
    }

    // Método para agregar un nuevo producto
    private void agregarProducto() {
        String nombre = editNombre.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();
        String precioStr = editPrecio.getText().toString().trim();

        // Validar que no se dejen campos vacíos
        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        Producto nuevoProducto = new Producto(nombre, descripcion, precio);
        listaProductos.add(nuevoProducto);  // Agregar el nuevo producto a la lista
        productoAdapter.notifyItemInserted(listaProductos.size() - 1);  // Notificar que un nuevo producto fue insertado

        // Limpiar los campos de entrada
        editNombre.setText("");
        editDescripcion.setText("");
        editPrecio.setText("");

        Toast.makeText(this, "Producto añadido", Toast.LENGTH_SHORT).show();
    }

    // Método para manejar la edición del producto
    @Override
    public void onProductoEditado(Producto productoEditado, int posicion) {
        // Actualizar el producto en la posición exacta de la lista
        listaProductos.set(posicion, productoEditado);
        productoAdapter.notifyItemChanged(posicion);  // Notifica al adaptador que hubo un cambio
        Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
    }

    // Método para abrir el diálogo de edición
    public void abrirDialogoEdicion(Producto producto, int posicion) {
        // Cuando se presiona el botón para editar un producto
        EditarProductoDialog dialogo = new EditarProductoDialog(producto, posicion);
        dialogo.show(getSupportFragmentManager(), "EditarProductoDialog");
    }

    // Método para manejar el clic en el botón Editar
    @Override
    public void onEditarClick(int position) {
        abrirDialogoEdicion(listaProductos.get(position), position);
    }

    // Método para manejar el clic en el botón Borrar
    @Override
    public void onBorrarClick(int position) {
        listaProductos.remove(position);  // Eliminar el producto de la lista
        productoAdapter.notifyItemRemoved(position);  // Notificar al adaptador que se eliminó un elemento
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }
}

