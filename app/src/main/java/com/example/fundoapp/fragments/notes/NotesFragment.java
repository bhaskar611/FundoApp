package com.example.fundoapp.fragments.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.R;
import com.example.fundoapp.adapters.Adapter;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.fragments.AddNotes_Fragment;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.util.ViewState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseNoteManager fireBaseNoteManager;
    private Adapter notesAdapter;
    private static final String TAG = "FragmentNotes";
    private final ArrayList<FirebaseNoteModel> firebaseNoteModels = new ArrayList<FirebaseNoteModel>();
    private NotesViewModel notesViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fireBaseNoteManager = new FirebaseNoteManager();
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notesViewModel.notesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ViewState<ArrayList<FirebaseNoteModel>>>() {
            @Override
            public void onChanged(ViewState<ArrayList<FirebaseNoteModel>> arrayListViewState) {
                if (arrayListViewState instanceof ViewState.Loading) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (arrayListViewState instanceof ViewState.Success) {
                    ArrayList<FirebaseNoteModel> notes = ((ViewState.Success<ArrayList<FirebaseNoteModel>>) arrayListViewState).getData();
                    Log.e(TAG, "onNoteReceived: " + notes);
                    notesAdapter = new Adapter(notes);
                    recyclerView.setAdapter(notesAdapter);
                    notesAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {

        FloatingActionButton addnote = Objects.requireNonNull(getView()).findViewById(R.id.addNotes);
        addnote.setOnClickListener(v -> {
            Fragment fragment = new AddNotes_Fragment();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });
    }
}
