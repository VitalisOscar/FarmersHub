package com.farmersapp.screens.market;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farmersapp.adapters.MarketCategoriesAdapter;
import com.farmersapp.adapters.MarketGridProductsAdapter;
import com.farmersapp.databinding.FragmentMarketHomeBinding;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.network.requests.market.GetCategoriesRequest;
import com.farmersapp.network.requests.market.GetRecentProductsRequest;
import com.farmersapp.models.Category;
import com.farmersapp.models.Product;

import java.util.ArrayList;

public class MarketHome extends Fragment {

    FragmentMarketHomeBinding binding;
    boolean CATEGORIES_LOADED = false;
    boolean PRODUCTS_LOADED = false;
    boolean VIEW_CREATED = false;

    // Home data holders
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Product> recentProducts = new ArrayList<>();

    // Adapters
    MarketCategoriesAdapter categoriesAdapter = null;
    MarketGridProductsAdapter productsAdapter = null;

    public MarketHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMarketHomeBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VIEW_CREATED = true;

        // Set up listeners
        initListeners();

        // Init the market
        // Load categories
        if(CATEGORIES_LOADED){
            updateCategoriesUi();
        }else{
            // Load in background
            new InitCategoriesTask(getActivity()).execute();
        }

        // Load recent products
        if(PRODUCTS_LOADED){
            updateProductsUi();
        }else{
            // Load in background
            new InitProductsTask(getActivity()).execute();
        }
    }


    private void updateCategoriesUi(){
        categoriesAdapter = new MarketCategoriesAdapter(getActivity(), categories);
        binding.categoriesRecycler.setAdapter(categoriesAdapter);

        // Hide the loader
        binding.categoriesLoader.setVisibility(View.GONE);
        binding.categoriesRecycler.setVisibility(View.VISIBLE);
    }

    private void updateProductsUi(){
        productsAdapter = new MarketGridProductsAdapter(getActivity(), recentProducts);
        binding.recentProductsRecycler.setAdapter(productsAdapter);

        // Hide the loader
        binding.recentProductsLoader.setVisibility(View.GONE);

        // Check if there is data
        binding.recentProductsRecycler.setVisibility(View.GONE);
        binding.recentProductsNoData.setVisibility(View.GONE);

        if(recentProducts.size() > 0){
            binding.recentProductsRecycler.setVisibility(View.VISIBLE);
        }else{
            binding.recentProductsNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners(){
        // onClickListeners etc here
    }


    // Loads categories from db in a background thread
    private class InitCategoriesTask extends AsyncTask<String, String, String>{

        Context context;

        public InitCategoriesTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // Dispatch firebase request
            try {
                RequestDispatcher.getInstance(context)
                        .setCallbacks(
                                InitCategoriesTask.this,
                                this.getClass().getMethod("loadedCallback", Object.class),
                                this.getClass().getMethod("errorCallback", Exception.class)
                        )
                        .dispatch(new GetCategoriesRequest());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void loadedCallback(Object result) {
            categories = (ArrayList<Category>) result;

            CATEGORIES_LOADED = true;

            if(VIEW_CREATED){
                updateCategoriesUi();
            }
        }

        public void errorCallback(Exception exception) {
            CATEGORIES_LOADED = true;
            updateCategoriesUi();
        }
    }

    // Loads products from db in a background thread
    private class InitProductsTask extends AsyncTask<String, String, String>{

        Context context;

        public InitProductsTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // Dispatch firebase request
            try {
                RequestDispatcher.getInstance(context)
                        .setCallbacks(
                                InitProductsTask.this,
                                this.getClass().getMethod("loadedCallback", Object.class),
                                this.getClass().getMethod("errorCallback", Exception.class)
                        )
                        .dispatch(new GetRecentProductsRequest());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void loadedCallback(Object result) {
            recentProducts = (ArrayList<Product>) result;

            PRODUCTS_LOADED = true;

            if(VIEW_CREATED){
                updateProductsUi();
            }
        }

        public void errorCallback(Exception exception) {
            PRODUCTS_LOADED = true;
            updateProductsUi();
        }
    }
}