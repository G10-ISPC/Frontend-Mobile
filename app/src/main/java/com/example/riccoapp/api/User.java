package com.example.riccoapp.api;

public class User {
    private String first_name; // El primer nombre
    private String last_name; // El apellido

    // Constructor
    public User(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    // Getters
    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }
}