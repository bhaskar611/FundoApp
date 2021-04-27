package com.example.fundoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.fundoapp.LoginActivity.Flag;
import static com.example.fundoapp.LoginActivity.SHARED_PREFS;


public class SplashActivity extends AppCompatActivity {

    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams
                .FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            isLoggedIn = sharedPreferences.getBoolean(Flag, false);
            Intent intent;
             if (isLoggedIn){
                 intent = new Intent(SplashActivity.this, HomeActivity.class);
                 startActivity(intent);
                 finish();
             }else{
                 intent = new Intent(SplashActivity.this, LoginActivity.class);
                 startActivity(intent);
                 finish();
             }


        },4000);

    }
}