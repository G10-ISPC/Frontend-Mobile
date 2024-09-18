package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<CarouselItem> list = new ArrayList<>();
    private List<CarouselItem> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Verificación opcional para evitar posibles errores nulos
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Configurar los carruseles
        ImageCarousel carousel = findViewById(R.id.carousel);
        ImageCarousel carousel2 = findViewById(R.id.carousel2);

        if (carousel != null && carousel2 != null) {
            carousel.registerLifecycle(getLifecycle());
            carousel2.registerLifecycle(getLifecycle());

            // Configurar datos para el primer carrusel
            list.add(new CarouselItem(R.drawable.carousel1));
            list.add(new CarouselItem(R.drawable.carousel2));
            list.add(new CarouselItem(R.drawable.carousel3));

            carousel.setData(list);

            // Configurar datos para el segundo carrusel
            list2.add(new CarouselItem(R.drawable.bur2, "Burgers Jobs"));
            list2.add(new CarouselItem(R.drawable.bur1, "Bill Gates"));
            list2.add(new CarouselItem(R.drawable.bur5, "♡ Doble Love ♡"));

            carousel2.setData(list2);
        }
    }

    // Inflar el menú de navegación
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    // Manejar la selección de los ítems del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_main) {
            intent = new Intent(this, MainActivity.class);
        } else if (itemId == R.id.nav_registro) {
            intent = new Intent(this, RegistroActivity.class);
        } else if (itemId == R.id.nav_login) {
            intent = new Intent(this, loginActivity.class);
        } else if (itemId == R.id.nav_contacto) {
            intent = new Intent(this, ContactoActivity.class);
        } else if (itemId == R.id.nav_products) {
            intent = new Intent(this, ProductsActivity.class);
        } else if (itemId == R.id.nav_userprofile) {
            intent = new Intent(this, UserProfileActivity.class);
        } else if (itemId == R.id.nav_about) {
            intent = new Intent(this, AboutActivity.class);
        }


        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
