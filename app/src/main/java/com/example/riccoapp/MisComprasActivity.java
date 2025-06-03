package com.example.riccoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MisComprasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Validar sesión antes de cargar UI
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return; // Detener ejecución para no mostrar UI
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_compras);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");
        String rol = sharedPreferences.getString("user_rol", "");
        return !token.isEmpty() && !rol.isEmpty();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MisComprasActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
}
