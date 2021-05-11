package com.example.fundoapp.model;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FireBaseNoteManager {

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

   public void  getAllNotes(NoteListener listener) {
       ArrayList<Note> noteslist = new ArrayList<Note>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .collection("myNotes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int i;
                for (i=0;i<queryDocumentSnapshots.size();i++){
                    Log.e("bhaskar", "onSuccess: "+queryDocumentSnapshots.getDocuments().get(i) );
                  //  Note notelist = new Note("title","content");
                   String title = queryDocumentSnapshots.getDocuments().get(i).getString("title");
                    String content = queryDocumentSnapshots.getDocuments().get(i).getString("content");
                  //  noteslist.add(queryDocumentSnapshots.getDocuments().getString("content"));
                    Note note = new Note(title,content);
                    noteslist.add(note);

                }
                listener.onNoteReceived(noteslist);
            }
        });
    }


}
