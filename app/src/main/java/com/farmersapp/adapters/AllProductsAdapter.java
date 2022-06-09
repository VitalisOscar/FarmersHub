package com.farmersapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.farmersapp.databinding.ViewProductBinding;
import com.farmersapp.models.Product;
import com.farmersapp.screens.market.SingleProductActivity;

import java.util.ArrayList;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Product> products;

    public AllProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewProductBinding binding = ViewProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Product product = products.get(position);

        holder.binding.title.setText(product.getName());
        holder.binding.price.setText(product.getPrice());
        holder.binding.category.setText(product.getCategory().getName());

        // Load the image
        Glide.with(context)
                .load(product.getImageUrl())
                .centerCrop()
                .into(holder.binding.image);

        // When clicked, open single product page, pass the product id and name
        holder.binding.details.setOnClickListener(view -> {
            Intent intent = new Intent(context, SingleProductActivity.class);
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    protected static class CategoryViewHolder extends RecyclerView.ViewHolder{
        ViewProductBinding binding;

        public CategoryViewHolder(ViewProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
