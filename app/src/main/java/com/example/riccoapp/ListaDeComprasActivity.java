package com.example.riccoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.adapter.ComprasAdapter;
import com.example.riccoapp.Models.Compra;
import com.example.riccoapp.Models.Detalle;
import com.example.riccoapp.api.CompraService;
import com.example.riccoapp.api.RetrofitClient;
import com.example.riccoapp.api.TokenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaDeComprasActivity extends AppCompatActivity {

    private List<Compra> listaCompras = new ArrayList<>();
    private List<Compra> comprasFiltradas = new ArrayList<>();

    private EditText editTextUsuario, editTextProducto, editTextFecha;
    private Button btnLimpiarFiltros, btnExportar, btnToggleFiltros;
    private LinearLayout layoutFiltros;
    private RecyclerView recyclerView;
    private ComprasAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_de_compras);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔗 Referencias UI
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextProducto = findViewById(R.id.editTextProducto);
        editTextFecha = findViewById(R.id.editTextFecha);
        btnLimpiarFiltros = findViewById(R.id.btnLimpiarFiltros);
        btnExportar = findViewById(R.id.btnExportar);
        btnToggleFiltros = findViewById(R.id.btnToggleFiltros);
        layoutFiltros = findViewById(R.id.layoutFiltros);
        recyclerView = findViewById(R.id.recyclerViewCompras);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComprasAdapter(comprasFiltradas);
        recyclerView.setAdapter(adapter);

        // 🧠 Filtros toggle
        btnToggleFiltros.setOnClickListener(v -> {
            if (layoutFiltros.getVisibility() == View.GONE) {
                layoutFiltros.setVisibility(View.VISIBLE);
            } else {
                layoutFiltros.setVisibility(View.GONE);
            }
        });

        // 🚀 Eventos
        btnLimpiarFiltros.setOnClickListener(v -> limpiarFiltros());
        btnExportar.setOnClickListener(v -> exportarExcel());

        // 🚚 Cargar datos desde backend
        obtenerTodasLasCompras();
    }

    private void obtenerTodasLasCompras() {
        // Obtenemos el token
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        TokenManager tokenManager = new TokenManager(sharedPreferences);
        String token = tokenManager.obtenerToken();

        if (token == null) {
            Toast.makeText(this, "Token no disponible. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construir cliente con header Authorization
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + token) // <--- clave
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/api/") // Asegúrate de que este IP funciona desde tu emulador
                .client(client)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        CompraService service = retrofit.create(CompraService.class);
        Call<List<Compra>> call = service.getCompras();

        call.enqueue(new Callback<List<Compra>>() {
            @Override
            public void onResponse(Call<List<Compra>> call, Response<List<Compra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCompras = response.body();
                    comprasFiltradas.clear();
                    comprasFiltradas.addAll(listaCompras);
                    adapter.setCompras(comprasFiltradas);
                } else {
                    Toast.makeText(ListaDeComprasActivity.this, "Error al obtener compras", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compra>> call, Throwable t) {
                Toast.makeText(ListaDeComprasActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filtrarCompras() {
        String filtroUsuario = editTextUsuario.getText().toString().toLowerCase();
        String filtroProducto = editTextProducto.getText().toString().toLowerCase();
        String filtroFecha = editTextFecha.getText().toString();

        comprasFiltradas.clear();

        for (Compra compra : listaCompras) {
            String descripcion = generarDescripcionCompra(compra.detalles).toLowerCase();

            boolean coincideProducto = descripcion.contains(filtroProducto);
            boolean coincideUsuario = (compra.user_first_name + " " + compra.user_last_name).toLowerCase().contains(filtroUsuario);
            boolean coincideFecha = filtroFecha.isEmpty() || compra.fecha.equals(filtroFecha);

            if (coincideProducto && coincideUsuario && coincideFecha) {
                comprasFiltradas.add(compra);
            }
        }

        adapter.setCompras(comprasFiltradas);
    }

    private void limpiarFiltros() {
        editTextUsuario.setText("");
        editTextProducto.setText("");
        editTextFecha.setText("");
        filtrarCompras();
    }

    private String generarDescripcionCompra(List<Detalle> detalles) {
        Map<String, Integer> conteo = new HashMap<>();

        for (Detalle detalle : detalles) {
            int cantidadExistente = conteo.containsKey(detalle.nombre_producto) ? conteo.get(detalle.nombre_producto) : 0;
            conteo.put(detalle.nombre_producto, cantidadExistente + detalle.cantidad);
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(entry.getValue()).append(" ").append(entry.getKey());
        }

        return sb.toString();
    }

    private void exportarExcel() {
        Toast.makeText(this, "Exportar Excel pendiente de implementación", Toast.LENGTH_SHORT).show();
    }
}
