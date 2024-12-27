package com.example.finalproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.activities.QuestionsActivity;
import com.example.finalproject.databinding.RvSubcategoryDesignBinding;
import com.example.finalproject.models.SubCategory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.viewHolder> {

    Context context;
    ArrayList<SubCategory> list;
    private String catId;

    public SubCategoryAdapter(Context context, ArrayList<SubCategory> list, String catId) {
        this.context = context;
        this.list = list;
        this.catId = catId;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_subcategory_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SubCategory subCategory = list.get(position);
        holder.binding.categoryName.setText(subCategory.getSubcategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuestionsActivity.class);
                intent.putExtra("catId", catId);
                intent.putExtra("subCatId", subCategory.getKey());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        RvSubcategoryDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding  = RvSubcategoryDesignBinding.bind(itemView);
        }
    }
}
