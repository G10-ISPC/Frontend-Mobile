package com.example.riccoapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<CarouselItem> list = new ArrayList<>();
    private List<CarouselItem> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageCarousel carousel = findViewById(R.id.carousel);
        ImageCarousel carousel2 = findViewById(R.id.carousel2);

        carousel.registerLifecycle(getLifecycle());
        carousel2.registerLifecycle(getLifecycle());

        // Image drawable
        list.add(
                new CarouselItem(
                        R.drawable.carousel1
                )
        );

        // Just image drawable
        list.add(
                new CarouselItem(
                        R.drawable.carousel2
                )
        );
        list.add(
                new CarouselItem(
                        R.drawable.carousel3
                )
        );



        list2.add(
                new CarouselItem(
                        R.drawable.bur2, "Burgers Jobs"
                )
        );

        // Just image drawable
        list2.add(
                new CarouselItem(
                        R.drawable.bur1, "Bill Gates"
                )
        );
        list2.add(
                new CarouselItem(
                        R.drawable.bur5, "♡ Doble Love ♡"
                )
        );

        carousel.setData(list);
        carousel2.setData(list2);

    }
}