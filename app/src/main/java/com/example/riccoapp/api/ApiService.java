package com.example.riccoapp.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiService {
    @POST("login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest); // Asegúrate de tener la clase LoginRequest

    @POST("registro/")
    Call<User> register(@Body RegisterRequest registerRequest);

    // Obtener perfil de usuario
    @GET("profile/")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String token);

    // Actualizar perfil de usuario
    @PUT("profile/")
    Call<UserProfileResponse> updateUserProfile(@Header("Authorization") String token, @Body UserProfileRequest userProfileRequest);

    // Eliminar cuenta de usuario
    @DELETE("api/profile/")  // Asegúrate de que esta sea la ruta correcta
    Call<Void> deleteUserProfile(@Header("Authorization") String token);

    //Mostrar Productos
    @GET("productos/")  // Asegúrate de que este sea el endpoint correcto
    Call<List<Product>> getProducts(); // Retorna una lista de objetos Product


}
