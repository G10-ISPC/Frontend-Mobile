package com.example.riccoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.R;
import com.example.riccoapp.api.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> listaCarrito;
    private OnCantidadChangeListener cantidadChangeListener;

    public CartAdapter(List<Product> listaCarrito, OnCantidadChangeListener listener) {
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
        holder.tvNombre.setText(producto.getNombre_producto());
        holder.tvPrecio.setText("$" + String.format("%.2f", producto.getPrecio()));
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));

        holder.btnSumar.setOnClickListener(v -> {
            producto.setCantidad(producto.getCantidad() + 1);
            notifyItemChanged(position);
            if (cantidadChangeListener != null) {
                cantidadChangeListener.onCantidadChanged();
            }
        });

        holder.btnRestar.setOnClickListener(v -> {
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
                notifyItemChanged(position);
                if (cantidadChangeListener != null) {
                    cantidadChangeListener.onCantidadChanged();
                }
            }
        });

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
        TextView tvNombre, tvPrecio, tvCantidad;
        Button btnSumar, btnRestar;
        ImageView btnEliminar;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    public interface OnCantidadChangeListener {
        void onCantidadChanged();
    }
}