package com.example.riccoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.LoginRequest;
import com.example.riccoapp.api.LoginResponse;
import com.example.riccoapp.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView textViewCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.button);
        TextView textViewCrearCuenta = findViewById(R.id.textView6);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
        // Maneja el clic en el TextView para crear cuenta
        textViewCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, RegistroActivity.class); // Cambia a tu actividad de registro
                startActivity(intent);
            }
        });
    }

    private void handleLogin() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (validateInputs(email, password)) {
            LoginRequest request = new LoginRequest(email, password);
            Log.d("LoginActivity", "Request: " + request.getEmail() + ", " + request.getPassword());

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<LoginResponse> call = apiService.login(request);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            String accessToken = loginResponse.getAccess(); // Obtener el token de acceso
                            String refreshToken = loginResponse.getRefresh(); // Obtener el token de refresco
                            String firstName = loginResponse.getUser().getFirstName();
                            String lastName = loginResponse.getUser().getLastName();
                            String rol = loginResponse.getRol();

                            Log.d("LoginActivity", "Rol obtenido: " + rol);
                            Log.d("LoginActivity", "First Name: " + firstName);
                            Log.d("LoginActivity", "Last Name: " + lastName);
                            Log.d("LoginActivity", "Token de acceso recibido: " + accessToken); // Asegúrate de registrar el token
                            Log.d("LoginActivity", "Token de refresco recibido: " + refreshToken); // Registro adicional

                            // Guarda datos en SharedPreferences para AUTENTICAR al usuario logueado
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("user_name", firstName);
                            editor.putString("user_lastname", lastName);
                            editor.putString("user_token", accessToken); // Guardar el token de acceso
                            editor.putString("refresh_token", refreshToken); // Guardar el token de refresco
                            editor.putString("user_rol", rol); // Guardar el rol

                            editor.apply(); // Aplicar los cambios

                            Toast.makeText(loginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Manejo de errores, por ejemplo:
                        Toast.makeText(loginActivity.this, "Login fallido: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("LoginActivity", "Error: " + t.getMessage());
                }
            });
        }
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        if (!isValidEmail(email)) {
            usernameEditText.setError("Ingrese de forma correcta el email");
            isValid = false;
        } else {
            usernameEditText.setError(null); // Limpiar error
        }

        if (!isValidPassword(password)) {
            passwordEditText.setError("La contraseña debe tener al menos 8 caracteres.");
            isValid = false;
        } else {
            passwordEditText.setError(null); // Limpiar error
        }

        return isValid;
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Simplificación de la validación de contraseña
    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}

