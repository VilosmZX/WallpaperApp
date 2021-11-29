package com.jdeveloper.walpaperapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jdeveloper.walpaperapp.Models.Photo;
import com.squareup.picasso.Picasso;

public class WallpaperActivity extends AppCompatActivity {

    ImageView imageView_wallpaper;
    FloatingActionButton fab_download, fab_save_as_wallpaper;
    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        imageView_wallpaper = findViewById(R.id.imageView_wallpaper);
        fab_download = findViewById(R.id.fab_download);
        fab_save_as_wallpaper = findViewById(R.id.fab_save_as_wallpaper);
        photo = (Photo) getIntent().getSerializableExtra("photo");
        Picasso.get().load(photo.getSrc().getOriginal()).into(imageView_wallpaper);

        fab_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = null;
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                Uri uri = Uri.parse(photo.getSrc().getLarge());
                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("wallpaper_"+photo.getPhotographer())
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "Wallpaper/_"+photo.getPhotographer()+".jpg");
                downloadManager.enqueue(request);

                Toast.makeText(getApplicationContext(), "Download Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
        fab_save_as_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallpaperActivity.this);
                Bitmap bitmap = ((BitmapDrawable) imageView_wallpaper.getDrawable()).getBitmap();

                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(getApplicationContext(), "Wallpaper telah diganti", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Errors", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}