package com.example.fundoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout drawer;
    SharedPreference sharedPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        sharedPreference = new SharedPreference(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NotesFragment()).commit();
            navigationView.setCheckedItem(R.id.note);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.getItemId();
        if (item.getItemId() == R.id.note) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new NotesFragment()).commit();
        } else if (item.getItemId() == R.id.archive) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Archive_Fragment()).commit();
        } else if (item.getItemId() == R.id.remainder) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ReminderFragment()).commit();
        } else if (item.getItemId() == R.id.logout) {
            logout();
        } else if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        sharedPreference.setLoggedIN(false);
        finish();
        Intent intToMain = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intToMain);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
