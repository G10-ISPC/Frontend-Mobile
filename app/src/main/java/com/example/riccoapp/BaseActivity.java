package com.example.riccoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class BaseActivity extends AppCompatActivity {

    protected TextView userNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_layout);
        setupToolbar(); // Configura la Toolbar en cada Activity que hereda de BaseActivity

        userNameTextView = findViewById(R.id.userNameTextView);
        loadUserName(); // Carga y muestra el nombre del usuario
    }

    // Método para cargar y mostrar el nombre del usuario desde SharedPreferences
    @SuppressLint("SetTextI18n")
    protected void loadUserName() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("user_name", "");
        String lastName = sharedPreferences.getString("user_lastname", "");
        String rol = sharedPreferences.getString("user_rol", "");

        // Mostrar el nombre en el TextView si está presente
        if (userNameTextView != null) {
            if ("admin".equals(rol)) {
                userNameTextView.setText(firstName + " " + lastName + " - Admin");
            } else {
                userNameTextView.setText(firstName + " " + lastName);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserName(); // Recargar el nombre al volver a la actividad
    }

    // Configuración de la Toolbar
    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    // Inflar el menú de navegación
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);

        // Obtener el rol del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String rol = sharedPreferences.getString("user_rol", "");


        // Mostrar productos solo si hay sesión
        menu.findItem(R.id.nav_products).setVisible(!rol.isEmpty());

        // Control de Mis Compras
        MenuItem misComprasItem = menu.findItem(R.id.nav_mis_compras);
        if ("cliente".equals(rol)) {
            misComprasItem.setVisible(true);
            misComprasItem.setEnabled(true);
        } else {
            misComprasItem.setVisible(false);
            misComprasItem.setEnabled(false);
        }



        Log.d("Menu", "Rol del usuario: " + rol); // Para verificar el rol

        // Mostrar/ocultar elementos del menú según el rol
        if ("cliente".equals(rol)) {
            menu.findItem(R.id.nav_registro).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_dashboardadmin).setVisible(false);
        } else if ("admin".equals(rol)) {
            menu.findItem(R.id.nav_registro).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_userprofile).setVisible(true);
            menu.findItem(R.id.nav_dashboardadmin).setVisible(true);
        }

        // Opciones de menú si no hay rol definido
        if (rol.isEmpty()) {
            menu.findItem(R.id.nav_registro).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_userprofile).setVisible(false);
            menu.findItem(R.id.nav_dashboardadmin).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
        }

        return true;
    }

    // Manejo de opciones seleccionadas en el menú (mueve este método aquí desde MainActivity)
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
        } else if (itemId == R.id.nav_dashboardadmin) {
            intent = new Intent(this, DashboardAdminActivity.class);
        } else if (itemId == R.id.nav_mis_compras) {
            intent = new Intent(this, MisComprasActivity.class);
        }else if (itemId == R.id.nav_logout) {
            logout(); // Llama al método de logout
            return true;
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Método de logout
    protected void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpia todos los datos guardados
        editor.apply();

        // Redirige a la pantalla de inicio de sesión
        Intent intent = new Intent(BaseActivity.this, loginActivity.class);
        startActivity(intent);

        finish(); // Finaliza la actividad actual para evitar volver atrás
    }
}
