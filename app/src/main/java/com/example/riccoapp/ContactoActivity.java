package com.example.riccoapp;


import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class ContactoActivity extends AppCompatActivity {
    private EditText nombre, apellido, telefono, email, mensaje;
    private Button contactbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        telefono = findViewById(R.id.telefono);
        email = findViewById(R.id.email);
        mensaje = findViewById(R.id.mensaje);
        contactbutton = findViewById(R.id.contactbutton);

        contactbutton.setOnClickListener(v -> {
            String nombreStr = nombre.getText().toString().trim();
            String apellidoStr = apellido.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String telefonoStr = telefono.getText().toString().trim();
            String mensajeStr = mensaje.getText().toString().trim();

            // Validación de campos vacíos
            if (nombreStr.isEmpty() || apellidoStr.isEmpty() || emailStr.isEmpty() || telefonoStr.isEmpty() || mensajeStr.isEmpty()) {
                Toast.makeText(ContactoActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(ContactoActivity.this, "Gracias por su mensaje, le responderemos en breve.", Toast.LENGTH_SHORT).show();
                nombre.setText("");
                apellido.setText("");
                telefono.setText("");
                email.setText("");
                mensaje.setText("");
            }

            // Validación de correo electrónico
            if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                Toast.makeText(ContactoActivity.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar número de teléfono como cadena de texto
            if (!telefonoStr.matches("\\d+")) {  // Verifica que solo contenga dígitos
                Toast.makeText(ContactoActivity.this, "Número de teléfono no válido", Toast.LENGTH_SHORT).show();
                return;
            }



        });
    }
}
