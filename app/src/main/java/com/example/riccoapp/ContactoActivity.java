package com.example.riccoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ContactoActivity extends AppCompatActivity {

    private TextInputEditText nombreEditText;
    private TextInputEditText apellidoEditText;
    private TextInputEditText telefonoEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText mensajeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        nombreEditText = findViewById(R.id.nombre);
        apellidoEditText = findViewById(R.id.apellido);
        telefonoEditText = findViewById(R.id.telefono);
        emailEditText = findViewById(R.id.email);
        mensajeEditText = findViewById(R.id.mensaje);
        Button contactButton = findViewById(R.id.contactbutton);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    Toast.makeText(ContactoActivity.this, "Gracias por su mensaje, le respondemos en breve", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
            private void clearFields() {
                nombreEditText.setText("");
                apellidoEditText.setText("");
                telefonoEditText.setText("");
                emailEditText.setText("");
                mensajeEditText.setText("");
            }

        });
    }

    private boolean validateInputs() {
        String nombre = nombreEditText.getText().toString().trim();
        String apellido = apellidoEditText.getText().toString().trim();
        String telefono = telefonoEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String mensaje = mensajeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            nombreEditText.setError("Ingrese su nombre");
            return false;
        } else if (!isValidName(nombre)) {
            nombreEditText.setError("El nombre no puede contener números");
            return false;
        }

        if (TextUtils.isEmpty(apellido)) {
            apellidoEditText.setError("Ingrese su apellido");
            return false;
        } else if (!isValidName(apellido)) {
            apellidoEditText.setError("El apellido no puede contener números");
            return false;
        }

        if (TextUtils.isEmpty(telefono)) {
            telefonoEditText.setError("Ingrese su teléfono");
            return false;
        } else if (!isValidPhone(telefono)) {
            telefonoEditText.setError("Teléfono no válido. Debe tener 10 dígitos.");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Ingrese su email");
            return false;
        } else if (!isValidEmail(email)) {
            emailEditText.setError("Email no válido");
            return false;
        }

        if (TextUtils.isEmpty(mensaje)) {
            mensajeEditText.setError("Ingrese su mensaje");
            return false;
        } else if (mensaje.length() < 10) {
            mensajeEditText.setError("El mensaje debe tener al menos 10 caracteres");
            return false;
        }

        return true;
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    private boolean isValidPhone(String phone) {
        // Verifica que el teléfono solo contenga 10 dígitos
        return phone.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

