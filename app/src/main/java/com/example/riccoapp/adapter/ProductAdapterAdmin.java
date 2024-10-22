package com.example.riccoapp.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riccoapp.R;
import com.example.riccoapp.api.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ProductoViewHolder> {
    private List<Product> products;
    private List<Boolean> isEditing; // Lista para manejar el estado de edición
    private OnProductoClickListener listener;

    // Interfaz para manejar eventos de clic en el adaptador
    public interface OnProductoClickListener {
        void onEditarClick(int position);
        void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio);
        void onBorrarClick(int position);
    }

    // Constructor del adaptador
    public ProductAdapterAdmin(List<Product> products, OnProductoClickListener listener) {
        this.products = products;
        this.listener = listener;
        this.isEditing = new ArrayList<>(Collections.nCopies(products.size(), false)); // Inicialmente, todos los productos no están en edición
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño del producto para cada elemento del RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        // Asigna los valores del producto a los campos de texto
        Product product = products.get(position);
        holder.nombreProducto.setText(product.getNombre_producto());
        holder.descripcionProducto.setText(product.getDescripcion());

        // Formatear el precio con el signo de pesos y 2 decimales
        holder.etPrecioProducto.setText(String.format("$%.2f", product.getPrecio()));

        // Si el producto está en modo edición, habilitamos los campos y mostramos el botón de "Guardar"
        if (isEditing.get(position)) {
            holder.nombreProducto.setEnabled(true);
            holder.descripcionProducto.setEnabled(true);
            holder.etPrecioProducto.setEnabled(true);
            holder.btnGuardar.setVisibility(View.VISIBLE);
            holder.btnEditar.setVisibility(View.GONE); // Ocultamos el botón de "Editar"
        } else {
            // Si no está en modo edición, deshabilitamos los campos y mostramos el botón de "Editar"
            holder.nombreProducto.setEnabled(false);
            holder.descripcionProducto.setEnabled(false);
            holder.etPrecioProducto.setEnabled(false);
            holder.btnGuardar.setVisibility(View.GONE);
            holder.btnEditar.setVisibility(View.VISIBLE); // Mostramos el botón de "Editar"
        }

        // Maneja el clic en "Editar" desde el adaptador
        holder.btnEditar.setOnClickListener(v -> {
            // Cambiar el estado de edición y notificar el cambio
            setEditing(position, true);
        });

        // Maneja el clic en "Guardar" desde el adaptador
        holder.btnGuardar.setOnClickListener(v -> {
            // Obtiene los valores de los campos de texto
            String nuevoNombre = holder.nombreProducto.getText().toString().trim();
            String nuevaDescripcion = holder.descripcionProducto.getText().toString().trim();
            String nuevoPrecioStr = holder.etPrecioProducto.getText().toString().trim();

            // Verificar si los campos están vacíos
            if (nuevoNombre.isEmpty() || nuevaDescripcion.isEmpty() || nuevoPrecioStr.isEmpty()) {
                Toast.makeText(holder.itemView.getContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
                return; // Salir si hay campos vacíos
            }

            // Si el precio es un número válido, proceder con la actualización
            try {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr.replace("$", "")); // Quitamos el símbolo $ antes de parsear
                listener.onGuardarClick(position, nuevoNombre, nuevaDescripcion, nuevoPrecio);

                // Mostrar Toast de éxito al guardar la edición
                Toast.makeText(holder.itemView.getContext(), "Producto editado con éxito", Toast.LENGTH_LONG).show();

                // Cambiar el estado de edición y notificar el cambio
                setEditing(position, false);
            } catch (NumberFormatException e) {
                // Manejar error si el precio no es un número válido
                Toast.makeText(holder.itemView.getContext(), "El precio debe ser un número válido", Toast.LENGTH_LONG).show();
            }
        });

        // Maneja el clic en "Borrar"
        holder.btnBorrar.setOnClickListener(v -> {
            // Crear el popup de confirmación para eliminar el producto
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setMessage("¿Está seguro de eliminar este producto?");
            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Llamar al listener para eliminar el producto si se confirma
                    if (listener != null) {
                        listener.onBorrarClick(position);
                    }
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Simplemente cerrar el diálogo
                    dialog.dismiss();
                }
            });

            // Mostrar el diálogo
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // Método para actualizar la lista de productos
    public void updateList(List<Product> newList) {
        products = newList;
        isEditing = new ArrayList<>(Collections.nCopies(newList.size(), false)); // Reiniciamos la lista de estados de edición
        notifyDataSetChanged();
    }

    // Método para activar o desactivar el estado de edición
    private void setEditing(int position, boolean editing) {
        isEditing.set(position, editing);
        notifyItemChanged(position);
    }

    // Método para asignar el listener
    public void setListener(OnProductoClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder que maneja la vista de cada producto
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        EditText nombreProducto, descripcionProducto, etPrecioProducto;
        ImageButton btnEditar, btnGuardar, btnBorrar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa los campos de texto y botones
            nombreProducto = itemView.findViewById(R.id.etNombreProducto);
            descripcionProducto = itemView.findViewById(R.id.etDescripcionProducto);
            etPrecioProducto = itemView.findViewById(R.id.etPrecioProducto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnGuardar = itemView.findViewById(R.id.btnGuardar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }

    // Método para obtener un producto en una posición específica
    public Product getProductAt(int position) {
        return products.get(position);
    }
}
