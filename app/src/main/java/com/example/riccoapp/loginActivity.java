package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

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
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (validateInputs(username, password)) {
            // Start HomeActivity after successful login
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish(); // Optionally close the login activity
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            // Handle failed login

        }
    }

    private boolean validateInputs(String username, String password) {
        boolean isValid = true;

        if (!isValidEmail(username)) {
            usernameEditText.setError("Ingrese de forma correcta el email");
            isValid = false;
        } else {
            usernameEditText.setError(null); // Clear the error
        }

        if (!isValidPassword(password)) {
            passwordEditText.setError("\"La contraseña debe tener al menos 8 caracteres, \" +\n" +
                    "            \"incluir una letra mayúscula, una letra minúscula, un número y un carácter especial.\");");
            isValid = false;
        } else {
            passwordEditText.setError(null); // Clear the error
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