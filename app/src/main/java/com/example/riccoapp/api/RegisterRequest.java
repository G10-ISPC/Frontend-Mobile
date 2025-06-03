package com.example.riccoapp.api;

import com.example.riccoapp.api.Direccion;

public class RegisterRequest {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String password2;
    private int telefono;

    public RegisterRequest(String first_name, String last_name, String email, String password, String password2, int telefono) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.telefono = telefono;

    }

    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getFirstName() { return first_name; }
    public void setFirstName(String first_Name) { this.first_name = first_name; }

    public String getLastName() { return last_name; }
    public void setLastName(String lastName) { this.last_name = lastName; }

    public int getTelefono() { return telefono; }
    public void setTelefono(int telefono) { this.telefono = telefono; }

}