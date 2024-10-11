package com.example.riccoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HamburgerAdapter extends RecyclerView.Adapter<HamburgerAdapter.HamburgerViewHolder> {

    private List<String> hamburgers;

    // Constructor vacío (sin argumentos)
    public HamburgerAdapter() {
        this.hamburgers = new ArrayList<>(); // Inicializa la lista vacía
    }

    // Constructor con una lista de hamburguesas
    public HamburgerAdapter(List<String> hamburgers) {
        this.hamburgers = hamburgers;
    }

    @NonNull
    @Override
    public HamburgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_hamburger, parent, false);
        return new HamburgerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HamburgerViewHolder holder, int position) {
        String hamburger = hamburgers.get(position);
        holder.hamburgerNameTextView.setText(hamburger);
    }

    @Override
    public int getItemCount() {
        return hamburgers.size();
    }

    static class HamburgerViewHolder extends RecyclerView.ViewHolder {
        TextView hamburgerNameTextView;

        public HamburgerViewHolder(@NonNull View itemView) {
            super(itemView);
            hamburgerNameTextView = itemView.findViewById(R.id.textViewHamburgerName);
        }
    }
}
