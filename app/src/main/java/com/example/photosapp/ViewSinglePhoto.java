package com.example.photosapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;

public class ViewSinglePhoto extends AppCompatActivity {

    ZoomageView image;
    String imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        imageFile = getIntent().getStringExtra(imageFile);
        File file = new File(imageFile);
        image = findViewById(R.id.image);

        if(file.exists()) {
            Glide.with(this).load(imageFile).into(image);
        }
    }
}