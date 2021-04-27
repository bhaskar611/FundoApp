package com.example.fundoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private  String TAG = "LoginActivity";
    private int RC_SIGN_IN = 1;
    EditText emailId, passwordId;
    Button btnSignIn;
    TextView textViewSignUp,forgotpassword;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String Flag = "Logged_In";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        setupClickListeners();
        setForgotpassword();

    }
    private void findViews(){

        signInButton = findViewById(R.id.sign_in_button);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        passwordId = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
        textViewSignUp = findViewById(R.id.textView);
        forgotpassword = findViewById(R.id.textView3);

    }

    private void setupClickListeners(){
        btnSignIn.setOnClickListener( v -> signInValidation());

        textViewSignUp.setOnClickListener(v -> {
            Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intSignUp);
        });

    }

    private void setForgotpassword(){
        forgotpassword.setOnClickListener( v -> {
            final EditText resetMail = new EditText(v.getContext());
            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password ?");
            passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {
                // extract the email and send reset link
                String mail = resetMail.getText().toString();
                mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show());

            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {
                // close the dialog
            });

            passwordResetDialog.create().show();


        });
    }

    public void signInValidation(){
        String email = emailId.getText().toString();
        String password = passwordId.getText().toString();
        if(email.isEmpty()){
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        } else if(!email.matches("^[a-zA-Z]+([._+-]{0,1}[a-zA-Z0-9]+)*@[a-zA-Z0-9]+.[a-zA-Z]{2,4}+(?:\\.[a-z]{2,}){0,1}$")) {
            emailId.setError("Please enter valid email id");
            emailId.requestFocus();
        }else  if(password.isEmpty()){
            passwordId.setError("Please enter your password");
            passwordId.requestFocus();
        } else  if(!password.matches("(^(?=.*[A-Z]))(?=.*[0-9])(?=.*[a-z])(?=.*[@*&^%#-*+!]{1}).{8,}$")) {
            passwordId.setError("Please enter Valid password");
            passwordId.requestFocus();
        }
        else  if(!(email.isEmpty() && password.isEmpty())){
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Flag,true );
                    editor.apply();
                    finish();
                    Intent intToHome = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intToHome);
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

        }

    }
}
