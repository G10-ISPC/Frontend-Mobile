package com.example.riccoapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HamburgerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HamburgerAdapter hamburgerAdapter;
    private List<String> hamburgers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamburger_list);

        recyclerView = findViewById(R.id.recyclerViewHamburgers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hamburgers = new ArrayList<>();
        hamburgers.add("Hamburguesa Clásica");
        hamburgers.add("Hamburguesa con Queso");
        hamburgers.add("Hamburguesa Vegetariana");
        hamburgers.add("Hamburguesa BBQ");
        hamburgers.add("Hamburguesa de Pollo");

        hamburgerAdapter = new HamburgerAdapter(hamburgers);
        recyclerView.setAdapter(hamburgerAdapter);
    }
}


