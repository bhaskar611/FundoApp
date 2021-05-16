package com.example.fundoapp.data_manager;
import android.util.Log;

import com.example.fundoapp.adapters.Adapter;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.util.CallBack;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseNoteManager {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private static final String TAG = "FirebaseNoteManager";
    Adapter adapter;

    public void  getAllNotes(CallBack listener) {
        ArrayList<FirebaseNoteModel> noteslist = new ArrayList<FirebaseNoteModel>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .collection("myNotes").get().addOnSuccessListener(queryDocumentSnapshots -> {
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

    public void addNote(String title, String content, CallBack<Boolean> addListener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("myNotes").document();
        Map<String, Object> note = new HashMap<>();
        note.put("title", title);
        note.put("content",content);
        note.put("Creation Date", System.currentTimeMillis());

        documentReference.set(note)
                .addOnSuccessListener(aVoid -> addListener.onSuccess(true))
                .addOnFailureListener(addListener::onFailure
                );
    }

    public void deleteNote(String docID){

       FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore
                .collection("Users")
                .document(firebaseUser.getUid())
                .collection("myNotes").document(docID);
        documentReference.delete();
    }
}
