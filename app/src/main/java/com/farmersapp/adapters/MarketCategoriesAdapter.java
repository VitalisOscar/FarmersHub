package com.farmersapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.farmersapp.databinding.ViewCategoryBinding;
import com.farmersapp.models.Category;
import com.farmersapp.screens.market.ProductsActivity;

import java.util.ArrayList;

public class MarketCategoriesAdapter extends RecyclerView.Adapter<MarketCategoriesAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Category> categories;

    public MarketCategoriesAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewCategoryBinding binding = ViewCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.binding.name.setText(category.getName());

        // Load the image
        Glide.with(context)
                .load(category.getImageUrl())
                .into(holder.binding.image);

        // When clicked, open products screen
        holder.binding.wrap.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductsActivity.class);
            intent.putExtra("category_id", category.getId());
            intent.putExtra("category_name", category.getName());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    protected static class CategoryViewHolder extends RecyclerView.ViewHolder{
        ViewCategoryBinding binding;

        public CategoryViewHolder(ViewCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
