package com.example.riccoapp.api;

public class LoginResponse {
    private String token;
    private String username;
    private User user;
    private String rol;


    // Constructor
    public LoginResponse(String token, String username, User user, String rol) {
        this.token = token;
        this.username = username;
        this.user = user;
        this.rol= rol;

    }


    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }


    public User Getuser() {
        return user;
    }
    public String getRol() {
        return rol;
    }
}
