package com.example.riccoapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCalle, etNumero, etEmail, etPassword;
    private Button btnEditar, btnEliminar, btnVolverASuscribirse;
    private boolean isEditing = false; // Flag para indicar si se está en modo de edición

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Referenciar los campos
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCalle = findViewById(R.id.etCalle);
        etNumero = findViewById(R.id.etNumero);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolverASuscribirse = findViewById(R.id.btnVolverASuscribirse);

        // Configurar datos predeterminados
        etNombre.setText("Roberto");
        etApellido.setText("Diaz");
        etCalle.setText("Dolores");
        etNumero.setText("257");
        etEmail.setText("diaz@gmail.com");
        etPassword.setText("Diaz123456"); // Asegúrate de no mostrar la contraseña real en la interfaz

        // Botón para habilitar/deshabilitar la edición
        btnEditar.setOnClickListener(v -> {
            if (!isEditing) {
                habilitarCampos(true);
                btnEditar.setText("Guardar");
            } else {
                if (validarCampos()) {
                    Toast.makeText(UserProfileActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    habilitarCampos(false);
                    btnEditar.setText("Editar");
                }
            }
            isEditing = !isEditing;
        });

        // Botón para eliminar la cuenta
        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacion());

        // Botón para volver a suscribirse
        btnVolverASuscribirse.setOnClickListener(v -> {
            // Lógica para volver a suscribirse
            Toast.makeText(UserProfileActivity.this, "Redirigiendo a la suscripción", Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar el código para redirigir al usuario a la página de suscripción.
        });
    }

    // Habilitar/deshabilitar campos
    private void habilitarCampos(boolean habilitar) {
        etNombre.setEnabled(habilitar);
        etApellido.setEnabled(habilitar);
        etCalle.setEnabled(habilitar);
        etNumero.setEnabled(habilitar);
        etEmail.setEnabled(habilitar);
        etPassword.setEnabled(habilitar);
    }

    // Validar campos
    private boolean validarCampos() {
        if (TextUtils.isEmpty(etNombre.getText())) {
            etNombre.setError("El nombre es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(etApellido.getText())) {
            etApellido.setError("El apellido es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(etCalle.getText())) {
            etCalle.setError("La calle es obligatoria");
            return false;
        }
        if (TextUtils.isEmpty(etNumero.getText())) {
            etNumero.setError("El número es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("El correo es obligatorio");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()) {
            etEmail.setError("El correo electrónico no es válido");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText()) || etPassword.getText().length() < 8) {
            etPassword.setError("La contraseña debe tener al menos 8 caracteres");
            return false;
        }
        return true;
    }

    // Diálogo de confirmación para eliminar cuenta
    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cuenta")
                .setMessage("¿Está seguro de que desea eliminar su cuenta?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Lógica para eliminar la cuenta
                    Toast.makeText(UserProfileActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                    // Ocultar los campos y mostrar el botón de volver a suscribirse
                    habilitarCampos(false);
                    btnEditar.setVisibility(View.GONE);
                    btnEliminar.setVisibility(View.GONE);
                    btnVolverASuscribirse.setVisibility(View.VISIBLE);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Cerrar el diálogo
                    dialog.dismiss();
                })
                .create()
                .show();
    }
}