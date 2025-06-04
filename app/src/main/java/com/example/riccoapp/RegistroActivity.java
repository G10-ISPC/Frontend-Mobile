package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.RegisterRequest;
import com.example.riccoapp.api.RetrofitClient;
import com.example.riccoapp.api.User;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends BaseActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, password2EditText, phoneEditText;
    private Button registerButton;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"); // Solo letras y espacios

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setupToolbar(); // Barra de navegación

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        password2EditText = findViewById(R.id.password2EditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.btnRegister);

        TextView tvLogin = findViewById(R.id.tvLogin);

        // Redirigir a login al hacer click en texto
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, loginActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(v -> {
            if (!validateInputs()) return;

            // Crear request para registro
            RegisterRequest registerRequest = new RegisterRequest(
                    firstNameEditText.getText().toString().trim(),
                    lastNameEditText.getText().toString().trim(),
                    emailEditText.getText().toString().trim(),
                    passwordEditText.getText().toString(),
                    password2EditText.getText().toString(),
                    Integer.parseInt(phoneEditText.getText().toString().trim())
            );

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<User> call = apiService.register(registerRequest);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        clearInputs();
                        Intent intent = new Intent(RegistroActivity.this, loginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            String errorResponse = response.errorBody().string();
                            Toast.makeText(RegistroActivity.this, "Error: " + errorResponse, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(RegistroActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegistroActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Método para validar todos los campos y mostrar errores
    private boolean validateInputs() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String password2 = password2EditText.getText().toString();
        String telefonoStr = phoneEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                password2.isEmpty() || telefonoStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!NAME_PATTERN.matcher(firstName).matches()) {
            Toast.makeText(this, "Nombre solo debe contener letras", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!NAME_PATTERN.matcher(lastName).matches()) {
            Toast.makeText(this, "Apellido solo debe contener letras", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (firstName.length() < 3) {
            Toast.makeText(this, "El nombre debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (lastName.length() < 3) {
            Toast.makeText(this, "El apellido debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.matches("\\d+")) {
            Toast.makeText(this, "La contraseña no puede ser completamente numérica.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[!@#$%^&*()\\-_=+{};:,<.>].*")) {
            Toast.makeText(this, "La contraseña debe contener al menos un carácter especial", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.toLowerCase().contains(firstName.toLowerCase()) ||
                password.toLowerCase().contains(lastName.toLowerCase()) ||
                password.toLowerCase().contains(email.toLowerCase())) {
            Toast.makeText(this, "La contraseña no debe contener tu nombre, apellido o correo.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(password2)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int telefono = Integer.parseInt(telefonoStr);
            if (telefono < 0) {
                Toast.makeText(this, "Número de teléfono no puede ser negativo", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Número de teléfono no válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Limpia los campos después de un registro exitoso
    private void clearInputs() {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        password2EditText.setText("");
        phoneEditText.setText("");
    }
}
