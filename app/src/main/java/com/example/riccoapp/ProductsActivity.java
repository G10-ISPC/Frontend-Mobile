package com.example.riccoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riccoapp.adapter.ProductAdapter;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.CartManager;
import com.example.riccoapp.api.Product;
import com.example.riccoapp.api.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import androidx.annotation.Nullable;



public class ProductsActivity extends BaseActivity implements ProductAdapter.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    private TextView cartCount;
    private int cartItemCount = 0;
    private List<Product> productosSeleccionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products); // Conecta con el layout XML

        setupToolbar(); // Barra superior de navegación
        userNameTextView = findViewById(R.id.userNameTextView); // Mostrar nombre del usuario
        loadUserName(); // Carga nombre desde SharedPreferences

        cartCount = findViewById(R.id.cartCount); // Referencia al contador del carrito
        cargarProductosDelCarrito(); // Carga los productos guardados previamente
        updateCartBadge(); // Actualiza el contador de productos visibles

        // Evento clic en el ícono del carrito
        findViewById(R.id.cartIcon).setOnClickListener(v -> {
            Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
            startActivityForResult(intent, 100);
        });

        // Inicialización de lista y RecyclerView
        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa el adaptador con la lista vacía y this como listener
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        loadProductsFromAPI(); // Llama a la API para obtener productos
    }

    // ===========================
    // Cargar productos desde la API
    // ===========================
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

                    // Solo productos visibles
                    List<Product> productosEnStock = new ArrayList<>();
                    for (Product product : productos) {
                        if (product.isVisible()) {
                            productosEnStock.add(product);
                        }
                    }

                    // Asignar imagen ficticia según posición
                    for (int i = 0; i < productosEnStock.size(); i++) {
                        int imagenId = (i % 10) + 1; // Asume que tienes 10 imágenes
                        productosEnStock.get(i).setImagenId(imagenId);
                    }

                    productList.addAll(productosEnStock);
                    productAdapter.notifyDataSetChanged(); // Refresca la lista
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

    // ===========================
    // Cargar carrito guardado
    // ===========================
    private void cargarProductosDelCarrito() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("carrito", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>() {
            }.getType();
            productosSeleccionados = gson.fromJson(json, type);
            cartItemCount = productosSeleccionados.size();
        } else {
            cartItemCount = 0;
        }
        updateCartBadge(); // Actualiza contador
    }

    // ===========================
// Método que se llama al hacer clic en un producto
// ===========================
    @Override
    public void onProductoClick(Product product) {
        CartManager.getInstance().agregarProducto(product); // Agrega al singleton
        updateCartBadge(); // Actualiza el contador
        guardarProductosEnSharedPreferences(); // ✅ Guarda en SharedPreferences
        Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
    }

    // ===========================
// Actualiza el contador del carrito
// ===========================
    private void updateCartBadge() {
        int count = CartManager.getInstance().getCantidadTotalProductos();  // Obtiene el tamaño real del carrito
        if (count > 0) {
            cartCount.setText(String.valueOf(count));
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }
    }

    // ===========================
    // Guarda el carrito actualizado en SharedPreferences
    // ===========================
    private void guardarProductosEnSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(CartManager.getInstance().getCarrito()); // ✅ Guarda lo actual del singleton

        editor.putString("carrito", json);
        editor.apply();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            // El carrito fue modificado → actualiza el badge
            updateCartBadge();
            guardarProductosEnSharedPreferences(); // Si querés guardar el carrito actualizado
        }
    }

}