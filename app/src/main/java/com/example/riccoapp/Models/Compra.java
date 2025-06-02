package com.example.riccoapp.Models;

import java.util.List;

public class Compra {
    public int id;
    public String fecha;
    public String user_first_name;
    public String user_last_name;
    public List<Detalle> detalles;

    public Compra() {}
}
