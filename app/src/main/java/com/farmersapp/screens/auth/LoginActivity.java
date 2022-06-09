package com.farmersapp.screens.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.farmersapp.databinding.ActivityLoginBinding;
import com.farmersapp.network.RequestDispatcher;
import com.farmersapp.network.requests.auth.LoginRequest;
import com.farmersapp.helpers.AuthHelper;
import com.farmersapp.helpers.Utils;
import com.farmersapp.models.User;
import com.farmersapp.screens.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

public class LoginActivity extends AppCompatActivity {

    // Binding
    ActivityLoginBinding binding;

    // Loader
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Authenticating...")
                .setCancellable(false)
                .setAnimationSpeed(5)
                .setDimAmount(0.5f);

        // Clicks
        binding.login.setOnClickListener(view -> {
            login();
        });

        binding.createAccount.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        binding.forgotPassword.setOnClickListener(view -> {

        });

        // Set view
        setContentView(binding.getRoot());
    }

    private void login(){
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        if(email.length() == 0){
            Snackbar.make(binding.login, "Enter your email", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(password.length() == 0){
            Snackbar.make(binding.login, "Enter your password", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Show the progress loader
        hud.show();

        // Dispatch request
        try {
            RequestDispatcher
                .getInstance(this)
                .setCallbacks(
                    this,
                    this.getClass().getMethod("onLoginSuccess", Object.class),
                    this.getClass().getMethod("onLoginError", Exception.class)
                )
                .dispatch(new LoginRequest(email, password));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void onLoginSuccess(Object result){
        // Update login state
        User user = (User) result;
        AuthHelper.saveUser(LoginActivity.this, user);

        hud.dismiss(); // Hiding the loader

        startActivity(new Intent(
                this,
                MainActivity.class
        ));

        finish();
    }

    public void onLoginError(Exception exception){
        hud.dismiss(); // Hiding the loader
        Utils.alert(LoginActivity.this, "Login Failed", exception.getMessage());
    }
}