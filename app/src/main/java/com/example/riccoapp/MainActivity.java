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
    private TextView userNameToolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar TextView del toolbar
        userNameToolbarText = findViewById(R.id.userNameToolbarText);

        loadUserDataAndSetupUI();
        setupToolbarAndCarousels();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }

        loadUserDataAndSetupUI();
        invalidateOptionsMenu();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", "");
        String rol = sharedPreferences.getString("user_rol", "");
        return !token.isEmpty() && !rol.isEmpty();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadUserDataAndSetupUI() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("user_name", "");
        String lastName = sharedPreferences.getString("user_lastname", "");
        String rol = sharedPreferences.getString("user_rol", "");

        if (userNameToolbarText != null) {
            if ("admin".equals(rol)) {
                userNameToolbarText.setText(firstName + " " + lastName + " - Admin");
            } else {
                userNameToolbarText.setText(firstName + " " + lastName);
            }

            if (rol.isEmpty()) {
                userNameToolbarText.setText("");
            }
        } else {
            Log.e("MainActivity", "userNameToolbarText es null");
        }
    }

    private void setupToolbarAndCarousels() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ImageCarousel carousel = findViewById(R.id.carousel);
        if (carousel != null) {
            carousel.registerLifecycle(getLifecycle());

            list.add(new CarouselItem(R.drawable.carousel1));
            list.add(new CarouselItem(R.drawable.carousel2));
            list.add(new CarouselItem(R.drawable.carousel3));
            carousel.setData(list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String rol = sharedPreferences.getString("user_rol", "");

        menu.findItem(R.id.nav_products).setVisible(!rol.isEmpty());

        MenuItem misComprasItem = menu.findItem(R.id.nav_mis_compras);
        if ("cliente".equals(rol)) {
            misComprasItem.setVisible(true);
            misComprasItem.setEnabled(true);
        } else {
            misComprasItem.setVisible(false);
            misComprasItem.setEnabled(false);
        }

        if ("cliente".equals(rol)) {
            menu.findItem(R.id.nav_registro).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_dashboardadmin).setVisible(false);
        } else if ("admin".equals(rol)) {
            menu.findItem(R.id.nav_registro).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_userprofile).setVisible(false);
            menu.findItem(R.id.nav_dashboardadmin).setVisible(true);
            menu.findItem(R.id.nav_main).setVisible(true);
            menu.findItem(R.id.nav_contacto).setVisible(true);
            menu.findItem(R.id.nav_about).setVisible(true);
            menu.findItem(R.id.nav_logout).setVisible(true);
        } else if (rol.isEmpty()) {
            menu.findItem(R.id.nav_registro).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_userprofile).setVisible(false);
            menu.findItem(R.id.nav_dashboardadmin).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
        }

        return true;
    }

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
        } else if (itemId == R.id.nav_logout) {
            logout();
            return true;
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        if (userNameToolbarText != null) {
            userNameToolbarText.setText("");
        }

        Intent intent = new Intent(MainActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
}
