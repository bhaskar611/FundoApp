package com.example.fundoapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference  {
  private Context context;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String IS_LOGGED_IN = "Logged_In";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

    }
//    public SharedPreference(LoginActivity loginActivity) {
//        this.context = loginActivity;
//    }
//
//    public SharedPreference(HomeActivity homeActivity) {
//        this.context = homeActivity;
//    }
//
//    public SharedPreference(SplashActivity splashActivity) {
//        this.context = splashActivity;
//    }
    public void setLoggedIN(boolean value){
        editor  = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN,value);
        editor.apply();

    }
    public boolean getLoggedIN(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);

    }


}