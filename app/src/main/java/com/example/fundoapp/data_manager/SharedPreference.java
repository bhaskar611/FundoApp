package com.example.fundoapp.data_manager;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference  {
    private static final String TITLE = "title";
    private static final String CONTENT = "content" ;
    private Context context;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String IS_LOGGED_IN = "Logged_In";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);

    }
    public void setLoggedIN(boolean value){
        editor  = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN,value);
        editor.apply();
    }
    public boolean getLoggedIN(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN,false);

    }

    public void setNoteTitle(String noteTitle ){
        editor = sharedPreferences.edit();
        editor.putString(TITLE,noteTitle);
        editor.apply();
    }
    public void setNoteContent(String noteContent ){
        editor = sharedPreferences.edit();
        editor.putString(CONTENT,noteContent);
        editor.apply();
    }
    public String getNoteTitle(){
        return sharedPreferences.getString(TITLE,null);
    }
    public String getNoteContent(){
        return sharedPreferences.getString(CONTENT,null);
    }
}