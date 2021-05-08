package com.example.fundoapp;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FireBaseNoteManager {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    void getAllNotes() {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .collection("myNotes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int i;
                for (i=0;i<queryDocumentSnapshots.size();i++){
                    Log.e("bhaskar", "onSuccess: "+queryDocumentSnapshots.getDocuments().get(i) );
                }

            }
        });
    }

}
