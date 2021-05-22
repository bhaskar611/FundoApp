package com.example.fundoapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fundoapp.R;
import com.example.fundoapp.data_manager.FirebaseLabelModel;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;

import java.util.ArrayList;
import java.util.Timer;

public class Label_Adapter extends RecyclerView.Adapter<Label_Adapter.LabelViewHolder> {

    private ArrayList<FirebaseLabelModel> labelList;
    FirebaseNoteManager noteManager;
    private MyViewHolder.OnNoteListener onNoteListener;
    private ArrayList<FirebaseNoteModel> notesSource;
    private Timer timer;

    public Label_Adapter(ArrayList<FirebaseLabelModel> labelList ){
        this.labelList =labelList;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        Log.e("bhaskar", "onCreateViewHolder: " );
        return new LabelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabelViewHolder holder, final int position) {
        FirebaseLabelModel label = labelList.get(position);
        holder.labelView.setText(label.getLabel());
        Log.e("bhaskar", "onBindViewHolder: "+position );
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

    public static class LabelViewHolder extends RecyclerView.ViewHolder {
        TextView labelView;
        View view;
        CardView mCardView;

        public LabelViewHolder(View itemview) {
            super(itemview);
            labelView = itemview.findViewById(R.id.label_item);
        }
    }

}
