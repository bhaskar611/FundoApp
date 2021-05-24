package com.example.fundoapp.fragments;

import com.example.fundoapp.data_manager.FirebaseLabelModel;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;

public interface AddLabelListner {
    void onLabelAdded(FirebaseLabelModel label);
}
