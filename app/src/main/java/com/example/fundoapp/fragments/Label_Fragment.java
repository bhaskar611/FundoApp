package com.example.fundoapp.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.R;
import com.example.fundoapp.adapters.Label_Adapter;
//import com.example.fundoapp.data_manager.DBManger;
import com.example.fundoapp.data_manager.FirebaseLabelModel;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;
import com.example.fundoapp.fragments.notes.NotesFragment;
import com.example.fundoapp.fragments.notes.editnote;
import com.example.fundoapp.util.CallBack;
import com.example.fundoapp.util.ViewState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Label_Fragment extends Fragment {

    private EditText mCreateLabel;
    private ImageButton mSaveLabel;
      private ImageView   backButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mprogressbarofcreatenote;
    FirebaseNoteManager fireBaseNoteManager;
    private static final String TAG = "LabelNotes_Fragment";
//    DBManger mDatabaseHelper;
    String docID;
    RecyclerView recyclerView;
    Label_View_Model label_view_model;
    private Label_Adapter label_adapter;
    AddLabelListner addLabelListner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addLabelListner = (AddLabelListner) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_label, container, false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerviewLabel);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fireBaseNoteManager = new FirebaseNoteManager();
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
                    String labelId = label_adapter.getItem(position).getLabelId();
                    label_adapter.removeNote(position);
                    fireBaseNoteManager.deleteLabel(labelId);
                }catch(IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        label_view_model = new ViewModelProvider(this).get(Label_View_Model.class);
        mSaveLabel =(ImageButton) view.findViewById(R.id.save_Label);
        backButton =(ImageView) view.findViewById(R.id.backButton);
        mCreateLabel = (EditText) view .findViewById(R.id.createLabel);
        mSaveLabel.setOnClickListener(this::onClick);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NotesFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //mCreateLabel = (EditText) .findViewById(R.id.createLabel);
       // mSaveLabel =(ImageButton) getView().findViewById(R.id.save_Label);
       // mprogressbarofcreatenote = (ProgressBar) getView().findViewById(R.id.progressbarofcreatenote);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //mSaveLabel.setOnClickListener(this::onClick);

        label_view_model.labelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ViewState<ArrayList<FirebaseLabelModel>>>() {
            @Override
            public void onChanged(ViewState<ArrayList<FirebaseLabelModel>> arrayListViewState) {
                if (arrayListViewState instanceof ViewState.Loading) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (arrayListViewState instanceof ViewState.Success) {
                        ArrayList<FirebaseLabelModel> labels = ((ViewState.Success<ArrayList<FirebaseLabelModel>>)arrayListViewState).getData();
                                 label_adapter = new Label_Adapter(labels, new Label_Adapter.OnLabellistener() {
                                     @Override
                                     public void OnLabelClick(int position, View viewHolder) {
                                         Toast.makeText(getContext(), "Note Clicked at Position " + position, Toast.LENGTH_SHORT).show();
                                         String labelId = label_adapter.getItem(position).getLabel();
                                         String docID = label_adapter.getItem(position).getLabelId();
                                         //Put the value
                                         Edit_Label edit_label = new Edit_Label();
                                         Bundle args1 = new Bundle();

                                         args1.putString("label", labelId);
                                         args1.putString("docID",
                                                 docID);
                                         edit_label.setArguments(args1);
                                         edit_label.setArguments(args1);
//Inflate the fragment
                                         assert getFragmentManager() != null;
                                         getFragmentManager().beginTransaction().replace(R.id.fragment_container, edit_label).commit();
                                     }
                                 });
                                 recyclerView.setAdapter(label_adapter);
                                 label_adapter.notifyDataSetChanged();
                    } else {
                    Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void onClick(View v) {
        String label = mCreateLabel.getText().toString();
        String user = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        long timeID = System.currentTimeMillis();
        if (label.isEmpty()) {
            Toast.makeText(getContext(), "Both field are Require", Toast.LENGTH_SHORT).show();
        } else {
            String currentUID = firebaseUser.getUid();
            DocumentReference exist = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
            if (currentUID.equals(exist.toString())) {
                FirebaseNoteManager firebaseNoteManager = new FirebaseNoteManager();
                firebaseNoteManager.adddLabel(label, new CallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Toast.makeText(getContext(),
                                "Created Label", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getContext(),
                                "Failed To Create Label", Toast.LENGTH_SHORT).show();
                    }
                });
            } else{

                FirebaseNoteManager firebaseNoteManager = new FirebaseNoteManager();
                //String  docID =
                firebaseNoteManager.adddLabel(label, new CallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Toast.makeText(getContext(),
                                "Created Label", Toast.LENGTH_SHORT).show();
                        mCreateLabel.setText(null);
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStackImmediate();
                        FirebaseLabelModel firebaseLabelModel = new FirebaseLabelModel(label,data);
                        addLabelListner.onLabelAdded(firebaseLabelModel);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getContext(),
                                "Failed To Create Label", Toast.LENGTH_SHORT).show();
                    }
                });
                assert getFragmentManager() != null;
            }

           // mprogressbarofcreatenote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
    }
    public void addLabel(FirebaseLabelModel label) {
        label_adapter.addLabel(label);
    }

}