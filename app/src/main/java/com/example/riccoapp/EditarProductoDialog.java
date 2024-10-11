package com.example.riccoapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditarProductoDialog extends DialogFragment {

    private EditText editNombre, editDescripcion, editPrecio;
    private Button btnGuardarCambios;
    private Producto producto;
    private int posicion;  // Nueva variable para almacenar la posición
    private OnProductoEditadoListener listener;

    // Interfaz para comunicar el diálogo con la actividad principal
    public interface OnProductoEditadoListener {
        void onProductoEditado(Producto productoEditado, int posicion);  // Modificado para incluir la posición
    }

    // Constructor que recibe el producto y su posición
    public EditarProductoDialog(Producto producto, int posicion) {
        this.producto = producto;  // Pasamos el producto a editar
        this.posicion = posicion;  // Pasamos la posición del producto
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflar el layout personalizado
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_dialogo_editar_producto, null);

        // Referencias a las vistas del diálogo
        editNombre = view.findViewById(R.id.editNombreProducto);
        editDescripcion = view.findViewById(R.id.editDescripcionProducto);
        editPrecio = view.findViewById(R.id.editPrecioProducto);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambios);

        // Llenar los campos con los valores actuales del producto
        editNombre.setText(producto.getNombre());
        editDescripcion.setText(producto.getDescripcion());
        editPrecio.setText(String.valueOf(producto.getPrecio()));

        // Acción del botón para guardar los cambios
        btnGuardarCambios.setOnClickListener(v -> {
            // Validar que no se dejen campos vacíos
            if (editNombre.getText().toString().isEmpty() ||
                    editDescripcion.getText().toString().isEmpty() ||
                    editPrecio.getText().toString().isEmpty()) {
                // Mostrar mensaje de error si hay campos vacíos
                Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar los valores del producto
            producto.setNombre(editNombre.getText().toString());
            producto.setDescripcion(editDescripcion.getText().toString());
            producto.setPrecio(Double.parseDouble(editPrecio.getText().toString()));

            // Comunicar los cambios a la actividad principal
            listener.onProductoEditado(producto, posicion);  // Pasamos también la posición
            dismiss();  // Cerrar el diálogo
        });

        // Crear el diálogo
        return new Dialog(getActivity(), getTheme()) {{
            setContentView(view);
        }};
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Asegurarse de que la actividad principal implementa la interfaz
            listener = (OnProductoEditadoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar OnProductoEditadoListener");
        }
    }
}

