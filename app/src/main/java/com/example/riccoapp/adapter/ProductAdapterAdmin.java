package com.example.riccoapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.riccoapp.R;
import com.example.riccoapp.api.Product;


import java.util.List;


public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ProductoViewHolder> {
    private List<Product> products;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onEditarClick(int position);
        void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio);
        void onBorrarClick(int position);
    }

    public ProductAdapterAdmin(List<Product> products, OnProductoClickListener listener) {
        this.products = products;
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
        Product product = products.get(position);
        holder.nombreProducto.setText(product.getNombre_producto());
        holder.descripcionProducto.setText(product.getDescripcion());
        holder.etPrecioProducto.setText(String.valueOf(product.getPrecio()));

        // Desactivar campos para edición al inicio
        holder.nombreProducto.setEnabled(false);
        holder.descripcionProducto.setEnabled(false);
        holder.etPrecioProducto.setEnabled(false);

        holder.btnEditar.setOnClickListener(v -> {
            // Habilitar edición
            holder.nombreProducto.setEnabled(true);
            holder.descripcionProducto.setEnabled(true);
            holder.etPrecioProducto.setEnabled(true);
            holder.btnGuardar.setVisibility(View.VISIBLE);
            holder.btnEditar.setVisibility(View.GONE);
        });

        holder.btnGuardar.setOnClickListener(v -> {
            // Guardar cambios
            String nuevoNombre = holder.nombreProducto.getText().toString().trim();
            String nuevaDescripcion = holder.descripcionProducto.getText().toString().trim();
            String nuevoPrecioStr = holder.etPrecioProducto.getText().toString().trim();
            if (!nuevoPrecioStr.isEmpty()) {
                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);
                listener.onGuardarClick(position, nuevoNombre, nuevaDescripcion, nuevoPrecio);
            }

            // Desactivar edición y mostrar botón de Editar
            holder.nombreProducto.setEnabled(false);
            holder.descripcionProducto.setEnabled(false);
            holder.etPrecioProducto.setEnabled(false);
            holder.btnGuardar.setVisibility(View.GONE);
            holder.btnEditar.setVisibility(View.VISIBLE);
        });

        holder.btnBorrar.setOnClickListener(v -> listener.onBorrarClick(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        EditText nombreProducto, descripcionProducto, etPrecioProducto;
        ImageButton btnEditar, btnGuardar, btnBorrar;

        public ProductoViewHolder(@NonNull View itemView, OnProductoClickListener listener) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.etNombreProducto);
            descripcionProducto = itemView.findViewById(R.id.etDescripcionProducto);
            etPrecioProducto = itemView.findViewById(R.id.etPrecioProducto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnGuardar = itemView.findViewById(R.id.btnGuardar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }

    public void updateList(List<Product> newList) {
        products = newList;
        notifyDataSetChanged();
    }

    public Product getProductAt(int position) {
        return products.get(position);
    }

}