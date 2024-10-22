package com.example.riccoapp;

import android.util.Log;
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
import android.widget.TextView;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private List<CarouselItem> list = new ArrayList<>();
    private List<CarouselItem> list2 = new ArrayList<>();
    private TextView userNameTextView; // 1. Declarar el TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // 2. Inicializar el TextView
        userNameTextView = findViewById(R.id.userNameTextView);



        // Recuperar datos del SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("user_name", "");
        String lastName = sharedPreferences.getString("user_lastname", "");
        String rol = sharedPreferences.getString("user_rol", "");
        Log.d("MainActivity", "First Name: " + firstName);
        Log.d("MainActivity", "Last Name: " + lastName);
        Log.d("MainActivity", "Rol: " + rol); // log para verificar el rol

        // Al recuperar el token en la MainActivity
        Log.d("SharedPreferences", "Token recuperado: " + sharedPreferences.getString("user_token", "No encontrado"));


        // 3. Mostrar el mensaje de bienvenida
        if ("admin".equals(rol)) {
            userNameTextView.setText(firstName + " " + lastName + " - Admin"); // Mostrar nombre y rol
        } else {
            userNameTextView.setText(firstName + " " + lastName); // Solo mostrar nombre para clientes
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume called"); // Verifica si se llama

        // Recupera datos del SharedPreferences al volver a la MainActivity
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("user_name", "");
        String lastName = sharedPreferences.getString("user_lastname", "");
        String rol = sharedPreferences.getString("user_rol", "");
        Log.d("MainActivity", "Nombre: " + firstName + ", Apellido: " + lastName + ", Rol: " + rol);

        // Mostrar el mensaje de bienvenida
        if ("admin".equals(rol)) {
            userNameTextView.setText(firstName + " " + lastName + " - Admin");
        } else {
            userNameTextView.setText(firstName + " " + lastName);
        }

        // Limpiar datos si el rol está vacío
        if (rol.isEmpty()) {
            userNameTextView.setText(""); // Limpia el TextView si no hay datos
        }

        // Invalidar el menú para que se vuelva a inflar
        invalidateOptionsMenu();
    }

    // Inflar el menú de navegación
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);

        // Obtener el rol del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String rol = sharedPreferences.getString("user_rol", "");
        Log.d("Menu", "Rol del usuario: " + rol); // Verifica que imprima el rol correcto

        // Mostrar/ocultar elementos del menú según el rol
        if ("cliente".equals(rol)) {
            // Ocultar las opciones que no deberían ver los clientes
            menu.findItem(R.id.nav_registro).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_admin_productos).setVisible(false);

        } else if ("admin".equals(rol)) {
            // Mostrar todas las opciones para admin
            menu.findItem(R.id.nav_registro).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_userprofile).setVisible(true);
            menu.findItem(R.id.nav_admin_productos).setVisible(true);
            menu.findItem(R.id.nav_main).setVisible(true);
            menu.findItem(R.id.nav_contacto).setVisible(true);
            menu.findItem(R.id.nav_about).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(true);
        }
        // Siempre mostrar Login y Registro si no hay rol
        if (rol.isEmpty()) {
            menu.findItem(R.id.nav_registro).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_userprofile).setVisible(false); // Oculta perfil de usuario
            menu.findItem(R.id.nav_admin_productos).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
        }

        return true;
    }
    //-----------------
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
        } else if (itemId == R.id.nav_admin_productos) {
            intent = new Intent(this, AdminActivity.class);
        } else if (itemId == R.id.nav_logout) {
            logout(); // Llama al método de logout
            return true; // Indica que se manejó la opción
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    //-------------------
    private void logout() {
        // Limpiar SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpia todos los datos guardados
        editor.apply();

        // Limpiar el TextView
        userNameTextView.setText(" ");

        // Redirigir a la pantalla de inicio de sesión
        Intent intent = new Intent(MainActivity.this, loginActivity.class);
        startActivity(intent);

        // Finalizar la actividad actual para evitar volver atrás
        finish();
    }
}