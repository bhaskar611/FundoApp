package com.example.fundoapp;

import com.example.fundoapp.model.Note;

import java.util.ArrayList;

public interface NoteListener{
    void onNoteReceived(ArrayList<Note> noteslist);
}
