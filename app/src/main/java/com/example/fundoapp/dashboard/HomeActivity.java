package com.example.fundoapp.dashboard;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fundoapp.data_manager.FirebaseLabelModel;
import com.example.fundoapp.data_manager.FirebaseUserManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.data_manager.model.FirebaseUserModel;
import com.example.fundoapp.fragments.AddLabelListner;
import com.example.fundoapp.fragments.AddNoteListner;
import com.example.fundoapp.fragments.Archive_Fragment;
import com.example.fundoapp.R;
import com.example.fundoapp.data_manager.SharedPreference;
import com.example.fundoapp.authentication.LoginActivity;
import com.example.fundoapp.fragments.Label_Fragment;
import com.example.fundoapp.fragments.Profile_Fragment;
import com.example.fundoapp.fragments.notes.AlertReceiver;
import com.example.fundoapp.fragments.notes.NotesFragment;
import com.example.fundoapp.fragments.ReminderFragment;
import com.example.fundoapp.util.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements AddNoteListner, AddLabelListner , TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "Retrive Token";
    FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout drawer;
    SharedPreference sharedPreference;
    StorageReference storageReference;
    private final FirebaseUserManager firebaseUserManager = new FirebaseUserManager();
    FirebaseUser user;
    ProgressBar mprogressbar;
    NotesFragment notesFragment;
    Label_Fragment label_fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        sharedPreference = new SharedPreference(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        notesFragment = new NotesFragment();
        label_fragment = new Label_Fragment();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    notesFragment).commit();
            navigationView.setCheckedItem(R.id.note);
        }
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name_display);
        TextView userEmail = headerView.findViewById(R.id.user_email_display);
        ImageView userDp = headerView.findViewById(R.id.user_profile);
        FloatingActionButton changePic = headerView.findViewById(R.id.changePicButton);

        mprogressbar = headerView.findViewById(R.id.progressbarofcreatenote);
        firebaseUserManager.getUserDetails(new CallBack<FirebaseUserModel>() {
            @Override
            public void onSuccess(FirebaseUserModel data) {
                userName.setText(data.getUserName());
                userEmail.setText(data.getUserEmail());
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(HomeActivity.this,
                        "Something went Wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
        StorageReference profileRef = storageReference.child("users/" + Objects.requireNonNull(fAuth.getCurrentUser()).getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(userDp));
        getNotificationFromWeather();
        retrivePushToken();
    }

    private void retrivePushToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getNotificationFromWeather() {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Got Notifiaction";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        fAuth = FirebaseAuth.getInstance();
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        mprogressbar.setVisibility(View.VISIBLE);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageView userDp = findViewById(R.id.user_profile);
                        Picasso.get().load(uri).into(userDp);
                        mprogressbar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.getItemId();
        if (item.getItemId() == R.id.note) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    notesFragment).commit();
        } else if (item.getItemId() == R.id.archive) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Archive_Fragment()).commit();
        } else if (item.getItemId() == R.id.labelTag) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    label_fragment).commit();
        } else if (item.getItemId() == R.id.remainder) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ReminderFragment()).commit();
        } else if (item.getItemId() == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Profile_Fragment()).commit();
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

    @Override
    public void onNoteAdded(FirebaseNoteModel note) {
        notesFragment.addnote(note);
    }


    @Override
    public void onLabelAdded(FirebaseLabelModel label) {
        label_fragment.addLabel(label);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int dayOfMonth = 0;
        int month = 0;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
}
