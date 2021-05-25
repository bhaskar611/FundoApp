package com.example.fundoapp.adapters;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.R;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter extends RecyclerView.Adapter<BaseViewHolder> {

   private   ArrayList<FirebaseNoteModel> notesList;
    FirebaseNoteManager noteManager;
   private  OnNoteListener onNoteListener;
   private ArrayList<FirebaseNoteModel> notesSource;
   private Timer timer;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public Adapter(ArrayList<FirebaseNoteModel> notesList , OnNoteListener onNoteListener ){
        this.notesList = notesList;
        this.onNoteListener = onNoteListener;
        notesSource = notesList;

    }

    @NonNull @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false),onNoteListener);
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);


    }

//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
////        switch (viewType) {
////            case VIEW_TYPE_NORMAL:
////                return new MyViewHolder(
////                        LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false));
////            case VIEW_TYPE_LOADING:
////                return new ProgressHolder(
////                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
////            default:
////                return null;
////        }
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
//        Log.e("bhaskar", "onCreateViewHolder: " );
//        return new MyViewHolder(view, onNoteListener) {
//            @Override
//            protected void clear() {
//
//            }
//        };
//    }

//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//        FirebaseNoteModel note = notesList.get(position);
//        holder.noteTitle.setText(note.getTitle());
//        holder.noteContent.setText(note.getContent());
//        Log.e("bhaskar", "onBindViewHolder: "+position );
//    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == notesList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        Log.e("bhaskar", "get Item Count: " + notesList.size());
        return notesList.size();
    }


    public void addItems(ArrayList<FirebaseNoteModel> postItems) {
        notesList.addAll(postItems);
        notifyDataSetChanged();
    }
    public void addLoading() {
        isLoaderVisible = true;
        notesList.add(new FirebaseNoteModel());
        notifyItemInserted(notesList.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = notesList.size() - 1;
        FirebaseNoteModel item = getItem(position);
        if (item != null) {
            notesList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clear() {
        notesList.clear();
        notifyDataSetChanged();
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


    public void addNote(FirebaseNoteModel note) {
        notesList.add(0,note);
        notifyItemInserted(0);
    }

    public  class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.notetitle)
        TextView textViewTitle;
        @BindView(R.id.notecontent)
        TextView textViewDescription;
        @BindView(R.id.notecard)
        CardView cardView;
        OnNoteListener onNoteListener;
        ViewHolder(View itemView,OnNoteListener onNoteListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }
        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            FirebaseNoteModel item = notesList.get(position);
            textViewTitle.setText(item.getTitle());
            textViewDescription.setText(item.getContent());
        }
        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getBindingAdapterPosition(),v);
        }


    }

    public static class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @Override
        protected void clear() {
        }
    }
}