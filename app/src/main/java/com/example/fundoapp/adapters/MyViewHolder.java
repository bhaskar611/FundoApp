package com.example.fundoapp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.fundoapp.R;

public class MyViewHolder extends ViewHolder {
    TextView noteTitle,noteContent;
    View view;
    CardView mCardView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTitle = itemView.findViewById(R.id.notetitle);
        noteContent = itemView.findViewById(R.id.notecontent);
        mCardView = itemView.findViewById(R.id.notecard);
        view = itemView;
    }
}
