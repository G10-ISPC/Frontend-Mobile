package com.example.riccoapp.api;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String telefono;
    private Direccion direccion; // Asegúrate de que esta clase esté definida

    public UserProfileResponse(String email, String firstName, String lastName, String telefono, Direccion direccion) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTelefono() { return telefono; }
    public Direccion getDireccion() { return direccion; }
}