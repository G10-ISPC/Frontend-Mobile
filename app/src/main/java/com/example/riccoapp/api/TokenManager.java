package com.example.riccoapp.api;

import android.content.SharedPreferences;

public class TokenManager {
    private final SharedPreferences sharedPreferences;

    public TokenManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String obtenerToken() {
        return sharedPreferences.getString("user_token", null); // Devuelve null si no se encuentra
    }
}
