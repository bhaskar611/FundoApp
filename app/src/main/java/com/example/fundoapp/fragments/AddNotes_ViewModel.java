//package com.example.fundoapp.fragments;
//
//import android.util.Log;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.fundoapp.data_manager.FirebaseNoteManager;
//import com.example.fundoapp.data_manager.NoteManager;
//import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
//import com.example.fundoapp.util.CallBack;
//import com.example.fundoapp.util.ViewState;
//
//import java.util.ArrayList;
//
//public class AddNotes_ViewModel extends ViewModel {
//    MutableLiveData<ViewState<FirebaseNoteModel>> notesMutableLiveData =
//            new MutableLiveData<>();
//    private static final String TAG = "NotesViewModel";
//    private NoteManager firebaseNoteManager;
//
//
//    public AddNotes_ViewModel() {
//        firebaseNoteManager = new FirebaseNoteManager();
//        addNotes();
//    }
//
//    private void addNotes() {
//        notesMutableLiveData.setValue();
//
//    }
//}
