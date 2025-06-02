package com.example.riccoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.adapter.CompraAdapter;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.RetrofitClient;
import com.example.riccoapp.api.Compra;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MisComprasActivity extends AppCompatActivity implements CompraAdapter.OnActionClickListener {

    private RecyclerView recyclerView;
    private CompraAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_compras);

        // 1. Obtener SharedPreferences correcto
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // 2. Depuración: Imprimir todas las preferencias
        Log.d("PREFS_DEBUG", "===== TODAS LAS PREFERENCIAS =====");
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("PREFS_DEBUG", entry.getKey() + ": " + entry.getValue().toString());
        }

        // 3. Obtener token con la clave correcta
        String authToken = prefs.getString("user_token", "");
        Log.d("TOKEN_DEBUG", "Token recuperado: " + authToken);

        // 4. Verificar si el token existe
        if (authToken.isEmpty()) {
            Toast.makeText(this, "No estás autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("TOKEN_DEBUG", "Token completo: " + authToken);

        // 6. Configurar Retrofit con el token
        try {
            Retrofit retrofit = RetrofitClient.getRetrofitInstanceWithToken(authToken);
            Log.d("TOKEN_DEBUG", "Token completo: " + authToken);
            apiService = retrofit.create(ApiService.class);
        } catch (Exception e) {
            Log.e("RETROFIT_ERROR", "Error al crear instancia de Retrofit: " + e.getMessage());
            Toast.makeText(this, "Error de configuración", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 7. Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMisCompras);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 8. Cargar datos
        loadPurchases();
    }

    private void loadPurchases() {
        Call<List<Compra>> call = apiService.getMisCompras();

        call.enqueue(new Callback<List<Compra>>() {
            @Override
            public void onResponse(Call<List<Compra>> call, Response<List<Compra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 9. Éxito: Mostrar datos
                    adapter = new CompraAdapter(response.body(), MisComprasActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    // 10. Manejar errores del servidor
                    Log.e("API_ERROR", "Código: " + response.code());

                    if (response.errorBody() != null) {
                        try {
                            Log.e("API_ERROR", "Mensaje: " + response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (response.code() == 401) {
                        Toast.makeText(MisComprasActivity.this, "Sesión expirada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MisComprasActivity.this,
                                "Error del servidor: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Compra>> call, Throwable t) {
                // 11. Manejar errores de conexión
                Log.e("NETWORK_ERROR", "Error de conexión: " + t.getMessage());
                Toast.makeText(MisComprasActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCancelClick(int compraId) {
        // 12. Implementar lógica de cancelación (pendiente)
        Toast.makeText(this, "Cancelar compra: " + compraId, Toast.LENGTH_SHORT).show();
    }
}