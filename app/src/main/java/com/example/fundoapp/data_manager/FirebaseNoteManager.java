package com.example.fundoapp.data_manager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fundoapp.adapters.Adapter;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.util.CallBack;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseNoteManager implements NoteManager {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private static final String TAG = "FirebaseNoteManager";
    Adapter adapter;
    String newNoteID;
    String newLabelID;

    public void  getAllNotes(CallBack listener) {
        ArrayList<FirebaseNoteModel> noteslist = new ArrayList<FirebaseNoteModel>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .collection("User Notes").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int i;
            for (i=0;i<queryDocumentSnapshots.size();i++){
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                Log.e(TAG, "onSuccess: "+ documentSnapshot);
                String title = documentSnapshot.getString("title");
                String content = documentSnapshot.getString("content");
                String docID = documentSnapshot.getId();
                FirebaseNoteModel firebaseNoteModel = new FirebaseNoteModel(title, content,docID);
                noteslist.add(firebaseNoteModel);
            }
            listener.onSuccess(noteslist);
        })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public void  getAllLabels(CallBack listener) {
        ArrayList<FirebaseLabelModel> noteslist = new ArrayList<FirebaseLabelModel>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .collection("Labels").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int i;
            for (i=0;i<queryDocumentSnapshots.size();i++){
                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                Log.e(TAG, "onSuccess: "+ documentSnapshot);
                String label = documentSnapshot.getString("Label");
                String docID = documentSnapshot.getId();
                FirebaseLabelModel firebaseLabelModel = new FirebaseLabelModel(label,docID);
                noteslist.add(firebaseLabelModel);
            }
            listener.onSuccess(noteslist);
        })
                .addOnFailureListener(e -> listener.onFailure(e));
    }



    public void addNote(String title, String content, CallBack<String> addListener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("User Notes").document();
        Map<String, Object> note = new HashMap<>();
        note.put("title", title);
        note.put("content",content);
        note.put("Creation Date", System.currentTimeMillis());

        documentReference.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        newNoteID = documentReference.getId();
                        addListener.onSuccess(newNoteID);
                        Log.e(TAG, "newNoteID "+ newNoteID );
                    }
                }
                )
                .addOnFailureListener(addListener::onFailure
                );
        Log.e(TAG, "addNote: " + newNoteID );
       // return newNoteID;

    }

    @Override
    public void adddLabel(String label, CallBack<String> listner) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("Labels").document();
        Map<String, Object> note = new HashMap<>();
        note.put("Label", label);
        note.put("Creation Date", System.currentTimeMillis());

        documentReference.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void aVoid) {
                                              newLabelID = documentReference.getId();
                                              listner.onSuccess(newLabelID);
                                              Log.e(TAG, "newNoteID "+ newNoteID );
                                          }
                                      }
                )
                .addOnFailureListener(listner::onFailure
                );
        Log.e(TAG, "addNote: " + newNoteID );
        // return newNoteID;

    }


    public void deleteNote(String docID){

       FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("User Notes").document(docID);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: Deleted "+ docID );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure:Error Deleted "+ docID );
            }
        });
    }

    public void deleteLabel(String labelID){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        assert firebaseUser != null;
        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("Labels").document(labelID);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: Deleted "+ labelID );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure:Error Deleted "+ labelID );
            }
        });
    }
}
