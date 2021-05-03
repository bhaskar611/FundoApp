package com.example.fundoapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class SharedPreference extends AppCompatActivity {
    Context context;
    public SharedPreference(LoginActivity loginActivity) {
        this.context = loginActivity;
    }

    public SharedPreference(HomeActivity homeActivity) {
        this.context = homeActivity;
    }

    public SharedPreference(SplashActivity splashActivity) {
        this.context = splashActivity;
    }

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String IS_LOGGED_IN = "Logged_In";
    SharedPreferences sharedPreferences =context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();



    public void setLoggedIN(boolean value){
        editor.putBoolean(IS_LOGGED_IN,value);
        editor.apply();
        finish();
    }

    public boolean getLoggedIN(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);

    }


}