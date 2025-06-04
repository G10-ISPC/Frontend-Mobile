package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DashboardAdminActivity extends BaseActivity {

    private Button btnListaCompras, btnAdminProductos;
    // TextView para el nombre (igual que en ContactoActivity)
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1) Seteamos el layout que acabamos de editar
        setContentView(R.layout.activity_dashboard_admin);

        // 2) Configuramos la toolbar y el menú (heredado de BaseActivity)
        setupToolbar();

        // 3) Asignamos el TextView para mostrar el nombre (igual que en Contacto)
        userNameTextView = findViewById(R.id.userNameTextView);
        loadUserName();  // Método que carga el nombre desde SharedPreferences

        // 4) Inicializamos los botones del Dashboard
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