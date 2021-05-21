package com.example.fundoapp.fragments.notes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.R;
import com.example.fundoapp.adapters.Adapter;
import com.example.fundoapp.adapters.MyViewHolder;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.fragments.AddNotes_Fragment;
import com.example.fundoapp.util.CallBack;
import com.example.fundoapp.util.ViewState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class NotesFragment extends Fragment {
 //implements MyViewHolder.OnNoteListener
    RecyclerView recyclerView;
    FirebaseNoteManager fireBaseNoteManager;
    private Adapter notesAdapter;
    private static final String TAG = "FragmentNotes";
    private final ArrayList<FirebaseNoteModel> firebaseNoteModels = new ArrayList<>();
    private NotesViewModel notesViewModel;
   // RecyclerView.ViewHolder viewHolder ;
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
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                try {
                    String noteId = notesAdapter.getItem(position).getId();
                    notesAdapter.removeNote(position);
                    fireBaseNoteManager.deleteNote(noteId);
                }catch(IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        EditText inputSearch = view.findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
               // FirebaseNoteManager = new firebaseNoteManager();
                fireBaseNoteManager.getAllNotes(new CallBack<ArrayList<FirebaseNoteModel>>() {
                    @Override
                    public void onSuccess(ArrayList<FirebaseNoteModel> data) {
                        Log.e(TAG, "onNoteReceived: " + data);
                        if (data.size() != 0) {
                            notesAdapter.searchNotes(s.toString());
                        }
                       // notesMutableLiveData.setValue(new ViewState.Success<>(data));
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        //notesMutableLiveData.setValue(new ViewState.Failure<>(exception));
                    }

                });
//                firebaseNoteManager.getAllNotes(new CallBack<ArrayList<FirebaseNoteModel>>() {
//                    @Override
//                    public void onSuccess(ArrayList<FirebaseNoteModel> data) {
//                        Log.e(TAG, "onNoteReceived: " + data);
//                       // notesMutableLiveData.setValue(new ViewState.Success<>(data));
//                    }
//
//                    @Override
//                    public void onFailure(Exception exception) {
//                        //notesMutableLiveData.setValue(new ViewState.Failure<>(exception));
//
//                    }
//                });

            }
        });

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
                    notesAdapter = new Adapter(notes, new MyViewHolder.OnNoteListener() {
                        @Override
                        public void onNoteClick(int position, View viewHolder) {

                            Toast.makeText(getContext(), "Note Clicked at Position " + position, Toast.LENGTH_SHORT).show();
                            String title = notesAdapter.getItem(position).getTitle();
                            String content = notesAdapter.getItem(position).getContent();
                            String docID = notesAdapter.getItem(position).getId();
                            //Put the value
                            editnote ldf = new editnote();
                            Bundle args1 = new Bundle();

                            args1.putString("title", title);
                            args1.putString("content",
                                    content);
                            args1.putString("docID",
                                    docID);
                            ldf.setArguments(args1);
                            ldf.setArguments(args1);
                            ldf.setArguments(args1);


//Inflate the fragment
                            getFragmentManager().beginTransaction().add(R.id.fragment_container, ldf).commit();
                        }
                    });
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

        FloatingActionButton addnote = Objects.requireNonNull(getView()).findViewById(R.id.changePicButton);
        addnote.setOnClickListener(v -> {
            Fragment fragment = new AddNotes_Fragment();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });
    }

//    public void onNoteClick(int position, View viewHolder) {
//
//        String title = notesAdapter.getItem(position).getTitle();
//        String content = notesAdapter.getItem(position).getContent();
//
//        Fragment fragment = new editnote();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        args.putString("content", content);
//        fragment.setArguments(args);
//        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
}
