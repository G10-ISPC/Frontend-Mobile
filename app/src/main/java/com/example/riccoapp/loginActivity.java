package com.example.riccoapp;
import android.content.SharedPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.LoginRequest;
import com.example.riccoapp.api.LoginResponse;
import com.example.riccoapp.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (validateInputs(email, password)) {
            // Crear el objeto de solicitud
            LoginRequest request = new LoginRequest(email, password);

            // Log para depurar
            Log.d("LoginActivity", "Request: " + request.getEmail() + ", " + request.getPassword());

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

            // Hacer la llamada a la API
            Call<LoginResponse> call = apiService.login(request);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        // Manejar respuesta exitosa
                        LoginResponse loginResponse = response.body(); // Obtener la respuesta del cuerpo
                        String token = loginResponse.getToken(); // Obtener el token
                        String firstName = loginResponse.Getuser().getFirstName(); // Obtener el primer nombre
                        String lastName = loginResponse.Getuser().getLastName(); // Obtener el apellido

                        Log.d("LoginActivity", "First Name: " + firstName);
                        Log.d("LoginActivity", "Last Name: " + lastName);


                        // Guardar en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_name", firstName);
                        editor.putString("user_lastname", lastName);
                        editor.putString("user_token", token); // Opcional: guardar el token
                        editor.clear();
                        editor.apply();

                        Toast.makeText(loginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Manejar error en la respuesta
                        Toast.makeText(loginActivity.this, "Error en el login: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    // Manejar error de conexión
                    Toast.makeText(loginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Error: ", t);
                }
            });
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
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
            passwordEditText.setError("La contraseña debe tener al menos 8 caracteres, " +
                    "incluir una letra mayúscula, una letra minúscula, un número y un carácter especial.");
            isValid = false;
        } else {
            passwordEditText.setError(null); // Limpiar error
        }

        return isValid;
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        // Expresión regular para verificar complejidad
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}