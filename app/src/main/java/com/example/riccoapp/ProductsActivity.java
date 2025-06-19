package com.example.riccoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.adapter.ProductAdapter;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.CartManager;
import com.example.riccoapp.api.Product;
import com.example.riccoapp.api.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends BaseActivity implements ProductAdapter.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    private TextView cartCount;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        setupToolbar(); // Barra de navegación
        userNameTextView = findViewById(R.id.userNameTextView); // Asignación de TextView específico de esta Activity
        loadUserName(); // Carga y muestra el nombre del usuario

        cartCount = findViewById(R.id.cartCount);
        cargarProductosDelCarrito();
        updateCartBadge();

        findViewById(R.id.cartIcon).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String rol = prefs.getString("user_rol", "");

            if (!rol.equals("cliente")) {
                Toast.makeText(this, "No tienes permisos para acceder al carrito", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        loadProductsFromAPI();
    }

    private void loadProductsFromAPI() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("user_token", "");

        if (accessToken.isEmpty()) {
            Toast.makeText(this, "Token de acceso no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Product>> call = apiService.getProducts("Bearer " + accessToken);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    List<Product> productos = response.body();

                    List<Product> productosEnStock = new ArrayList<>();
                    for (Product product : productos) {
                        if (product.isVisible()) {
                            productosEnStock.add(product);
                        }
                    }

                    for (int i = 0; i < productosEnStock.size(); i++) {
                        int imagenId = (i % 10) + 1; // para asignar imagen
                        productosEnStock.get(i).setImagenId(imagenId);
                    }

                    productList.addAll(productosEnStock);
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("ProductsActivity", "Response error: " + response.message());
                    Toast.makeText(ProductsActivity.this, "Error al cargar los productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductsActivity", "API call failed: " + t.getMessage());
                Toast.makeText(ProductsActivity.this, "Error al cargar los productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarProductosDelCarrito() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("carrito", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            List<Product> productosSeleccionados = gson.fromJson(json, type);
            CartManager.getInstance().setCarrito(productosSeleccionados);
        }
    }

    @Override
    public void onProductoClick(Product product) {
        if (product.getStock() > 0) {
            CartManager.getInstance().agregarProducto(product);
            updateCartBadge();
            guardarProductosEnSharedPreferences();
            Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay stock disponible para este producto", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartBadge() {
        int count = CartManager.getInstance().getCantidadTotalProductos();
        if (count > 0) {
            cartCount.setText(String.valueOf(count));
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }
    }

    private void guardarProductosEnSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(CartManager.getInstance().getCarrito());
        editor.putString("carrito", json);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();

    }

}
