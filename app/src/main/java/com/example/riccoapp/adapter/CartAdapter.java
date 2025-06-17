package com.example.riccoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.R;
import com.example.riccoapp.api.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> listaCarrito;
    private final OnCantidadChangeListener cantidadChangeListener;
    private final Context context;

    public CartAdapter(Context context, List<Product> listaCarrito, OnCantidadChangeListener listener) {
        this.context = context;
        this.listaCarrito = listaCarrito;
        this.cantidadChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product producto = listaCarrito.get(position);

        // Seteamos los datos visibles
        holder.tvNombre.setText(producto.getNombre_producto());
        holder.tvPrecio.setText("$" + String.format("%.2f", producto.getPrecio()));
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.tvStockDisponible.setText("Stock disponible: " + producto.getStock());

        // Botón para aumentar cantidad (validando stock)
        holder.btnSumar.setOnClickListener(v -> {
            if (producto.getCantidad() < producto.getStock()) {
                producto.setCantidad(producto.getCantidad() + 1);
                notifyItemChanged(position);
                if (cantidadChangeListener != null) {
                    cantidadChangeListener.onCantidadChanged();
                }
            } else {
                Toast.makeText(context, "No hay más stock disponible", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para disminuir cantidad (mínimo 1)
        holder.btnRestar.setOnClickListener(v -> {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
                notifyItemChanged(position);
                if (cantidadChangeListener != null) {
                    cantidadChangeListener.onCantidadChanged();
                }
            }
        });

        // Botón para eliminar producto del carrito
        holder.btnEliminar.setOnClickListener(v -> {
            listaCarrito.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listaCarrito.size());
            if (cantidadChangeListener != null) {
                cantidadChangeListener.onCantidadChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre, tvPrecio, tvCantidad, tvStockDisponible;
        final Button btnSumar, btnRestar;
        final ImageView btnEliminar;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvStockDisponible = itemView.findViewById(R.id.tvStockDisponible);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    // Interfaz para notificar cambios de cantidad al activity o fragment que usa el adapter
    public interface OnCantidadChangeListener {
        void onCantidadChanged();
    }
}
