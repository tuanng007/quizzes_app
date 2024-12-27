package com.example.finalproject.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.finalproject.R;
import com.example.finalproject.adapters.SubCategoryAdapter;
import com.example.finalproject.databinding.ActivitySubcategoryBinding;
import com.example.finalproject.models.SubCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {

    ActivitySubcategoryBinding binding;
    FirebaseDatabase database;
    SubCategoryAdapter adapter;
    ArrayList<SubCategory> list;
    Dialog loadingDialog;
    private String categoryId, categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubcategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        categoryId = getIntent().getStringExtra("catId");
        categoryName = getIntent().getStringExtra("name");

        binding.topAppBar.setTitle(categoryName);

        list = new ArrayList<>();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSubCatgory.setLayoutManager(layoutManager);

        adapter = new SubCategoryAdapter(this, list, categoryId);
        binding.rvSubCatgory.setAdapter(adapter);

//        binding.back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        database.getReference().child("categories").child(categoryId).child("subCategories").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SubCategory model = dataSnapshot.getValue(SubCategory.class);
                        model.setKey(dataSnapshot.getKey());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    loadingDialog.dismiss();
                } else {
                    Toast.makeText(SubCategoryActivity.this, "Category not exist", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubCategoryActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });


    }
}