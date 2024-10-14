package com.example.riccoapp.api;

public class LoginResponse {
    private String access; // Token de acceso
    private String refresh; // Token de refresco
    private String rol; // Rol del usuario
    private User user; // Objeto User

    // Getters
    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getRol() {
        return rol;
    }

    public User getUser() {
        return user;
    }
}