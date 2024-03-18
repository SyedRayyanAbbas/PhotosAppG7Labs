package com.example.photosapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.MEDIA_MOUNTED;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recycler; 
    ArrayList<String> images;
    
    PhotosAdapter adapter;
    
    GridLayoutManager manager;
    
    TextView totalImages;
    
    static int PERMISSION_REQUEST_CODE = 10;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        recycler = findViewById(R.id.galleryRecycler);
        images = new ArrayList<>();
        adapter = new PhotosAdapter(this, images);
        manager = new GridLayoutManager(this, 2);
        totalImages = findViewById(R.id.galleryTP);

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(manager);

        checkPermissions();
    }
    
    private void checkPermissions() {
        int outcome = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        if (outcome == PackageManager.PERMISSION_GRANTED) {
            loadImages();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (grantResults.length > 0) {
            boolean accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (accepted) {
                loadImages();
            } else {
                Toast.makeText(this, "Permission has not been granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void loadImages() {
        boolean SDCard = Environment.getExternalStorageState().equals(MEDIA_MOUNTED);

        if (SDCard) {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String order = MediaStore.Images.Media.DATE_TAKEN + " DESC";

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, order);
            int count = cursor.getCount();
            totalImages.setText("Total Photos: " + count);

            for(int i=0; i<count; i++) {
                cursor.moveToPosition(i);
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                images.add(cursor.getString(columnIndex));
            }

            recycler.getAdapter().notifyDataSetChanged();
            cursor.close();
        }
    }
}