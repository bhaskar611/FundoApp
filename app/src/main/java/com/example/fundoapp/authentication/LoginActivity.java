package com.example.fundoapp.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fundoapp.R;
import com.example.fundoapp.SharedPreference;
import com.example.fundoapp.dashboard.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;

    private final String TAG = "LoginActivity";
    private final int RC_SIGN_IN = 1;
    EditText emailId, passwordId;
    Button btnSignIn;
    TextView textViewSignUp,forgotpassword;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    SharedPreference sharedPreference;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;



    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sharedPreference = new SharedPreference(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setupClickListeners(){
        btnSignIn.setOnClickListener( v -> signInValidation());

        textViewSignUp.setOnClickListener(v -> {
            Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intSignUp);
        });

        signInButton.setOnClickListener(view -> signIn());

    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId() );
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        if (task.getResult().getAdditionalUserInfo().isNewUser()){
                            String userName = firebaseUser.getDisplayName();
                            String email = firebaseUser.getEmail();
                            Map<String, Object> user = new HashMap<>();
                            user.put("UserName", userName);
                            user.put("Email", email);
                            firebaseFirestore.collection("Users").document(firebaseUser.getUid()).set(user);
                        }
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();

            Toast.makeText(LoginActivity.this,personName + personEmail ,Toast.LENGTH_SHORT).show();
        }

    }

    private void setForgotpassword(){
        forgotpassword.setOnClickListener( v -> {
            final EditText resetMail = new EditText(v.getContext());
            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password ?");
            passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Conform", (dialog, which) -> {
                // extract the email and send reset link
                String mail = resetMail.getText().toString();
                mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show());

            });

            passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> {
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
                    sharedPreference.setLoggedIN(true);
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
