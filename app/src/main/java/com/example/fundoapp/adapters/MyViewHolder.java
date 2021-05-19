package com.example.fundoapp.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.fundoapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MyViewHolder extends ViewHolder implements  View.OnClickListener  {
    //implements  View.OnClickListener
    TextView noteTitle,noteContent;
    View view;
    CardView mCardView;
    Button updateButton;
    FirebaseAuth mFirebaseAuth;
   OnNoteListener onNoteListener;

    public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.notetitle);
        noteContent = itemView.findViewById(R.id.notecontent);
        mCardView = itemView.findViewById(R.id.notecard);
       // updateButton = itemView.findViewById(R.id.editButton);
        view = itemView;
        mFirebaseAuth=FirebaseAuth.getInstance();
        this.onNoteListener = onNoteListener;
      itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getBindingAdapterPosition(),v);
    }

    public interface OnNoteListener {
        void onNoteClick(int position, View viewHolder);
    }
}
