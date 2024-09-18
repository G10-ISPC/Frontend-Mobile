package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establece el layout de la pantalla de precarga
        setContentView(R.layout.activity_splash);

        // Usamos un Handler para retrasar la apertura de la MainActivity por unos segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Luego de 3 segundos, abrir la actividad principal
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                // Finalizar SplashActivity para evitar volver a ella
                finish();
            }
        }, 3000); // Duración de 3 segundos para la pantalla de precarga
    }
}