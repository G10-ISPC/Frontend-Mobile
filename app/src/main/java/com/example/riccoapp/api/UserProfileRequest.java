package com.example.riccoapp.api;

import com.google.gson.annotations.SerializedName;

public class UserProfileRequest {
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String telefono;
    private Direccion direccion; // Asegúrate de que esta clase esté definida

    public UserProfileRequest(String email, String firstName, String lastName, String telefono) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
}