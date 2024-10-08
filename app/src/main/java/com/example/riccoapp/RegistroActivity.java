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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, password2EditText, phoneEditText, streetEditText, numberEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        password2EditText = findViewById(R.id.password2EditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        streetEditText = findViewById(R.id.streetEditText);
        numberEditText = findViewById(R.id.numberEditText);
        registerButton = findViewById(R.id.btnRegister);

        TextView tvLogin = findViewById(R.id.tvLogin);

        // Redirigir a la actividad de login al hacer clic en el TextView
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, loginActivity.class);
            startActivity(intent);
            finish();  // cierra la actividad de registro para que no vuelva atrás
        });

        registerButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String password2 = password2EditText.getText().toString();
            int telefono = Integer.parseInt(phoneEditText.getText().toString());
            String street = streetEditText.getText().toString();
            int number = Integer.parseInt(numberEditText.getText().toString());

            if (!password.equals(password2)) {
                Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest.Direccion direccion = new RegisterRequest.Direccion(street, number);

            RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, email, password, password2, telefono, direccion);

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<User> call = apiService.register(registerRequest);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        // Limpiar los campos
                        firstNameEditText.setText("");
                        lastNameEditText.setText("");
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        password2EditText.setText("");
                        phoneEditText.setText("");
                        streetEditText.setText("");
                        numberEditText.setText("");

                        // Redirigir al login
                        Intent intent = new Intent(RegistroActivity.this, loginActivity.class);
                        startActivity(intent);

                        // Finalizar la actividad actual para que el usuario no pueda volver al registro
                        finish();

                    } else {
                        if (response.code() == 400) {
                            Toast.makeText(RegistroActivity.this, "Datos inválidos", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(RegistroActivity.this, "Error del servidor", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistroActivity.this, "Error desconocido", Toast.LENGTH_SHORT).show();
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

}