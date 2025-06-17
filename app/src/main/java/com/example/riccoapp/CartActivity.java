package com.example.riccoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
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


public class CartActivity extends BaseActivity implements CartAdapter.OnCantidadChangeListener {

    RecyclerView recyclerViewCart;
    TextView textTotal, tvCantidadProductos;
    Button btnFinalizarCompra, btnVaciarCarrito;
    List<Product> productosEnCarrito = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerCarrito);
        textTotal = findViewById(R.id.tvTotalPrecio);
        tvCantidadProductos = findViewById(R.id.tvCantidadProductos);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        //btnVaciarCarrito = findViewById(R.id.btnVaciarCarrito);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Cargar productos del carrito
        productosEnCarrito = CartManager.getInstance().getCarrito();

        // Inicializar Adapter con la lista correcta
        cartAdapter = new CartAdapter(CartActivity.this, productosEnCarrito, this);
        recyclerViewCart.setAdapter(cartAdapter);

        calcularYMostrarTotal();
        actualizarCantidadProductos();

        btnFinalizarCompra.setOnClickListener(v -> obtenerUsuarioYFinalizarCompra());
        //btnVaciarCarrito.setOnClickListener(v -> vaciarCarrito());
    }

    @Override
    public void onCantidadChanged() {
        calcularYMostrarTotal();
        actualizarCantidadProductos();
        guardarCarrito();
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
        Log.d("CartActivity", "User ID recuperado: " + userId);
        //Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_LONG).show();

        if (userId.isEmpty()) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        finalizarCompra();
    }

    private void finalizarCompra() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = Integer.parseInt(prefs.getString("user_id", "0"));
        Log.d("CartActivity", "User ID recuperado: " + userId);

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

        Log.d("CartActivity", "JSON enviado: " + new Gson().toJson(compraRequest));

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.finalizarCompra(compraRequest).enqueue(new Callback<CompraResponse>() {
            @Override
            public void onResponse(Call<CompraResponse> call, Response<CompraResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getInitPoint() != null) {
                        abrirEnlacePago(response.body().getInitPoint());
                    } else {
                        Toast.makeText(CartActivity.this, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show();

                        // Vaciar carrito
                        CartManager.getInstance().vaciarCarrito();

                        // Actualizar UI
                        updateCartUI();

                        // Retrasar la apertura de la siguiente actividad para que se vea el Toast
                        new android.os.Handler().postDelayed(() -> {
                            Intent intent = new Intent(CartActivity.this, MisComprasActivity.class);
                            startActivity(intent);
                            finish();
                        }, 1500);  // 1.5 segundos de delay
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


    private double calcularTotal() {
        double total = 0;
        for (Product p : productosEnCarrito) {
            total += p.getPrecio() * p.getCantidad();
        }
        return total;
    }

    private void abrirEnlacePago(String url) {
        startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url)));
    }

    private void updateCartUI() {
        productosEnCarrito.clear(); // Vacía lista local
        cartAdapter.notifyDataSetChanged();
        // Refresca RecyclerView
        calcularYMostrarTotal(); // Muestra el nuevo total
    }


}
