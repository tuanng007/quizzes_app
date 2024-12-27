package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.finalproject.R;
import com.example.finalproject.adapters.CategoryAdapter;
import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.models.Category;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase database;
    CategoryAdapter adapter;
    ArrayList<Category> list;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView view;
    View header;
    Dialog loadingDialog;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.nav_view);

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this, list);
        binding.rvCategory.setAdapter(adapter);

        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())  {
                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Category model = dataSnapshot.getValue(Category.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Category not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

        setUpDrawerLayout();
    }

    private void setUpDrawerLayout() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                binding.topAppBar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else if (item.getItemId() == R.id.nav_logout) {
                    showLogoutConfirmationDialog();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutUser();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}