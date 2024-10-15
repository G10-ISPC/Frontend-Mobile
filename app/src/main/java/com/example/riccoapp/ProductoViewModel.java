package com.example.riccoapp;


import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.Product;
import com.example.riccoapp.api.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
                    Toast.makeText(getApplication(), "Error al cargar productos: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al cargar productos", t);
                Toast.makeText(getApplication(), "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addProducto(Product product) {
        Log.d("ProductoViewModel", "Producto a agregar: " + product.toString());
        apiService.createProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadProductos();
                } else {
                    Log.e("ProductoViewModel", "Error en la respuesta: " + response.code() + " - " + response.message());
                    Toast.makeText(getApplication(), "Error al agregar producto: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ProductoViewModel", "Error al agregar producto", t);
                Toast.makeText(getApplication(), "Error al agregar producto", Toast.LENGTH_SHORT).show();
            }
        });
    }


}




