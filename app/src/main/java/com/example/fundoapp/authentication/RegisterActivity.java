package com.example.fundoapp.authentication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fundoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
   private EditText emailId, passwordId;
   private Button btnSignUp;
   private TextView nameId,textViewSignIn,phoneId;
   private FirebaseAuth mFirebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setupClickListeners();
    }

    private void findViews() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        emailId = findViewById(R.id.editText);
        passwordId = findViewById(R.id.editText2);
        btnSignUp = findViewById(R.id.button2);
        textViewSignIn = findViewById(R.id.textView);
        nameId = findViewById(R.id.editTextName);
        phoneId = findViewById(R.id.editTextPhone);
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void setupClickListeners(){
        btnSignUp.setOnClickListener( v -> registerUser());
        textViewSignIn.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(i);
        });
    }

    private boolean isValidName(String name){

        if(name.isEmpty()) {
            nameId.setError("Please enter name id");
            nameId.requestFocus();
            return false;
        } else if(name.matches("[0-9*$%#&^()@!_+{}';]*")) {
            nameId.setError("Please enter proper name id");
            nameId.requestFocus();
            return false;

        }else{
            return true;
        }
    }

    private boolean isValidEmail(String email){

         if(email.isEmpty()) {
            emailId.setError("Please enter email id");
            emailId.requestFocus();
            return false;
        } else if(!email.matches("^[a-zA-Z]+([._+-]{0,1}[a-zA-Z0-9]+)*@[a-zA-Z0-9]+.[a-zA-Z]{2,4}+(?:\\.[a-z]{2,}){0,1}$")) {
            emailId.setError("Please enter valid email id");
            emailId.requestFocus();
             return false;
        }else{
            return true;
        }
    }

    private boolean isValidPassword(String password){

        if(password.isEmpty()) {
            passwordId.setError("Please enter your password");
            passwordId.requestFocus();
            return false;
        } else  if(!password.matches("(^(?=.*[A-Z]))(?=.*[0-9])(?=.*[a-z])(?=.*[@*&^%#-*+!]{1}).{8,}$")) {
            passwordId.setError("Please enter Valid password");
            passwordId.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    private boolean isValidPhone(String phone){

        if(phone.isEmpty()) {
            phoneId.setError("Please enter phone id");
            phoneId.requestFocus();
            return false;
        } else if(!phone.matches("(([0-9]{2})?)[ ][0-9]{10}")) {
            phoneId.setError("Please enter valid phone id");
            phoneId.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public void registerUser() {
        String email = emailId.getText().toString();
        String password = passwordId.getText().toString();
        String name = nameId.getText().toString();
        String phone = phoneId.getText().toString();

        if (!isValidName(name)) {
            return;
        }
        else if (!isValidEmail(email)) {
            return;
        }else if (!isValidPassword(password)) {
            return;
        } else if (!isValidPhone(phone)) {
            return;
        } else  if(email.isEmpty() && password.isEmpty()) {
            Toast.makeText(RegisterActivity.this,"Fields Are Empty!",
                    Toast.LENGTH_SHORT).show();
        } else  if(!(email.isEmpty() && password.isEmpty())) {

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this,
                            task -> {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this,"SignUp Unsuccessful, Please Try Again",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    DocumentReference documentReference = firebaseFirestore.collection("Users")
                                            .document(firebaseUser.getUid()).collection("myNotes").document();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("UserName", name);
                                    user.put("Email", email);
                                    documentReference.set(user).addOnSuccessListener(aVoid -> Toast.
                                            makeText(getApplicationContext(),
                                            "User information added Successfully", Toast.LENGTH_SHORT).show()).
                                            addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                                                    "Failed To add User information", Toast.LENGTH_SHORT).show());
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            });
        }
        else{
            Toast.makeText(RegisterActivity.this,"Error Occurred!",
                    Toast.LENGTH_SHORT).show();

        }
    }




}