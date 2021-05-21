package com.example.fundoapp.adapters;

import android.os.Handler;
import android.os.Looper;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Adapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<FirebaseNoteModel> notesList;
    FirebaseNoteManager noteManager;
   private MyViewHolder.OnNoteListener onNoteListener;
   private ArrayList<FirebaseNoteModel> notesSource;
   private Timer timer;

    public Adapter(ArrayList<FirebaseNoteModel> notesList ,MyViewHolder.OnNoteListener onNoteListener ){
        this.notesList = notesList;
        this.onNoteListener = onNoteListener;
        notesSource = notesList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
        Log.e("bhaskar", "onCreateViewHolder: " );
        return new MyViewHolder(view,onNoteListener);
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


    public FirebaseNoteModel getItem(int position) {
        return notesList.get(position);

    }


    public void removeNote(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void searchNotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    notesList =notesSource;
                } else {
                    ArrayList<FirebaseNoteModel> temp = new ArrayList<>();
                    for (FirebaseNoteModel note : notesSource) {
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            || note.getContent().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    notesList = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },       500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }


}