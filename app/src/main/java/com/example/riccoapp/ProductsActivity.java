package com.example.riccoapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.riccoapp.adapter.ProductAdapter;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.Product;
import com.example.riccoapp.api.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnProductoClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;  // Adaptador corregido a ProductoAdapter
    private List<Product> productList;  // Lista de productos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);  // Conectar con el XML

        // Inicializar la lista de productos
        productList = new ArrayList<>();

        // Vincular el RecyclerView con el XML
        recyclerView = findViewById(R.id.recyclerViewProductos);  // ID correcto que coincide con el XML

        // Configurar el administrador de diseño (en este caso, LinearLayout para una lista vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el adaptador con la lista vacía y el listener
        productAdapter = new ProductAdapter(productList, this);  // `this` es el listener
        recyclerView.setAdapter(productAdapter);  // Asignar el adaptador al RecyclerView

        // Cargar la lista de productos desde la API
        loadProductsFromAPI();
    }

    private void loadProductsFromAPI() {
        // Crear una instancia de ApiService
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Realizar la llamada a la API
        Call<List<Product>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    List<Product> productos = response.body();

                    // Filtrar los productos para mostrar solo los que están en stock
                    List<Product> productosEnStock = new ArrayList<>();
                    for (Product product : productos) {
                        if (product.isInStock()) {
                            productosEnStock.add(product);
                        }
                    }

                    // Asignar imágenes según la posición o cualquier otra lógica
                    for (int i = 0; i < productosEnStock.size(); i++) {
                        // Asignar un identificador de imagen basado en el índice (bur1, bur2, etc.)
                        int imagenId = (i % 10) + 1;  // Si tienes 10 imágenes
                        productosEnStock.get(i).setImagenId(imagenId);
                    }

                    productList.addAll(productosEnStock);  // Añadir productos en stock a la lista
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("ProductsActivity", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductsActivity", "API call failed: " + t.getMessage());
                Toast.makeText(ProductsActivity.this, "Error al cargar los productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método de la interfaz OnProductoClickListener
    @Override
    public void onProductoClick(Product product) {
        // Acción al hacer clic en un producto
    }
}
