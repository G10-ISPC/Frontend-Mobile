package com.example.riccoapp.api;

import com.example.riccoapp.Models.Compra;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CompraService {
    @GET("todas-compras/")
    Call<List<Compra>> getCompras();
}
