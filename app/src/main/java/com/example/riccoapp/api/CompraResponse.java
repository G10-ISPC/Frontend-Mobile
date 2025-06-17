package com.example.riccoapp.api;


import com.google.gson.annotations.SerializedName;

public class CompraResponse {
    @SerializedName("initPoint")
    private String initPoint; // URL de pago

    public String getInitPoint() {
        return initPoint;
    }
}