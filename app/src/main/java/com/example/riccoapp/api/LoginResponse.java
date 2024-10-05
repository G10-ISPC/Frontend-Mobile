package com.example.riccoapp.api;

public class LoginResponse {
    private String token; // Puedes agregar más campos según tu API
    private String username;
    private User user;
    //private String first_name; // El primer nombre
    //private String last_name; // El apellido

    // Constructor
    public LoginResponse(String token, String username, User user) {
        this.token = token;
        this.username = username;
        this.user = user;

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
}
