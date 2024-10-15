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


public class ProductAdapterAdmin extends RecyclerView.Adapter<ProductAdapterAdmin.ViewHolder> {
    private List<Product> products;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onEditarClick(int position);
        void onGuardarClick(int position, String nuevoNombre, String nuevaDescripcion, double nuevoPrecio);
        void onBorrarClick(int position);
    }

    public ProductAdapterAdmin(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_producto, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nombre.setText(product.getNombre_producto());
        holder.descripcion.setText(product.getDescripcion());
        holder.precio.setText(String.valueOf(product.getPrecio()));
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, precio;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.etNombreProducto);
            descripcion = itemView.findViewById(R.id.etDescripcionProducto);
            precio = itemView.findViewById(R.id.etPrecioProducto);
        }
    }


    public void updateList(List<Product> newList) {
        products = newList;
        notifyDataSetChanged();
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

