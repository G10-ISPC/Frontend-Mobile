package com.example.riccoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.R;
import com.example.riccoapp.api.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductoViewHolder> {

    private List<Product> listaProductos;
    private OnProductoClickListener listener; // Listener para los clics

    // Interfaz para manejar los clics en productos
    public interface OnProductoClickListener {
        void onProductoClick(Product product);
    }

    // Constructor que recibe la lista de productos y el listener
    public ProductAdapter(List<Product> listaProductos, OnProductoClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada producto (sin botones)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        // Obtén el producto de la lista
        Product producto = listaProductos.get(position);

        // Configura los campos con los valores del producto
        holder.nombreProducto.setText(producto.getNombre_producto());
        holder.descripcionProducto.setText(producto.getDescripcion());
        holder.etPrecioProducto.setText(String.valueOf(producto.getPrecio()));

        // Todos los campos deshabilitados para solo visualización
        holder.etPrecioProducto.setEnabled(false);
        holder.nombreProducto.setEnabled(false);
        holder.descripcionProducto.setEnabled(false);

        // Configurar el clic en el producto
        holder.itemView.setOnClickListener(v -> listener.onProductoClick(producto));
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        EditText nombreProducto, descripcionProducto;
        EditText etPrecioProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencia a los campos de texto
            nombreProducto = itemView.findViewById(R.id.etNombreProducto);
            descripcionProducto = itemView.findViewById(R.id.etDescripcionProducto);
            etPrecioProducto = itemView.findViewById(R.id.etPrecioProducto);
        }
    }
}
