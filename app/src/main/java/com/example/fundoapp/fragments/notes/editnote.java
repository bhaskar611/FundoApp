package com.example.fundoapp.fragments.notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fundoapp.R;
import com.example.fundoapp.dashboard.HomeActivity;
import com.example.fundoapp.fragments.notes.NotesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editnote extends Fragment    {

    private static final String TAG = "edit note";
    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    HomeActivity homeActivity;
    Button dateAndTime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.note_details, container, false);
//        medittitleofnote= (EditText) Objects.requireNonNull(getView()).findViewById(R.id.notetitle);
//        meditcontentofnote=(EditText) Objects.requireNonNull(getView()).findViewById(R.id.notecontent);
//        msaveeditnote= Objects.requireNonNull(getView()).findViewById(R.id.gotoeditnote);
        String title = getArguments().getString("title");
        String content = getArguments().getString("content");
        String docID = getArguments().getString("docID");
        Log.e(TAG, "onCreate: " +title);
        Log.e(TAG, "onCreate: " +content);

        medittitleofnote= (EditText) view.findViewById(R.id.edit_note_title);
         meditcontentofnote = (EditText) view.findViewById(R.id.edit_note_description);
        dateAndTime = view.findViewById(R.id.timePicker     );

        msaveeditnote=  view.findViewById(R.id.update_button);
        meditcontentofnote.setText(content);
        medittitleofnote.setText(title);


        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


//        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbarofeditnote);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker =new TimePickerFragment();
                timePicker.show(getFragmentManager(),"time picker");
            }
        });

        msaveeditnote.setOnClickListener(v -> {
            String newtitle=medittitleofnote.getText().toString();
            String newcontent=meditcontentofnote.getText().toString();

            if(newtitle.isEmpty()||newcontent.isEmpty())
            {
                Toast.makeText(getContext(),"Something is empty",Toast.LENGTH_SHORT).show();
            }
            else
            {
                firebaseFirestore=FirebaseFirestore.getInstance();
                DocumentReference documentReference = firebaseFirestore
                        .collection("Users")
                        .document(firebaseUser.getUid())
                        .collection("User Notes").document(docID);
                Map<String,Object> note=new HashMap<>();
                note.put("title",newtitle);
                note.put("content",newcontent);
                note.put("Creation Date", System.currentTimeMillis());

                documentReference.set(note).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                    Fragment fragment = new NotesFragment();
                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
//                    assert getFragmentManager() != null;
//                    getFragmentManager().popBackStackImmediate();
                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Failed To update",Toast.LENGTH_SHORT).show());
            }

        });


//        String notetitle=data.getStringExtra(title);
//        String notecontent=data.getStringExtra(content);
//        meditcontentofnote.setText(notecontent);
//        medittitleofnote.setText(notetitle);
        return view;
    }



}
