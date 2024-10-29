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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.riccoapp.api.ApiService;
import com.example.riccoapp.api.Direccion;
import com.example.riccoapp.api.RetrofitClient;
import com.example.riccoapp.api.UserProfileRequest;
import com.example.riccoapp.api.UserProfileResponse;
import com.example.riccoapp.api.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    // Variables de vista
    private EditText etNombre, etApellido, etCalle, etNumero, etEmail, etTelefono;
    private Button btnEditar, btnEliminar, btnVolverASuscribirse;
    private TextView userNameTextView;
    private boolean isEditing = false;

    // Inicializar el API Service

    private ApiService apiService;
    private UserProfileResponse currentUser;
    private TokenManager tokenManager; // Agregar TokenManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Inicializar Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Inicializar SharedPreferences y TokenManager
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        tokenManager = new TokenManager(sharedPreferences);

        // Referenciar los campos
        inicializarVistas();

        // Cargar datos del usuario
        cargarUsuario();

        // Configurar botones
        configurarBotones();
    }

    private void inicializarVistas() {
        userNameTextView = findViewById(R.id.userNameTextView);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCalle = findViewById(R.id.etCalle);
        etNumero = findViewById(R.id.etNumero);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

    }

    private void configurarBotones() {
        btnEditar.setOnClickListener(v -> {
            if (!isEditing) {
                habilitarCampos(true);
                btnEditar.setText(R.string.guardar);
            } else {
                if (validarCampos()) {
                    actualizarUsuario();
                }
            }
            isEditing = !isEditing;
        });

        btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacion());

    }

    private void cargarUsuario() {
        String token = tokenManager.obtenerToken(); // Usar TokenManager para obtener el token
        Log.d("UserProfileActivity", "Token obtenido: " + token);

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agregar prefijo Bearer
        token = "Bearer " + token;

        // Llamar a la API para obtener el perfil del usuario
        Call<UserProfileResponse> call = apiService.getUserProfile(token);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserProfileResponse> call, @NonNull Response<UserProfileResponse> response) {
                Log.d("UserProfileActivity", "Código de respuesta: " + response.code() + ", Mensaje: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    mostrarDatosUsuario(currentUser); // Mostrar datos del usuario en la UI
                } else {
                    manejarErroresDeCarga(response); // Manejar errores de carga
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfileResponse> call, @NonNull Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatosUsuario(UserProfileResponse user) {

        etEmail.setText(user.getEmail());
        etNombre.setText(user.getFirstName());
        etApellido.setText(user.getLastName());
        etTelefono.setText(user.getTelefono());

        if (user.getDireccion() != null) {
            etCalle.setText(user.getDireccion().getCalle());
            etNumero.setText(String.valueOf(user.getDireccion().getNumero()));
        } else {
            etCalle.setText("");
            etNumero.setText("");
        }
        habilitarCampos(false); // Deshabilitar campos al cargar datos
    }

    private void manejarErroresDeCarga(Response<UserProfileResponse> response) {
        switch (response.code()) {
            case 401:
                Toast.makeText(UserProfileActivity.this, "Token inválido o sesión expirada", Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(UserProfileActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(UserProfileActivity.this, "Error al cargar datos: " + response.message(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void actualizarUsuario() {
        String token = tokenManager.obtenerToken();
        token = "Bearer " + token; // Agregar el prefijo "Bearer" al token

        // Validar que todos los campos estén completos como ya lo haces
        if (etNombre.getText().toString().isEmpty() ||
                etApellido.getText().toString().isEmpty() ||
                etEmail.getText().toString().isEmpty() ||
                etTelefono.getText().toString().isEmpty() ||
                etCalle.getText().toString().isEmpty() ||
                etNumero.getText().toString().isEmpty()) {

            Toast.makeText(UserProfileActivity.this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear la dirección actualizada
        Direccion updatedDireccion = new Direccion(etCalle.getText().toString(), Integer.parseInt(etNumero.getText().toString()));

        // Crear el usuario actualizado
        UserProfileRequest updatedUser = new UserProfileRequest(
                etEmail.getText().toString(),
                etNombre.getText().toString(),
                etApellido.getText().toString(),
                etTelefono.getText().toString(),
                updatedDireccion
        );

        // Hacer la llamada a la API para actualizar el perfil del usuario
        Call<UserProfileResponse> call = apiService.updateUserProfile(token, updatedUser);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserProfileResponse> call, @NonNull Response<UserProfileResponse> response) {
                if (response.isSuccessful()) {
                    // Guardar los nuevos datos en SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                    editor.putString("user_name", etNombre.getText().toString());
                    editor.putString("user_lastname", etApellido.getText().toString());
                    //editor.putString("user_rol", ""); // Actualiza el rol si es necesario
                    editor.apply();
                    Log.d("UserProfileActivity", "Datos guardados: " + etNombre.getText().toString() + " " + etApellido.getText().toString());

                    Toast.makeText(UserProfileActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                    habilitarCampos(false);  // Deshabilitar los campos después de actualizar
                    btnEditar.setText(R.string.editar);  // Cambiar el texto del botón a "Editar"
                    isEditing = false;  // Cambiar el estado de edición
                } else {
                    Toast.makeText(UserProfileActivity.this, "Error al actualizar el perfil: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserProfileResponse> call, @NonNull Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarCuenta() {
        String token = tokenManager.obtenerToken();
        token = "Bearer " + token; // Agregar el prefijo "Bearer" al token
        if (token != null) {
            apiService.deleteUserProfile(token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(UserProfileActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                        redirigirAlInicio();  // Llamamos a este nuevo método
                    } else {
                        Toast.makeText(UserProfileActivity.this, "Error al eliminar la cuenta: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(UserProfileActivity.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirigirAlInicio() {
        // Limpiar las preferencias compartidas para borrar cualquier dato almacenado
        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        // Redirigir a la pantalla principal o de registro
        Intent intent = new Intent(UserProfileActivity.this, RegistroActivity.class);  // Cambia a MainActivity si quieres que vaya a la pantalla principal
        startActivity(intent);
        finish();  // Finaliza esta actividad para que no pueda volver con el botón "Atrás"
    }
    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cuenta")
                .setMessage("¿Está seguro de que desea eliminar su cuenta?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> eliminarCuenta())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private boolean validarCampos() {
        if (TextUtils.isEmpty(etNombre.getText().toString())) {
            etNombre.setError("El nombre es requerido");
            return false;
        }
        if (!etNombre.getText().toString().matches("[a-zA-Z]+")) {
            etNombre.setError("El nombre solo debe contener letras");
            return false;
        }

        if (TextUtils.isEmpty(etApellido.getText().toString())) {
            etApellido.setError("El apellido es requerido");
            return false;
        }
        if (!etApellido.getText().toString().matches("[a-zA-Z]+")) {
            etApellido.setError("El apellido solo debe contener letras");
            return false;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString()) || !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Se requiere un correo electrónico válido");
            return false;
        }

        if (TextUtils.isEmpty(etTelefono.getText().toString())) {
            etTelefono.setError("El teléfono es requerido");
            return false;
        }

        if (TextUtils.isEmpty(etCalle.getText().toString())) {
            etCalle.setError("La calle es requerida");
            return false;
        }
        if (!etCalle.getText().toString().matches("[a-zA-Z\\s]+")) { // Solo letras y espacios
            etCalle.setError("La calle solo debe contener letras y espacios");
            return false;
        }

        if (TextUtils.isEmpty(etNumero.getText().toString()) || !etNumero.getText().toString().matches("\\d+")) { // Solo números
            etNumero.setError("El número debe ser un valor numérico");
            return false;
        }

        return true;
    }



    private void habilitarCampos(boolean habilitar) {
        etNombre.setEnabled(habilitar);
        etApellido.setEnabled(habilitar);
        etCalle.setEnabled(habilitar);
        etNumero.setEnabled(habilitar);
        etEmail.setEnabled(false);
        etTelefono.setEnabled(habilitar);
    }
}


