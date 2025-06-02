package com.example.riccoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.Models.Compra;
import com.example.riccoapp.Models.Detalle;
import com.example.riccoapp.R;

import java.util.List;

public class ComprasAdapter extends RecyclerView.Adapter<ComprasAdapter.ViewHolder> {

    private List<Compra> compras;

    public ComprasAdapter(List<Compra> compras) {
        this.compras = compras;
    }

    public void setCompras(List<Compra> nuevasCompras) {
        this.compras = nuevasCompras;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todaslascompras, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Compra compra = compras.get(position);
        holder.bind(compra);
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textUsuario;
        private final TextView textFecha;
        private final TextView textDetalle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsuario = itemView.findViewById(R.id.textUsuario);
            textFecha = itemView.findViewById(R.id.textFecha);
            textDetalle = itemView.findViewById(R.id.textDetalle);
        }

        public void bind(Compra compra) {
            String usuario = compra.user_first_name + " " + compra.user_last_name;
            textUsuario.setText(usuario);
            textFecha.setText(compra.fecha);

            StringBuilder detalles = new StringBuilder();
            for (Detalle detalle : compra.detalles) {
                detalles.append(detalle.cantidad)
                        .append(" x ")
                        .append(detalle.nombre_producto)
                        .append("\n");
            }

            textDetalle.setText(detalles.toString().trim());
        }
    }
}
