package com.farmersapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.farmersapp.databinding.ViewGridProductBinding;
import com.farmersapp.models.Product;

import java.util.ArrayList;

public class MarketGridProductsAdapter extends RecyclerView.Adapter<MarketGridProductsAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Product> products;

    public MarketGridProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGridProductBinding binding = ViewGridProductBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Product product = products.get(position);

        holder.binding.title.setText(product.getName());
        holder.binding.price.setText(product.getPrice());

        // Load the image
        Glide.with(context)
                .load(product.getImageUrl())
                .centerCrop()
                .into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    protected static class CategoryViewHolder extends RecyclerView.ViewHolder{
        ViewGridProductBinding binding;

        public CategoryViewHolder(ViewGridProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
