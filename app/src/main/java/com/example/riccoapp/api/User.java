package com.example.riccoapp.api;

public class User {
    private String first_name;
    private String last_name;
    private String rol;

    // Constructor
    public User(String first_name, String last_name, String rol) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.rol = rol;
    }

    // Getters
    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getRol() {
        return rol;
    }
}
