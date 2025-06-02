package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardAdminActivity extends AppCompatActivity {

    private Button btnListaCompras, btnAdminProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        btnListaCompras = findViewById(R.id.btnListaCompras);
        btnAdminProductos = findViewById(R.id.btnAdminProductos);

        btnListaCompras.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdminActivity.this, ListaDeComprasActivity.class);
            startActivity(intent);
        });

        btnAdminProductos.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdminActivity.this, AdminActivity.class);
            startActivity(intent);
        });
    }
}

