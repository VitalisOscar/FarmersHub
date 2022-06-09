package com.farmersapp.screens.market;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.farmersapp.databinding.ActivitySingleProductBinding;

public class SingleProductActivity extends AppCompatActivity {

    ActivitySingleProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingleProductBinding.inflate(getLayoutInflater());

        // End activity when user presses back arrow on toolbar
        binding.toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        setContentView(binding.getRoot());
    }
}