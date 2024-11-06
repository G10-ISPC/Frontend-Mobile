package com.example.riccoapp;

import static android.content.Context.MODE_PRIVATE;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.Product;
import com.example.riccoapp.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ProductoViewModel extends AndroidViewModel {
    private MutableLiveData<List<Product>> productos;
    private ApiService apiService;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<List<Product>> getProductos() {
        if (productos == null) {
            productos = new MutableLiveData<>();
            loadProductos();
        }
        return productos;
    }

    public void loadProductos() {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productos.setValue(response.body());
                } else {
                    Log.e("ProductoViewModel", "Error en la respuesta: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al cargar productos", t);
            }
        });
    }

    public void addProducto(Product product) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("user_token", "");
        apiService.createProduct(product, "Bearer " + accessToken).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadProductos(); // Recargar la lista de productos
                } else {
                    Log.e("ProductoViewModel", "Error en la respuesta: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al agregar producto", t);
            }
        });
    }

    public void deleteProducto(int id) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("user_token", "");
        apiService.deleteProduct(id, "Bearer " + accessToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadProductos();
                } else {
                    Log.e("ProductoViewModel", "Error en la respuesta: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al eliminar producto", t);
            }
        });
    }

    public void updateProducto(int id, Product updatedProduct) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("user_token", "");
        apiService.updateProduct(id, updatedProduct, "Bearer " + accessToken).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    loadProductos();
                } else {
                    Log.e("ProductoViewModel", "Error en la respuesta: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al actualizar producto", t);
            }
        });
    }

    public void updateStockStatus(int id, Product updatedProduct) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("user_token", "");
        Log.d("ProductoViewModel", "Token usado: " + accessToken);
        apiService.updateProduct(id, updatedProduct, "Bearer " + accessToken).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Log.d("ProductoViewModel", "Estado del stock actualizado correctamente: " + response.body().isInStock());
                    loadProductos();
                } else {
                    Log.e("ProductoViewModel", "Error en la respuesta: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al actualizar estado de stock", t);
            }
        });
    }


}

