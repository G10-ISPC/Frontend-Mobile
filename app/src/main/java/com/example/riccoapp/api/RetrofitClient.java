package com.example.riccoapp.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8000/api/"; // Asegúrate de que esta URL sea correcta
    private static Retrofit retrofit = null;

    // Método para obtener Retrofit sin token
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Gson para convertir JSON a objetos
                    .build();
        }
        return retrofit;
    }

    // Método para obtener Retrofit con OkHttpClient y token
    public static Retrofit getRetrofitInstanceWithToken(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token)) // Tu interceptor si es necesario
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // Usamos el cliente con el token
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
