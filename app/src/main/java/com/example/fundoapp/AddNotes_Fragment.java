package com.example.fundoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNotes_Fragment extends Fragment {

    private EditText mcreatetitleofnote,mcreatecontentofnote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frgment_addnotes, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Button msavenote = (Button) Objects.requireNonNull(getView()).findViewById(R.id.noteSaveBtn);
        mcreatecontentofnote= (EditText) getView().findViewById(R.id.editTextTitle);
        mcreatetitleofnote= (EditText) getView().findViewById(R.id.editTextDescription);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        msavenote.setOnClickListener(v -> {
            String title=mcreatetitleofnote.getText().toString();
            String content=mcreatecontentofnote.getText().toString();
            if(title.isEmpty() || content.isEmpty())
            {
                Toast.makeText(getContext(),"Both field are Require",Toast.LENGTH_SHORT).show();
            }
            else
            {



                DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                Map<String ,Object> note= new HashMap<>();
                note.put("title",title);
                note.put("content",content);

                documentReference.set(note).addOnSuccessListener(aVoid -> Toast.makeText(getContext(),"Note Created Succesffuly",Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getContext(),"Failed To Create Note",Toast.LENGTH_SHORT).show());
            }
        });
    }
}