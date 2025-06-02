package com.example.riccoapp.adapter;

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
            tvId.setText(String.valueOf(compra.getId()));
            tvFecha.setText(compra.getFecha());
            tvDescripcion.setText(compra.getDescripcion());
            tvPrecio.setText(String.format("S/ %.2f", compra.getPrecio()));
            tvEstado.setText(compra.getEstado());

            // Mostrar botón solo para estados cancelables
            if ("pendiente".equalsIgnoreCase(compra.getEstado())) {
                btnAccion.setVisibility(View.VISIBLE);
                btnAccion.setText("Cancelar");
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