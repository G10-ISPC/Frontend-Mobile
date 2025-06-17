package com.example.riccoapp.api;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String rol;
    private boolean is_staff; // Agregado para manejar el nuevo campo

    // Constructor
    public User(int id, String first_name, String last_name, String rol, boolean is_staff) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.rol = rol;
        this.is_staff = is_staff; // Inicializa el nuevo campo
    }

    // Getters
    public int getId() { return id; }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getRol() {
        return rol;
    }

    public boolean isStaff() {
        return is_staff; // Método para obtener el nuevo campo
    }
}