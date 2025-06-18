package com.example.riccoapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.adapter.CartAdapter;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.RetrofitClient;
import com.example.riccoapp.api.CompraRequest;
import com.example.riccoapp.api.DetalleCompra;
import com.example.riccoapp.api.CompraResponse;
import com.example.riccoapp.api.Product;
import com.example.riccoapp.api.CartManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import com.google.gson.Gson;
import android.content.Intent;
import android.os.CountDownTimer;
import java.util.Locale;
import android.os.Handler;




public class CartActivity extends BaseActivity implements CartAdapter.OnCantidadChangeListener {

    RecyclerView recyclerViewCart;
    TextView textTotal, tvCantidadProductos, tvTemporizador;
    Button btnFinalizarCompra;
    List<Product> productosEnCarrito = new ArrayList<>();
    CartAdapter cartAdapter;

    private CountDownTimer countDownTimer;
    private static final long TIEMPO_LIMITE_MS = 2 * 60 * 1000; // 2 minutos
    private static final String PREF_TIEMPO_INICIO = "tiempo_inicio_carrito";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerCarrito);
        textTotal = findViewById(R.id.tvTotalPrecio);
        tvCantidadProductos = findViewById(R.id.tvCantidadProductos);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        tvTemporizador = findViewById(R.id.tvTemporizador);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Cargar productos del carrito
        productosEnCarrito = CartManager.getInstance().getCarrito();

        cartAdapter = new CartAdapter(CartActivity.this, productosEnCarrito, this);
        recyclerViewCart.setAdapter(cartAdapter);

        calcularYMostrarTotal();
        actualizarCantidadProductos();

        iniciarTemporizadorSiEsNecesario();

        btnFinalizarCompra.setOnClickListener(v -> obtenerUsuarioYFinalizarCompra());
    }

    @Override
    public void onCantidadChanged() {
        calcularYMostrarTotal();
        actualizarCantidadProductos();
        guardarCarrito();
    }

    private void iniciarTemporizadorSiEsNecesario() {
        if (productosEnCarrito.isEmpty()) {
            tvTemporizador.setVisibility(View.GONE);
            return;
        }

        tvTemporizador.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        long tiempoInicio = prefs.getLong(PREF_TIEMPO_INICIO, -1);
        long ahora = System.currentTimeMillis();

        if (tiempoInicio == -1) {
            tiempoInicio = ahora;
            prefs.edit().putLong(PREF_TIEMPO_INICIO, tiempoInicio).apply();
        }

        long tiempoPasado = ahora - tiempoInicio;
        long tiempoRestante = TIEMPO_LIMITE_MS - tiempoPasado;

        if (tiempoRestante <= 0) {
            Toast.makeText(this, "Tiempo agotado. El carrito fue vaciado.", Toast.LENGTH_LONG).show();
            CartManager.getInstance().vaciarCarrito();
            vaciarCarritoEnSharedPreferences();
            getSharedPreferences("CartPrefs", MODE_PRIVATE).edit().remove(PREF_TIEMPO_INICIO).apply();
            updateCartUI();
            return;
        }

        countDownTimer = new CountDownTimer(tiempoRestante, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutos = millisUntilFinished / 60000;
                long segundos = (millisUntilFinished % 60000) / 1000;
                String tiempoFormateado = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
                tvTemporizador.setText("Tu carrito se vacía en: " + tiempoFormateado);
            }

            @Override
            public void onFinish() {
                Toast.makeText(CartActivity.this, "Tiempo agotado. El carrito fue vaciado.", Toast.LENGTH_LONG).show();

                CartManager.getInstance().vaciarCarrito();
                vaciarCarritoEnSharedPreferences();

                // Eliminar marca de tiempo del temporizador
                getSharedPreferences("CartPrefs", MODE_PRIVATE).edit().remove(PREF_TIEMPO_INICIO).apply();

                // Reiniciar contador de carrito
                SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                editor.putInt("countCart", 0);
                editor.apply();

                updateCartUI();
            }

        }.start();
    }

    private void calcularYMostrarTotal() {
        double total = 0;
        for (Product p : productosEnCarrito) {
            total += p.getPrecio() * p.getCantidad();
        }
        textTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void actualizarCantidadProductos() {
        int cantidadTotal = 0;
        for (Product p : productosEnCarrito) {
            cantidadTotal += p.getCantidad();
        }
        tvCantidadProductos.setText(cantidadTotal + (cantidadTotal == 1 ? " producto" : " productos"));
    }

    private void guardarCarrito() {
        SharedPreferences.Editor editor = getSharedPreferences("CartPrefs", MODE_PRIVATE).edit();
        String json = new Gson().toJson(productosEnCarrito);
        editor.putString("carrito", json);
        editor.apply();
    }

    private void vaciarCarrito() {
        CartManager.getInstance().vaciarCarrito();
        productosEnCarrito.clear();
        cartAdapter.notifyDataSetChanged();
        calcularYMostrarTotal();
        actualizarCantidadProductos();
        setResult(RESULT_OK);
        finish();
    }

    private void obtenerUsuarioYFinalizarCompra() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");
        if (userId.isEmpty()) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }
        finalizarCompra();
    }

    private void finalizarCompra() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = Integer.parseInt(prefs.getString("user_id", "0"));

        if (userId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        List<DetalleCompra> detallesCompra = new ArrayList<>();
        for (Product p : productosEnCarrito) {
            detallesCompra.add(new DetalleCompra(p.getCantidad(), p.getPrecio(), String.valueOf(p.getId()), p.getNombre_producto()));
        }

        CompraRequest compraRequest = new CompraRequest(
                "Compra de productos",
                calcularTotal(),
                userId,
                detallesCompra
        );

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.finalizarCompra(compraRequest).enqueue(new Callback<CompraResponse>() {
            @Override
            public void onResponse(Call<CompraResponse> call, Response<CompraResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getInitPoint() != null) {
                        abrirEnlacePago(response.body().getInitPoint());
                    } else {
                        Toast.makeText(CartActivity.this, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show();

                        CartManager.getInstance().vaciarCarrito();
                        vaciarCarritoEnSharedPreferences();

                        getSharedPreferences("CartPrefs", MODE_PRIVATE).edit().remove(PREF_TIEMPO_INICIO).apply();

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("countCart", 0);
                        editor.apply();

                        updateCartUI();

                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(CartActivity.this, MisComprasActivity.class);
                            startActivity(intent);
                            finish();
                        }, 1500);
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Error al procesar la compra", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CompraResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error al procesar la compra", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void vaciarCarritoEnSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("carrito");
        editor.apply();
    }

    private double calcularTotal() {
        double total = 0;
        for (Product p : productosEnCarrito) {
            total += p.getPrecio() * p.getCantidad();
        }
        return total;
    }

    private void abrirEnlacePago(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void updateCartUI() {
        productosEnCarrito.clear();
        cartAdapter.notifyDataSetChanged();
        calcularYMostrarTotal();
        actualizarCantidadProductos();
        tvTemporizador.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
