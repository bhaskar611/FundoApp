package com.example.fundoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fundoapp.dashboard.HomeActivity;
import com.example.fundoapp.fragments.notes.NotesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class notedetails extends Fragment {

    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    HomeActivity homeActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.fragment_notes, container, false);
        return inflater.inflate(R.layout.edit_note, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getArguments().getString("title");
        String content = getArguments().getString("content");
        meditcontentofnote.setText(content);
        medittitleofnote.setText(title);

        medittitleofnote= (EditText) Objects.requireNonNull(getView()).findViewById(R.id.edittitleofnote);
        meditcontentofnote=(EditText) Objects.requireNonNull(getView()).findViewById(R.id.editcontentofnote);
        msaveeditnote= Objects.requireNonNull(getView()).findViewById(R.id.saveeditnote);

        data=getActivity().getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbarofeditnote);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        msaveeditnote.setOnClickListener(v -> {
            //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();

            String newtitle=medittitleofnote.getText().toString();
            String newcontent=meditcontentofnote.getText().toString();

            if(newtitle.isEmpty()||newcontent.isEmpty())
            {
                Toast.makeText(getContext(),"Something is empty",Toast.LENGTH_SHORT).show();
            }
            else
            {
                DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseUser.getUid()).collection("User Notes").document(data.getStringExtra("noteId"));
                Map<String,Object> note=new HashMap<>();
                note.put("title",newtitle);
                note.put("content",newcontent);
                documentReference.set(note).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                    Fragment fragment = new NotesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Failed To update",Toast.LENGTH_SHORT).show());
            }

        });


        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        meditcontentofnote.setText(notecontent);
        medittitleofnote.setText(notetitle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            homeActivity.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
