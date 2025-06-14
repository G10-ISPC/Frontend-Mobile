package com.example.riccoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riccoapp.adapter.CartAdapter;
import com.example.riccoapp.api.CartManager;
import com.example.riccoapp.api.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity implements CartAdapter.OnCantidadChangeListener {

    RecyclerView recyclerViewCart;
    TextView textTotal;
    Button btnFinalizarCompra, btnVaciarCarrito;
    List<Product> productosEnCarrito = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerCarrito);
        textTotal = findViewById(R.id.tvTotalPrecio);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        btnVaciarCarrito = findViewById(R.id.btnVaciarCarrito);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Cargamos los productos
        productosEnCarrito = CartManager.getInstance().getCarrito();

        // Iniciamos el adapter con el listener
        cartAdapter = new CartAdapter(productosEnCarrito, this);
        recyclerViewCart.setAdapter(cartAdapter);

        calcularYMostrarTotal();

        btnFinalizarCompra.setOnClickListener(v -> {
            CartManager.getInstance().vaciarCarrito();
            productosEnCarrito.clear();
            cartAdapter.notifyDataSetChanged();
            calcularYMostrarTotal();
            setResult(RESULT_OK);
            finish();
        });

        btnVaciarCarrito.setOnClickListener(v -> {
            CartManager.getInstance().vaciarCarrito();
            productosEnCarrito.clear();
            cartAdapter.notifyDataSetChanged();
            calcularYMostrarTotal();
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void onCantidadChanged() {
        calcularYMostrarTotal();
        guardarCarrito();
    }

    private void calcularYMostrarTotal() {
        double total = 0;
        for (Product p : productosEnCarrito) {
            total += p.getPrecio() * p.getCantidad();
        }
        textTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void guardarCarrito() {
        SharedPreferences.Editor editor = getSharedPreferences("CartPrefs", MODE_PRIVATE).edit();
        String json = new com.google.gson.Gson().toJson(productosEnCarrito);
        editor.putString("carrito", json);
        editor.apply();
    }
}