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


// comentario para borrar despues
public interface ApiService {
    @POST("login/")
    Call<LoginResponse> login(@Body LoginRequest loginRequest); // Asegúrate de tener la clase LoginRequest

    @POST("registro/")
    Call<User> register(@Body RegisterRequest registerRequest);

    // Obtener perfil de usuario
    @GET("perfilUsuario/")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String token);

    // Actualizar perfil de usuario
    @PUT("perfilUsuario/")
    Call<UserProfileResponse> updateUserProfile(@Header("Authorization") String token, @Body UserProfileRequest userProfileRequest);

    // Eliminar cuenta de usuario
    @DELETE("perfilUsuario/")
    Call<Void> deleteUserProfile(@Header("Authorization") String token);

    @GET("producto/")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

    @POST("producto/")
    Call<Product> createProduct(@Body Product product, @Header("Authorization") String token);

    @DELETE("producto/{id}/")
    Call<Void> deleteProduct(@Path("id") int id, @Header("Authorization") String token);

    @PUT("producto/{id}/")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product, @Header("Authorization") String token);

    // Se agrega la opcion de stock
    @PUT("producto/{id}/stock")
    Call<Product> updateStock(@Path("id") int id, @Body Product product, @Header("Authorization") String token);

    @GET("mis-compras/")
    Call<List<Compra>> getMisCompras();

    @POST("cancelar-compra/{id_compra}/")
    Call<Void> cancelarCompra(@Path("id_compra") int idCompra);
}
