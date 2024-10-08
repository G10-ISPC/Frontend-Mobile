package com.example.riccoapp.api;

public class RegisterRequest {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String password2;
    private Direccion direccion;
    private int telefono;

    public static class Direccion {

        private String street;
        private int number;

        public Direccion(String street, int number) {

            this.street = street;
            this.number = number;
        }

        public String getStreet() {
            return street;
        }

        public int getNumber() {
            return number;
        }

    }

    public RegisterRequest(String first_name, String last_name, String email, String password, String password2, int telefono, Direccion direccion) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }

    public int getTelefono() {
        return telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

}
