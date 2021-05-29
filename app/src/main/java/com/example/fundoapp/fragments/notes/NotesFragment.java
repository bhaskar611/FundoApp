package com.example.fundoapp.fragments.notes;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fundoapp.R;
import com.example.fundoapp.adapters.Adapter;
import com.example.fundoapp.adapters.MyViewHolder;
import com.example.fundoapp.adapters.OnNoteListener;
import com.example.fundoapp.adapters.PaginationListener;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.NoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.fragments.AddNotes_Fragment;
import com.example.fundoapp.util.CallBack;
import com.example.fundoapp.util.ViewState;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.fundoapp.adapters.PaginationListener.LIMIT;


public class NotesFragment extends Fragment  {
    RecyclerView recyclerView;
    FirebaseNoteManager fireBaseNoteManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private Adapter notesAdapter;
    private static final String TAG = "FragmentNotes";
    private ArrayList<FirebaseNoteModel> firebaseNoteModels = new ArrayList<>();
    private NotesViewModel notesViewModel;
    private RecyclerView.LayoutManager layoutManager;

    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;
    private static int TOTAL_NOTES_COUNT = 0;
    private static int CURRENT_NOTES_COUNT = 0;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_notes, container, false);

        final StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
                                                 @Override
                                                 protected void loadMoreItems() {
//                                                     fetchNotes(notesAdapter.getItem(CURRENT_NOTES_COUNT-1).getCreationTime());

                                                     isLoading = true;
                                                     fetchNotes(notesAdapter.getItem(notesAdapter.getItemCount()-2).getCreationTime());
//                                                     fetchNotes(notesAdapter.getItem(notesAdapter.getItemCount()).getCreationTime());
                                                     Log.e(TAG, "loadMoreItems: " + CURRENT_NOTES_COUNT );
//                                                     fetchNotes(0);
                                                 }

                                                 @Override
                                                 public boolean isLastPage() {
                                                     return isLastPage;
                                                 }

                                                 @Override
                                                 public boolean isLoading() {
                                                     return isLoading;
                                                 }
                                             });
                    fireBaseNoteManager = new FirebaseNoteManager();

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        fetchNotes(0);
        deleteNote();


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
//                        Log.e(TAG, "onNoteReceived: " + data);
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
            }
        });
        setUpOnClickListeners(view);
        return view;
    }

    private void setUpOnClickListeners(View view) {
        FloatingActionButton addnote = view.findViewById(R.id.changePicButton);
        addnote.setOnClickListener(v -> {
            Fragment fragment = new AddNotes_Fragment();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });
    }


    private void deleteNote() {
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
    }


//    private void switchViews(View view) {
//        Switch switchview = view.findViewById(R.id.switch1);
//        switchview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int spanCount ;
//                if (switchview.isChecked()){
//                    spanCount =1;
//
//                } else {
//                    spanCount =getResources().getInteger(R.integer.span_count);
//                }
//                layoutManager = new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
//                recyclerView.setLayoutManager(layoutManager);
////
//            }
//        });
////
//    }


    private void fetchNotes(long timestamp) {
        fetchAllNotesSize(new CallBack<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                TOTAL_NOTES_COUNT = data;
                Log.e(TAG, "onSuccess: total notes count " +  data );
                ArrayList<FirebaseNoteModel> noteslist = new ArrayList<FirebaseNoteModel>();
                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                firebaseFirestore= FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                        .collection("User Notes")
                        .orderBy("Creation Date")
                        .startAfter(timestamp)
//                        .startAt(timestamp)
                        .limit(LIMIT)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                int i;
                                for (i=0;i<queryDocumentSnapshots.size();i++) {
                                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
//                                    Log.e(TAG, "onSuccess: " + documentSnapshot);
                                    String title = documentSnapshot.getString("title");
                                    String content = documentSnapshot.getString("content");
                                    String docID = documentSnapshot.getId();
                                    long timestamp = documentSnapshot.getLong("Creation Date");

                                    FirebaseNoteModel note = new FirebaseNoteModel(title, content, docID);
                                    note.setCreationTime(timestamp);
                                    noteslist.add(note);
//                                    CURRENT_NOTES_COUNT += i;
                                }
//
                                if (CURRENT_NOTES_COUNT != 0)
                                    notesAdapter.removeLoading();
//                                notesAdapter.addItems(noteslist);
                                if (CURRENT_NOTES_COUNT < TOTAL_NOTES_COUNT ) {
                                    Log.e(TAG, "onSuccess: Current & Total "+ CURRENT_NOTES_COUNT + " : " + TOTAL_NOTES_COUNT );
  //                                 notesAdapter.addLoading();
                                } else {
                                    Log.e(TAG, "onSuccess: is last page true " + CURRENT_NOTES_COUNT + " : " + TOTAL_NOTES_COUNT );

                                    isLastPage = true;
                                }
                                isLoading = false;
                                CURRENT_NOTES_COUNT += queryDocumentSnapshots.size() ;
                                notesAdapter.addItems(noteslist);
                            }
                        });

                recyclerView.setAdapter(notesAdapter);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

    }

    private void fetchAllNotesSize(CallBack<Integer> countCallBack){
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .collection("User Notes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        countCallBack.onSuccess(queryDocumentSnapshots.size());

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                countCallBack.onFailure(e);
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<FirebaseNoteModel> notes = new ArrayList<>();
        notesAdapter = new Adapter(notes, new OnNoteListener() {
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

//        notesViewModel.notesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ViewState<ArrayList<FirebaseNoteModel>>>() {
//            @Override
//            public void onChanged(ViewState<ArrayList<FirebaseNoteModel>> arrayListViewState) {
//                if (arrayListViewState instanceof ViewState.Loading) {
//                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
//                } else if (arrayListViewState instanceof ViewState.Success) {
//                    ArrayList<FirebaseNoteModel> notes = ((ViewState.Success<ArrayList<FirebaseNoteModel>>) arrayListViewState).getData();
////                    Log.e(TAG, "onNoteReceived: " + notes);
//                    notesAdapter = new Adapter(notes, new OnNoteListener() {
//                        @Override
//                        public void onNoteClick(int position, View viewHolder) {
//
//                            Toast.makeText(getContext(), "Note Clicked at Position " + position, Toast.LENGTH_SHORT).show();
//                            String title = notesAdapter.getItem(position).getTitle();
//                            String content = notesAdapter.getItem(position).getContent();
//                            String docID = notesAdapter.getItem(position).getId();
//                            //Put the value
//                            editnote ldf = new editnote();
//                            Bundle args1 = new Bundle();
//
//                            args1.putString("title", title);
//                            args1.putString("content",
//                                    content);
//                            args1.putString("docID",
//                                    docID);
//                            ldf.setArguments(args1);
//                            ldf.setArguments(args1);
//                            ldf.setArguments(args1);
//
//
////Inflate the fragment
//                            getFragmentManager().beginTransaction().add(R.id.fragment_container, ldf).commit();
//                        }
//                    });
//                    recyclerView.setAdapter(notesAdapter);
//                    notesAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        });
//        recyclerView.setAdapter(notesAdapter);
//        notesAdapter.notifyDataSetChanged();

    }


    public void addnote(FirebaseNoteModel note) {
        notesAdapter.addNote(note);
    }


}
