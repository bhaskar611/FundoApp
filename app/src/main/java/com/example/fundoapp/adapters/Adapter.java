package com.example.fundoapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.R;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<MyViewHolder> {

    private final ArrayList<FirebaseNoteModel> notesList;
    FirebaseNoteManager noteManager;

    public Adapter(ArrayList<FirebaseNoteModel> notesList){
        this.notesList = notesList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
        Log.e("bhaskar", "onCreateViewHolder: " );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        FirebaseNoteModel note = notesList.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());
        Log.e("bhaskar", "onBindViewHolder: "+position );
    }

    @Override
    public int getItemCount() {
        Log.e("bhaskar", "get Item Count: " + notesList.size());
        return notesList.size();
    }


//    public void deleteItem(int position) {
////        String docId = notesList.get(position).getId();
////        noteManager.deleteNote(docId);
//        notesList.remove(position);
//        notifyItemRemoved(position);
//    }

    public FirebaseNoteModel getItem(int position) {
        return notesList.get(position);

    }

//    public void deleteFromFireStore(int position) {
//      noteManager = new FirebaseNoteManager();
//        String docId = notesList.get(position).getId();
//        noteManager.deleteNote(docId);
//    }

    public void removeNote(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }
}