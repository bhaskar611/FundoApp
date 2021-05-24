package com.example.fundoapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edit_Label extends Fragment {

    private static final String TAG = "edit label";
    EditText medittitleoflabel;
    FloatingActionButton msaveeditlabel;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    HomeActivity homeActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.label_details, container, false);

        String label = getArguments().getString("label");
        String docID = getArguments().getString("docID");
        Log.e(TAG, "onCreate: " +label);

        medittitleoflabel = (EditText) view.findViewById(R.id.editlabel);
        msaveeditlabel =  view.findViewById(R.id.updateeditlabel);
        medittitleoflabel.setText(label);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        msaveeditlabel.setOnClickListener(v -> {
            //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();
            String newlabel= medittitleoflabel.getText().toString();

            if(newlabel.isEmpty())
            {
                Toast.makeText(getContext(),"Something is empty",Toast.LENGTH_SHORT).show();
            }
            else
            {
                firebaseFirestore=FirebaseFirestore.getInstance();
                DocumentReference documentReference = firebaseFirestore
                        .collection("Users")
                        .document(firebaseUser.getUid())
                        .collection("Labels").document(docID);
                Map<String,Object> note=new HashMap<>();
                note.put("Label",newlabel);
                documentReference.set(note).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(),"Label is updated",Toast.LENGTH_SHORT).show();
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStackImmediate();
                }).addOnFailureListener(e -> Toast.makeText(getContext(),"Failed To update",Toast.LENGTH_SHORT).show());
            }

        });
        return view;
    }
}

