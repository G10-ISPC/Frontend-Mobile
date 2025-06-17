package com.example.riccoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ContactoActivity extends BaseActivity {

    private TextInputEditText nombreEditText;
    private TextInputEditText apellidoEditText;
    private TextInputEditText telefonoEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText mensajeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        setupToolbar(); // Barra de navegación
        userNameTextView = findViewById(R.id.userNameTextView); // Asignación de TextView específico de esta Activity
        loadUserName(); // Carga y muestra el nombre del usuario

        nombreEditText = findViewById(R.id.nombre);
        apellidoEditText = findViewById(R.id.apellido);
        telefonoEditText = findViewById(R.id.telefono);
        emailEditText = findViewById(R.id.email);
        mensajeEditText = findViewById(R.id.mensaje);
        Button contactButton = findViewById(R.id.contactbutton);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateInputs()) {
                    Toast.makeText(ContactoActivity.this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nombre = nombreEditText.getText().toString().trim();
                String apellido = apellidoEditText.getText().toString().trim();
                String telefono = telefonoEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String mensaje = mensajeEditText.getText().toString().trim();

                // Armar cuerpo del mensaje
                String body = "Nombre: " + nombre + "\n"
                        + "Apellido: " + apellido + "\n"
                        + "Teléfono: " + telefono + "\n"
                        + "Email: " + email + "\n\n"
                        + "Mensaje:\n" + mensaje;

                // Crear Intent de envío de correo
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822"); // Solo apps de correo
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ricco@gmail.com"}); // Reemplazá por tu mail
                intent.putExtra(Intent.EXTRA_SUBJECT, "Formulario de Contacto Ricco App");
                intent.putExtra(Intent.EXTRA_TEXT, body);

                try {
                    startActivity(Intent.createChooser(intent, "Enviar mensaje con:"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactoActivity.this, "No hay aplicaciones de correo instaladas.", Toast.LENGTH_SHORT).show();
                }
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

