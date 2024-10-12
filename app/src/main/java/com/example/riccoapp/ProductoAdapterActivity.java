package com.example.riccoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductoAdapterActivity extends RecyclerView.Adapter<ProductoAdapterActivity.ProductoViewHolder> {

    private List<ProductoActivity> listaProductos;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onEditarClick(int position);
        void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio);
        void onBorrarClick(int position);
    }

    public ProductoAdapterActivity(List<ProductoActivity> listaProductos, OnProductoClickListener listener) {
        this.listaProductos = listaProductos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_producto, parent, false);
        return new ProductoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoActivity producto = listaProductos.get(position);
        holder.nombreProducto.setText(producto.getNombre());
        holder.descripcionProducto.setText(producto.getDescripcion());
        holder.etPrecioProducto.setText(String.valueOf(producto.getPrecio()));

        if (producto.isEditando()) {
            holder.etPrecioProducto.setEnabled(true);
            holder.nombreProducto.setEnabled(true); // Habilita el campo de nombre
            holder.descripcionProducto.setEnabled(true); // Habilita el campo de descripción
            holder.btnGuardar.setVisibility(View.VISIBLE);
        } else {
            holder.etPrecioProducto.setEnabled(false);
            holder.nombreProducto.setEnabled(false); // Deshabilita el campo de nombre
            holder.descripcionProducto.setEnabled(false); // Deshabilita el campo de descripción
            holder.btnGuardar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        EditText nombreProducto, descripcionProducto;
        EditText etPrecioProducto;
        ImageButton btnEditar, btnBorrar, btnGuardar;

        public ProductoViewHolder(@NonNull View itemView, OnProductoClickListener listener) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.etNombreProducto);
            descripcionProducto = itemView.findViewById(R.id.etDescripcionProducto);
            etPrecioProducto = itemView.findViewById(R.id.etPrecioProducto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            btnGuardar = itemView.findViewById(R.id.btnGuardar);

            btnEditar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditarClick(position);
                    }
                }
            });

            btnGuardar.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String nuevoNombre = nombreProducto.getText().toString().trim();
                        String nuevaDescripcion = descripcionProducto.getText().toString().trim();
                        String nuevoPrecioStr = etPrecioProducto.getText().toString().trim();

                        if (!nuevoPrecioStr.isEmpty()) {
                            double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                            listener.onGuardarClick(position, nuevoNombre, nuevaDescripcion, nuevoPrecio);
                        }
                    }
                }
            });

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
