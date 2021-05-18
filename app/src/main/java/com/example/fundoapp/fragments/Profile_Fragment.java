package com.example.fundoapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fundoapp.dashboard.HomeActivity;
import com.example.fundoapp.data_manager.FirebaseUserManager;
import com.example.fundoapp.data_manager.model.FirebaseUserModel;
import com.example.fundoapp.util.CallBack;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.example.fundoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static java.util.Objects.*;

public class Profile_Fragment extends Fragment {

    ImageView profileImageView;
    Button saveBtn;
    StorageReference storageReference;
    FirebaseAuth fAuth ;
    FirebaseFirestore fStore;
    FirebaseUser user;
    private final FirebaseUserManager firebaseUserManager = new FirebaseUserManager();
    ProgressBar mprogressbarofcreatenote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        mprogressbarofcreatenote = (ProgressBar) getView().findViewById(R.id.progressbarofcreatenote);
        profileImageView = requireNonNull(getView()).findViewById(R.id.profileImageView);
        saveBtn = requireNonNull(getView()).findViewById(R.id.savebutton);
        TextView userName = requireNonNull(getView()).findViewById(R.id.user_name_display);
        TextView userEmail = requireNonNull(getView()).findViewById(R.id.user_email_display);

        firebaseUserManager.getUserDetails(new CallBack<FirebaseUserModel>() {
            @Override
            public void onSuccess(FirebaseUserModel data) {
                userName.setText(data.getUserName());
                userEmail.setText(data.getUserEmail());
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getContext(),
                        "Something went Wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });

        saveBtn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Picasso.get().load(uri).into(profileImageView);
                Glide.with(getContext())
                        .load(uri)
                        .override(300, 200)
                        .into(profileImageView);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        fAuth = FirebaseAuth.getInstance();
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
