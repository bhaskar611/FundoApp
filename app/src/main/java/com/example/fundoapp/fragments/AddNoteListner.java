package com.example.fundoapp.fragments;

import com.example.fundoapp.data_manager.model.FirebaseNoteModel;

public interface AddNoteListner{
    void onNoteAdded(FirebaseNoteModel note);
}
