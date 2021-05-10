package com.example.fundoapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.Adapter;
import com.example.fundoapp.FireBaseNoteManager;
import com.example.fundoapp.Note;
import com.example.fundoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    FireBaseNoteManager fireBaseNoteManager;

    private  Adapter notesAdapter;
    private  ArrayList<Note> notes = new ArrayList<Note>();

    public NotesFragment(Adapter notesAdapter) {
        this.notesAdapter = notesAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        findViews(view);
//       final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
//       linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//         recyclerView = view.findViewById(R.id.recyclerview);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(noteAdapter);
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    private void findViews(View view) {
        FloatingActionButton addnote = view.findViewById(R.id.addNotes);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
       //recyclerView.setAdapter(notesAdapter);
        fireBaseNoteManager = new FireBaseNoteManager();


        fireBaseNoteManager.getAllNotes(noteslist -> {
            Log.e("bhaskar", "onNoteReceived: "+noteslist);


                notesAdapter = new Adapter(noteslist);

           recyclerView.setAdapter(notesAdapter);
        });
        //notesAdapter = new Adapter(noteslist);
//       notesAdapter=new Adapter(fireBaseNoteManager.getAllNotes(new NoteListener() {
//           @Override
//           public void onNoteReceived(ArrayList noteslist) {
//               Log.e("bhaskar", "onNoteReceived: "+noteslist);
//           }
//       });


            //recyclerView.setAdapter();
        //recyclerView.setAdapter(notesAdapter);

        //addNotes(view);
//        addnote.setOnClickListener(v -> {
//            Fragment fragment = new AddNotes_Fragment();
//            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//
//        });
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

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
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//
//        super.onActivityCreated(savedInstanceState);
//        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//        firebaseFirestore=FirebaseFirestore.getInstance();
//
//
//
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All Notes");
//        addNotes();
////        Query query=firebaseFirestore.collection("Users").document(firebaseUser.getUid())
////                .collection("myNotes").orderBy("title",Query.Direction.ASCENDING);
////
////        FirestoreRecyclerOptions<firebasemodel> allUserNotes= new FirestoreRecyclerOptions
////                .Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
////
////        noteAdapter= new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUserNotes) {
////            @RequiresApi(api = Build.VERSION_CODES.M)
////            @Override
////            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i,
////                                            @NonNull firebasemodel firebasemodel) {
////
////
////                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.menupopbutton);
////
////                int colourcode=getRandomColor();
////                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources()
////                        .getColor(colourcode,null));
////
////                noteViewHolder.notetitle.setText(firebasemodel.getTitle());
////                noteViewHolder.notecontent.setText(firebasemodel.getContent());
////
////                String docId=noteAdapter.getSnapshots().getSnapshot(i).getId();
////
////                noteViewHolder.itemView.setOnClickListener(v -> {
////                    //we have to open note detail activity
////                    //Put the value
////                    notedetails details = new notedetails ();
////                    Bundle args = new Bundle();
////                    args.putString("title",firebasemodel.getTitle());
////                    args.putString("content",firebasemodel.getContent());
////                    args.putString("noteId",docId);
////
////                    details.setArguments(args);
////
////                    //Inflate the fragment
////                    getFragmentManager().beginTransaction().add(R.id.fragment_container, details).commit();
////
////
////                });
////
////                popupbutton.setOnClickListener(v -> {
////
////                    PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
////                    popupMenu.setGravity(Gravity.END);
////                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(item -> {
////                        //we have to open note detail activity
////                        //Put the value
////                        notedetails details = new notedetails ();
////                        Bundle args = new Bundle();
////                        args.putString("title",firebasemodel.getTitle());
////                        args.putString("content",firebasemodel.getContent());
////                        args.putString("noteId",docId);
////
////                        details.setArguments(args);
////
////                        //Inflate the fragment
////                        getFragmentManager().beginTransaction().add(R.id.fragment_container, details).commit();
////                       return false;
////                    });
////
////                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(item -> {
////                        //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
////                        DocumentReference documentReference=firebaseFirestore.collection("notes")
////                                .document(firebaseUser.getUid()).collection("myNotes").document(docId);
////                        documentReference.delete().addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(),
////                                "This note is deleted",Toast.LENGTH_SHORT).show())
////                                .addOnFailureListener(e -> Toast.makeText(v.getContext(),"Failed To Delete",
////                                        Toast.LENGTH_SHORT).show());
////                        return false;
////                    });
////
////                    popupMenu.show();
////                });
////            }
////
////            @NonNull
////            @Override
////            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
////                View view = LayoutInflater.from(container.getContext()).inflate(R.layout.notes_layout,container,false);
////                return new NoteViewHolder(view);
////            }
////        };
//    }

    //private void addNotes(View view) {
//        FloatingActionButton addnote = view.findViewById(R.id.addNotes);
//        addnote.setOnClickListener(v -> {
//            Fragment fragment = new AddNotes_Fragment();
//            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_container, fragment);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//
//        });
  //  }

//    public static class NoteViewHolder extends RecyclerView.ViewHolder
//    {
//        public TextView notetitle;
//        public TextView notecontent;
//        LinearLayout mnote;
//
//        public NoteViewHolder(@NonNull View itemView) {
//            super(itemView);
//            notetitle=itemView.findViewById(R.id.notetitle);
//            notecontent=itemView.findViewById(R.id.notecontent);
//            mnote=itemView.findViewById(R.id.note);
//        }
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        noteAdapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if(noteAdapter!=null)
//        {
//            noteAdapter.stopListening();
//        }
//    }
//
//    private int getRandomColor()
//    {
//        List<Integer> colorcode=new ArrayList<>();
//        colorcode.add(R.color.gray);
//        colorcode.add(R.color.pink);
//        colorcode.add(R.color.lightgreen);
//        colorcode.add(R.color.skyblue);
//        colorcode.add(R.color.color1);
//        colorcode.add(R.color.color2);
//        colorcode.add(R.color.color3);
//        colorcode.add(R.color.color4);
//        colorcode.add(R.color.color5);
//        colorcode.add(R.color.green);
//
//        Random random=new Random();
//        int number=random.nextInt(colorcode.size());
//        return colorcode.get(number);
//    }
}
