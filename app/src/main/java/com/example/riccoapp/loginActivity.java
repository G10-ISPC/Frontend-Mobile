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

import org.json.JSONArray;
import org.json.JSONObject;

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
        textViewCrearCuenta = findViewById(R.id.textView6);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        textViewCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, RegistroActivity.class);
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
                            String accessToken = loginResponse.getAccess();
                            String refreshToken = loginResponse.getRefresh();
                            String firstName = loginResponse.getUser().getFirstName();
                            String lastName = loginResponse.getUser().getLastName();
                            String rol = loginResponse.getRol();

                            Log.d("LoginActivity", "Rol obtenido: " + rol);
                            Log.d("LoginActivity", "First Name: " + firstName);
                            Log.d("LoginActivity", "Last Name: " + lastName);
                            Log.d("LoginActivity", "Token de acceso recibido: " + accessToken);
                            Log.d("LoginActivity", "Token de refresco recibido: " + refreshToken);

                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("user_name", firstName);
                            editor.putString("user_lastname", lastName);
                            editor.putString("user_token", accessToken);
                            editor.putString("refresh_token", refreshToken);
                            editor.putString("user_rol", rol);

                            editor.apply();

                            Toast.makeText(loginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("loginActivity", "Error Body: " + errorBody);
                            JSONObject jsonObject = new JSONObject(errorBody);

                            //extrae el mensaje del error
                            JSONObject errorDetails = jsonObject.optJSONObject("error");


                            if (errorDetails != null) {
                                JSONArray nonFieldErrors = errorDetails.optJSONArray("non_field_errors");
                                if (nonFieldErrors != null && nonFieldErrors.length() > 0) {
                                    String errorMessage = nonFieldErrors.getString(0);
                                    Toast.makeText(loginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(loginActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(loginActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("LoginActivity", "Error al procesar la respuesta: " + e.getMessage());
                            Toast.makeText(loginActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("LoginActivity", "Error: " + t.getMessage());
                    Toast.makeText(loginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
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

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}
