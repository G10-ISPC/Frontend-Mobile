package com.example.riccoapp.api;

import com.example.riccoapp.Models.Compra;
import com.example.riccoapp.Models.CompraEstadoRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface CompraService {

    @GET("todas-compras/")
    Call<List<Compra>> getCompras();

    @PATCH("compra/{id}/cambiar-estado/")
    Call<Void> actualizarEstado(
            @Path("id") int id,
            @Body CompraEstadoRequest estadoRequest
    );
}
