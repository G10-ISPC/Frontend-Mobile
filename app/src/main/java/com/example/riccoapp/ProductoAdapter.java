package com.example.riccoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private OnProductoClickListener listener;

    // Interfaz que maneja las acciones de los botones de editar y borrar
    public interface OnProductoClickListener {
        void onEditarClick(int position);
        void onBorrarClick(int position);
    }

    // Constructor del adapter
    public ProductoAdapter(List<Producto> listaProductos, OnProductoClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.nombreProducto.setText(producto.getNombre());
        holder.descripcionProducto.setText(producto.getDescripcion());
        holder.precioProducto.setText(String.format("Precio: $%.2f", producto.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Método para actualizar un producto en una posición específica
    public void actualizarProducto(int position, Producto productoActualizado) {
        listaProductos.set(position, productoActualizado); // Actualiza el producto en la lista
        notifyItemChanged(position); // Notifica que esa posición ha cambiado para que se actualice la vista
    }

    // Método para agregar un nuevo producto a la lista y notificar al adaptador
    public void agregarProducto(Producto nuevoProducto) {
        listaProductos.add(nuevoProducto);  // Agregar el nuevo producto a la lista
        notifyItemInserted(listaProductos.size() - 1);  // Notificar que un nuevo producto fue insertado
    }

    // Método para eliminar un producto de la lista y notificar al adaptador
    public void eliminarProducto(int position) {
        if (position >= 0 && position < listaProductos.size()) {
            listaProductos.remove(position); // Elimina el producto de la lista
            notifyItemRemoved(position); // Notifica que se ha eliminado un producto
            notifyItemRangeChanged(position, listaProductos.size()); // Notifica el cambio en el rango
        }
    }

    // ViewHolder que contiene los elementos de la vista
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, descripcionProducto, precioProducto;
        ImageButton btnEditar, btnBorrar;

        public ProductoViewHolder(@NonNull View itemView, OnProductoClickListener listener) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            descripcionProducto = itemView.findViewById(R.id.tvDescripcionProducto);
            precioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);

            // Manejo de clic en el botón Editar
            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditarClick(position);
                    }
                }
            });

            // Manejo de clic en el botón Borrar
            btnBorrar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onBorrarClick(position);
                    }
                }
            });
        }
    }
}

