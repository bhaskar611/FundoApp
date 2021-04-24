package com.example.fundoapp;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText emailId, passwordId;
    Button btnSignUp;
    TextView nameId,tvSignIn,phoneId;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        passwordId = findViewById(R.id.editText2);
        btnSignUp = findViewById(R.id.button2);
        tvSignIn = findViewById(R.id.textView);
        nameId = findViewById(R.id.editTextName);
        phoneId = findViewById(R.id.editTextPhone);

        btnSignUp.setOnClickListener( v -> registerValidation());

        tvSignIn.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        });

    }

    public void registerValidation() {
        String email = emailId.getText().toString();
        String password = passwordId.getText().toString();
        String name =nameId.getText().toString();
        String phone=phoneId.getText().toString();

        if(name.isEmpty()) {
            nameId.setError("Please enter name id");
            nameId.requestFocus();
        } else if(name.matches("[0-9*$%#&^()@!_+{}';]*")) {
            nameId.setError("Please enter proper name id");
            nameId.requestFocus();
        }else if(email.isEmpty()) {
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        } else if(!email.matches("^[a-zA-Z]+([._+-]{0,1}[a-zA-Z0-9]+)*@[a-zA-Z0-9]+.[a-zA-Z]{2,4}+(?:\\.[a-z]{2,}){0,1}$")) {
            emailId.setError("Please enter valid email id");
            emailId.requestFocus();
        }else  if(password.isEmpty()) {
            passwordId.setError("Please enter your password");
            passwordId.requestFocus();
        } else  if(!password.matches("(^(?=.*[A-Z]))(?=.*[0-9])(?=.*[a-z])(?=.*[@*&^%#-*+!]{1}).{8,}$")) {
            passwordId.setError("Please enter Valid password");
            passwordId.requestFocus();
        } else if(phone.isEmpty()) {
            phoneId.setError("Please enter phone id");
            phoneId.requestFocus();
        } else if(!phone.matches("(([0-9]{2})?)[ ][0-9]{10}")) {
            phoneId.setError("Please enter valid phone id");
            phoneId.requestFocus();
        } else  if(email.isEmpty() && password.isEmpty()) {
            Toast.makeText(MainActivity.this,"Fields Are Empty!",
                    Toast.LENGTH_SHORT).show();
        } else  if(!(email.isEmpty() && password.isEmpty())) {

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this,
                            task -> {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this,"SignUp Unsuccessful, Please Try Again",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                }
                            });
        }
        else{
            Toast.makeText(MainActivity.this,"Error Occurred!",
                    Toast.LENGTH_SHORT).show();

        }
    }

}