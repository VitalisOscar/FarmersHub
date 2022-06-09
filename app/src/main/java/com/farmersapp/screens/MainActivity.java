package com.farmersapp.screens;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.farmersapp.R;
import com.farmersapp.databinding.ActivityMainBinding;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.network.requests.market.SeedDbRequest;
import com.farmersapp.screens.forum.ForumHome;
import com.farmersapp.screens.market.MarketHome;

public class MainActivity extends BaseActivity {

    // View binding
    ActivityMainBinding binding;

    // Fragments
    MarketHome marketHome;
    ForumHome forumHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View bindings
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Initialize the app shell
        initShell();

        // Set the content's view
        setContentView(binding.getRoot());
    }

    private void initShell(){
        marketHome = new MarketHome();
        forumHome = new ForumHome();

        // Attach
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(binding.frameMarket.getId(), marketHome).commit();
        fm.beginTransaction().replace(binding.frameForum.getId(), forumHome).commit();

        // Listen to bottom nav
        binding.bottomNav.setOnItemSelectedListener(item -> {
            // Hide all
            binding.frameMarket.setVisibility(View.GONE);
            binding.frameForum.setVisibility(View.GONE);

            // Set outlined icons
            MenuItem itemMarket = binding.bottomNav.getMenu().getItem(0);
            MenuItem itemForum = binding.bottomNav.getMenu().getItem(1);
            MenuItem itemMore = binding.bottomNav.getMenu().getItem(2);

            itemMarket.setIcon(R.drawable.cart_outline);
            itemForum.setIcon(R.drawable.forum_outline);
            itemMore.setIcon(R.drawable.more_outline);

            // For selected item, set appropriate title, filled icon and show associated layout
            if(item.getItemId() == R.id.itemMarket){
                binding.frameMarket.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle("Market");
                itemMarket.setIcon(R.drawable.cart_filled);
                return true;
            }

            if(item.getItemId() == R.id.itemForum){
                binding.frameForum.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle("Forum");
                itemForum.setIcon(R.drawable.forum_filled);
                return true;
            }

            if(item.getItemId() == R.id.itemMore){
                binding.frameMore.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle("More");
                itemMore.setIcon(R.drawable.more_filled);
                return true;
            }

            return false;
        });

        // Select the market destination by default
        binding.bottomNav.setSelectedItemId(R.id.itemMarket);


        // Options Menu
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.itemSeed){
                RequestDispatcher.getInstance(MainActivity.this)
                        .dispatch(new SeedDbRequest());

                return true;
            }

            return false;
        });
    }
}