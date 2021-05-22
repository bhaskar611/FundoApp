package com.example.fundoapp.fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fundoapp.data_manager.FirebaseLabelModel;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.NoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.util.CallBack;
import com.example.fundoapp.util.ViewState;

import java.util.ArrayList;

public class Label_View_Model extends ViewModel {
    MutableLiveData<ViewState<ArrayList<FirebaseLabelModel>>> labelMutableLiveData =
            new MutableLiveData<>();
    private static final String TAG = "LabelViewModel";
    private NoteManager firebaseNoteManager;


    public Label_View_Model() {
        firebaseNoteManager = new FirebaseNoteManager();
        loadLabel();
    }

    private void loadLabel() {
        labelMutableLiveData.setValue(new ViewState.Loading<>());
        firebaseNoteManager.getAllLabels(new CallBack<ArrayList<FirebaseLabelModel>>() {
            @Override
            public void onSuccess(ArrayList<FirebaseLabelModel> data) {
                labelMutableLiveData.setValue(new ViewState.Success<>(data));
            }

            @Override
            public void onFailure(Exception exception) {
                labelMutableLiveData.setValue(new ViewState.Failure<>(exception));

            }
        });
    }



}
