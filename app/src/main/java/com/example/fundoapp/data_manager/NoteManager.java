package com.example.fundoapp.data_manager;



import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.util.CallBack;

import java.util.ArrayList;

public interface NoteManager {

    void getAllNotes(CallBack<ArrayList<FirebaseNoteModel>> listener);
    void addNote(String title, String description, CallBack<String> addListener);
//    void moveToTrash(String fromPath, String toPath, String noteId);

}
