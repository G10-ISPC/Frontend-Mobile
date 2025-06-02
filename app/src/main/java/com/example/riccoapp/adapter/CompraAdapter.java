package com.example.riccoapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riccoapp.R;
import com.example.riccoapp.api.Compra;
import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    private List<Compra> compras;
    private OnActionClickListener listener;

    public CompraAdapter(List<Compra> compras, OnActionClickListener listener) {
        this.compras = compras;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compra, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        Compra compra = compras.get(position);
        holder.bind(compra);
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    public class CompraViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvFecha, tvDescripcion, tvPrecio, tvEstado;
        Button btnAccion;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnAccion = itemView.findViewById(R.id.btnAccion);
        }

        public void bind(Compra compra) {
            // Depuración
            Log.d("COMPRA_DEBUG", "ID: " + compra.getId());
            Log.d("COMPRA_DEBUG", "Precio: " + compra.getPrecio());
            Log.d("COMPRA_DEBUG", "Fecha: " + compra.getFecha());
            Log.d("COMPRA_DEBUG", "Descripción: " + compra.getDescripcion());

            // Mostrar datos
            tvId.setText(String.valueOf(compra.getId()));

            // Formatear fecha
            String fecha = compra.getFecha();
            if (fecha != null && fecha.contains("T")) {
                fecha = fecha.split("T")[0];
            }
            tvFecha.setText(fecha);

            // Descripción
            tvDescripcion.setText(compra.getDescripcion());

            // Precio
            tvPrecio.setText(String.format(" %.2f", compra.getPrecio()));

            // Estado
            String estado = compra.getEstado();
            if (estado != null && estado.length() > 0) {
                estado = estado.substring(0, 1).toUpperCase() + estado.substring(1).toLowerCase();
            }
            tvEstado.setText(estado);

            // Botón de acciones
            if ("pendiente".equalsIgnoreCase(compra.getEstado())) {
                btnAccion.setVisibility(View.VISIBLE);
                btnAccion.setOnClickListener(v -> listener.onCancelClick(compra.getId()));
            } else {
                btnAccion.setVisibility(View.GONE);
            }
        }
    }

    public interface OnActionClickListener {
        void onCancelClick(int compraId);
    }
}