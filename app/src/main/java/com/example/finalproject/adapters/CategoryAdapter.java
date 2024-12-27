package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.activities.SubCategoryActivity;
import com.example.finalproject.databinding.RvCategoryDesignBinding;
import com.example.finalproject.models.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {

    Context context;
    ArrayList<Category> list;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_category_design, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Category category = list.get(position);
        holder.binding.categoryName.setText(category.getCategoryName());

        Picasso.get()
                .load(category.getCategoryImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.binding.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("catId", category.getKey());
                intent.putExtra("name", category.getCategoryName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        RvCategoryDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding  = RvCategoryDesignBinding.bind(itemView);
        }
    }
}
