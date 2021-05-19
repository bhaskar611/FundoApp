package com.example.fundoapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fundoapp.R;
import com.example.fundoapp.data_manager.DBManger;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
//import com.example.fundoapp.data_manager.DatabaseHelper;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.fragments.notes.NotesFragment;
import com.example.fundoapp.util.CallBack;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNotes_Fragment extends Fragment {

    private EditText mcreatetitleofnote,mcreatecontentofnote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mprogressbarofcreatenote;
    private static final String TAG = "AddNotes_Fragment";
    DBManger mDatabaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addnotes, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FloatingActionButton msavenote = (FloatingActionButton) Objects.requireNonNull(getView()).findViewById(R.id.savenote);
        mcreatecontentofnote = (EditText) getView().findViewById(R.id.createcontentofnote);
        mcreatetitleofnote = (EditText) getView().findViewById(R.id.createtitleofnote);
        mprogressbarofcreatenote = (ProgressBar) getView().findViewById(R.id.progressbarofcreatenote);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        msavenote.setOnClickListener(this::onClick);

    }

    private void onClick(View v) {
        String title = mcreatetitleofnote.getText().toString();
        String content = mcreatecontentofnote.getText().toString();
        String user = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        long timeID = System.currentTimeMillis();
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getContext(), "Both field are Require", Toast.LENGTH_SHORT).show();
        } else {
            String currentUID = firebaseUser.getUid();
            DocumentReference exist = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
            if (currentUID.equals(exist.toString())) {
                FirebaseNoteManager firebaseNoteManager = new FirebaseNoteManager();
                firebaseNoteManager.addNote(title, content, new CallBack<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(getContext(),
                                "Note Created Successfully",
                                Toast.LENGTH_SHORT).show();
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStackImmediate();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getContext(),
                                "Failed To Create Note", Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                Map<String, Object> noteGettingUserDetails = new HashMap<>();
            noteGettingUserDetails.put("Email", email);
            noteGettingUserDetails.put("UserName",user);
                FirebaseNoteManager firebaseNoteManager = new FirebaseNoteManager();
             String  docID = firebaseNoteManager.addNote(title, content, new CallBack<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        Toast.makeText(getContext(),
                                "Note Created Successfully",
                                Toast.LENGTH_SHORT).show();
//                        assert getFragmentManager() != null;
//                        getFragmentManager().popBackStackImmediate();
                        Fragment fragment = new NotesFragment();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getContext(),
                                "Failed To Create Note", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG, " docID  " + docID );
                mDatabaseHelper = new DBManger(getContext());
                if (title.length() != 0 && content.length() !=0) {
                  if(mDatabaseHelper.addNotes(user,docID,title,content)){
                      Toast.makeText(getContext(),"note saved in SqliteDB" + docID + " + " + title ,Toast.LENGTH_SHORT).show();
                  }

                } else {
                    //toastMessage("You must put something in the text field!");
                }
                Fragment fragment = new NotesFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                assert getFragmentManager() != null;
//                getFragmentManager().popBackStackImmediate();
            }

        mprogressbarofcreatenote.setVisibility(View.VISIBLE);
        }
    }
}