package com.example.riccoapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.Models.Compra;
import com.example.riccoapp.Models.CompraEstadoRequest;
import com.example.riccoapp.Models.Detalle;
import com.example.riccoapp.R;
import com.example.riccoapp.api.CompraService;
import com.example.riccoapp.api.RetrofitClient;
import com.example.riccoapp.api.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        private final Spinner spinnerEstado;
        private final Button btnGuardarEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsuario = itemView.findViewById(R.id.textUsuario);
            textFecha = itemView.findViewById(R.id.textFecha);
            textDetalle = itemView.findViewById(R.id.textDetalle);
            spinnerEstado = itemView.findViewById(R.id.spinnerEstado);
            btnGuardarEstado = itemView.findViewById(R.id.btnGuardarEstado);
        }

        public void bind(Compra compra) {
            // Mostrar usuario y fecha
            String usuario = compra.user_first_name + " " + compra.user_last_name;
            textUsuario.setText(usuario);
            textFecha.setText(compra.fecha);

            // Mostrar detalles
            StringBuilder detalles = new StringBuilder();
            for (Detalle detalle : compra.detalles) {
                detalles.append(detalle.cantidad)
                        .append(" x ")
                        .append(detalle.nombre_producto)
                        .append("\n");
            }
            textDetalle.setText(detalles.toString().trim());

            // Spinner de estados
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    itemView.getContext(),
                    R.array.estados_array,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstado.setAdapter(adapter);

            if (compra.estado != null) {
                int pos = adapter.getPosition(compra.estado);
                if (pos >= 0) spinnerEstado.setSelection(pos);
            }

            // Botón guardar
            btnGuardarEstado.setOnClickListener(v -> {
                String nuevoEstado = spinnerEstado.getSelectedItem().toString();

                Context context = itemView.getContext();
                SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                TokenManager tokenManager = new TokenManager(prefs);
                String token = tokenManager.obtenerToken();

                if (token == null) {
                    Toast.makeText(context, "No hay token", Toast.LENGTH_SHORT).show();
                    return;
                }

                Retrofit retrofit = RetrofitClient.getRetrofitInstanceWithToken(token);
                CompraService service = retrofit.create(CompraService.class);

                CompraEstadoRequest request = new CompraEstadoRequest(nuevoEstado);
                service.actualizarEstado(compra.id_compra, request).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
