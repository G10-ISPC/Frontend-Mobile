package com.example.riccoapp.api;

import okhttp3.Interceptor; // Asegúrate de que esto esté presente
import okhttp3.Request; // Asegúrate de que esto esté presente
import okhttp3.Response; // Asegúrate de que esto esté presente
import java.io.IOException; // Asegúrate de que esto esté presente

public class AuthInterceptor implements Interceptor {

    private String authToken;

    public AuthInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + authToken); // Agregar el token

        Request request = builder.build();
        return chain.proceed(request);
    }
}