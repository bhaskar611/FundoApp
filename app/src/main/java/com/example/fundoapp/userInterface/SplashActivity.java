package com.example.fundoapp.userInterface;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundoapp.R;
import com.example.fundoapp.authentication.LoginActivity;
import com.example.fundoapp.dashboard.HomeActivity;
import com.example.fundoapp.data_manager.SharedPreference;


public class SplashActivity extends AppCompatActivity {

    private boolean isLoggedIn;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreference = new SharedPreference(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams
                .FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {

            isLoggedIn = sharedPreference.getLoggedIN();
            Intent intent;
             if (isLoggedIn){
                 intent = new Intent(SplashActivity.this, HomeActivity.class);
             }else{
                 intent = new Intent(SplashActivity.this, LoginActivity.class);
             }
            startActivity(intent);
            finish();


        },4000);

    }
}