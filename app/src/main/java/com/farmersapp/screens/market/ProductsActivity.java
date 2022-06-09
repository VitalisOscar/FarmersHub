package com.farmersapp.screens.market;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.farmersapp.R;
import com.farmersapp.adapters.AllProductsAdapter;
import com.farmersapp.databinding.ActivityProductsBinding;
import com.farmersapp.models.Product;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.network.requests.market.GetAllProductsRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity {

    ActivityProductsBinding binding;

    // Product Filters
    Map<String, Object> filters;


    // Will hold the results
    ArrayList<Product> products = new ArrayList<>();

    AllProductsAdapter productsAdapter;


    // The request to fetch products, we need to keep an instance to handle pagination
    GetAllProductsRequest request;

    int LIMIT = 15;
    int CURRENT_PAGE = 1;

    // These will keep track of the current state views being used
    // e.g when products are being loaded for first time, the main loader is used
    // but not when loading more products on subsequent pages

    View loader = null, content = null, no_data = null, errorView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());

        filters = new HashMap<>();

        // Add adapter to recycler view
        productsAdapter = new AllProductsAdapter(this, products);
        binding.resultsRecycler.setAdapter(productsAdapter);

        // Get intent data
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getString("category_id", null) != null){
                if(getIntent().getExtras().getString("category_name", null) != null){
                    // Set the category as title
                    binding.toolbar.setTitle(getIntent().getExtras().getString("category_name"));
                }

                // Add category id to filters
                filters.put("category_id", getIntent().getExtras().getString("category_id"));
            }
        }

        // End activity when user presses back arrow on toolbar
        binding.toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        // Refreshing when there is an error or no data
        binding.errorView.findViewById(R.id.retry).setOnClickListener(view -> {
            loadProducts();
        });

        binding.noData.findViewById(R.id.refresh).setOnClickListener(view -> {
            loadProducts();
        });


        // Request
        request = new GetAllProductsRequest(filters, CURRENT_PAGE, LIMIT);

        // Load products
        // Here, we use the main layout state views for loader, error and no data states
        no_data = binding.noData;
        errorView = binding.errorView;
        content = binding.content;
        loader = binding.loader;

        loadProducts();

        setContentView(binding.getRoot());
    }

    private void loadProducts(){
        // Show the loader
        if(loader != null) loader.setVisibility(View.VISIBLE);

        // Hide other state views
        if(content != null) content.setVisibility(View.GONE);
        if(no_data != null) no_data.setVisibility(View.GONE);
        if(errorView != null) errorView.setVisibility(View.GONE);

        // Fetch for current page
        try {
            RequestDispatcher.getInstance(ProductsActivity.this)
                    .setCallbacks(
                            this,
                            this.getClass().getMethod("productsLoaded", Object.class),
                            this.getClass().getMethod("errorOccurred", Exception.class)
                    )
                    .dispatch(request.setPage(CURRENT_PAGE));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void productsLoaded(Object result){
        ArrayList<Product> results = (ArrayList<Product>) result;

        // Hide the loader
        loader.setVisibility(View.GONE);

        // Check if results were empty
        if(results.size() == 0){
            // Show the no data state
            no_data.setVisibility(View.VISIBLE);
        }else{
            // Result is a list of products
            products.addAll(results);

            // Update the recycler view adapter
            productsAdapter.notifyDataSetChanged();

            // Show the content
            content.setVisibility(View.VISIBLE);


            // Pagination
            // Check if there may be more products to be fetched
            if(results.size() == LIMIT){
                // Might be
                // Show the load more button
                binding.loadMoreBtn.setVisibility(View.VISIBLE);

                // Set the load more progress as current loader, no more as errorView and noData view
                loader = binding.loadMoreProgress;
                errorView = binding.noMoreToFetch;
                no_data = binding.noMoreToFetch;

                // This as content, will be hidden when fetching more
                content = binding.loadMoreBtn;

                // Listen to fetch more clicks
                binding.loadMoreBtn.setOnClickListener(view -> {
                    // Go to next page
                    CURRENT_PAGE++;

                    // Load products
                    loadProducts();
                });
            }else{
                // Less than LIMIT products returned, might be the last batch of available results
                // Display the text 'No more data'
                binding.noMoreToFetch.setVisibility(View.VISIBLE);

                // Hide the load more button
                binding.loadMoreBtn.setVisibility(View.GONE);
            }
        }
    }

    public void errorOccurred(Exception exception){
        // Hide the loader and show the error view
        loader.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }
}