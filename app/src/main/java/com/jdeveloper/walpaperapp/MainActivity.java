package com.jdeveloper.walpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jdeveloper.walpaperapp.Adapters.CuratedAdapter;
import com.jdeveloper.walpaperapp.Listeners.CuratedResponseListener;
import com.jdeveloper.walpaperapp.Listeners.OnRecylerClickListener;
import com.jdeveloper.walpaperapp.Listeners.SearchResponseListener;
import com.jdeveloper.walpaperapp.Models.CuratedApiResponse;
import com.jdeveloper.walpaperapp.Models.Photo;
import com.jdeveloper.walpaperapp.Models.SearchApiResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecylerClickListener{

    RecyclerView recyclerView_home;
    CuratedAdapter adapter;
    ProgressDialog dialog;
    RequestManager manager;
    FloatingActionButton fab_next;
    FloatingActionButton fab_previous;
    int page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab_next = findViewById(R.id.fab_next);
        fab_previous = findViewById(R.id.fab_previous);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Memuat...");
        dialog.setCancelable(false);

        manager = new RequestManager(this);
        manager.getCuratedWallpapers(listener, "1");

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String next_page = String.valueOf(page + 1);
                manager.getCuratedWallpapers(listener, next_page);
                dialog.show();
            }
        });

        fab_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(page > 1){
                    String prev_page = String.valueOf(page-1);
                    manager.getCuratedWallpapers(listener, prev_page);
                    dialog.show();
                }
            }
        });

    }

    private final CuratedResponseListener listener = new CuratedResponseListener() {
        @Override
        public void onFetch(CuratedApiResponse response, String msg) {
            dialog.dismiss();
            if(response.getPhotos().isEmpty()){
                Toast.makeText(getApplicationContext(), "Foto tdak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }
            page = response.getPage();

            showData(response.getPhotos());
        }

        @Override
        public void onError(String msg) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(List<Photo> photos) {
        recyclerView_home = findViewById(R.id.recyler_home);
        recyclerView_home.setHasFixedSize(true);
        recyclerView_home.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CuratedAdapter(this, photos, this);
        recyclerView_home.setAdapter(adapter);
    }

    @Override
    public void onClick(Photo photo) {
        Intent intent = new Intent(MainActivity.this, WallpaperActivity.class);
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Cari gambar...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manager.searchCuratedWallpapers(searchResponseListener, "1", query);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private final SearchResponseListener searchResponseListener = new SearchResponseListener() {
        @Override
        public void onFetch(SearchApiResponse response, String msg) {
            dialog.dismiss();
            if(response.getPhotos().isEmpty()){
                Toast.makeText(getApplicationContext(), "Foto tak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            page = response.getPage();
            showData(response.getPhotos());
        }

        @Override
        public void onError(String msg) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    };
}