package com.example.riccoapp.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Compra {

    @SerializedName("id_compra")
    public int id_compra;

    @SerializedName("fecha")
    public String fecha;

    @SerializedName("user_first_name")
    public String user_first_name;

    @SerializedName("user_last_name")
    public String user_last_name;

    @SerializedName("detalles")
    public List<Detalle> detalles;

    @SerializedName("estado")
    public String estado;

    public Compra() {}
}
