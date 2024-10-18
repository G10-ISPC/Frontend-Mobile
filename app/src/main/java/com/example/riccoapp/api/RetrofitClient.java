package com.example.riccoapp.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8000/api/"; // Cambia esto por la URL de tu API
    private static Retrofit retrofit = null;

    // Método para obtener Retrofit SIN token
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Método para obtener Retrofit CON OkHttpClient
    public static Retrofit getRetrofitInstance(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // Agregar el cliente
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}