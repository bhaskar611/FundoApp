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
import com.example.fundoapp.data_manager.FirebaseLabelModel;
import com.example.fundoapp.data_manager.FirebaseNoteManager;
import com.example.fundoapp.data_manager.model.FirebaseNoteModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Label_Adapter extends RecyclerView.Adapter<Label_Adapter.LabelViewHolder> {

    private ArrayList<FirebaseLabelModel> labelList;
    FirebaseNoteManager noteManager;
    private OnLabellistener onLabellistener;
    private ArrayList<FirebaseNoteModel> notesSource;
    private Timer timer;

    public Label_Adapter(ArrayList<FirebaseLabelModel> labelList , OnLabellistener onLabellistener){
        this.labelList =labelList;
        this.onLabellistener = onLabellistener;
    }

    @NonNull
    @Override
    public LabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_view,parent,false);
        Log.e("bhaskar", "onCreateViewHolder: " );
        return new LabelViewHolder(view,onLabellistener);
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

    public FirebaseLabelModel getItem(int position) {
        return labelList.get(position);

    }

    public void removeNote(int position) {
        labelList.remove(position);
        notifyItemRemoved(position);
    }

    public void addLabel(FirebaseLabelModel label) {
        labelList.add(0,label);
        notifyItemInserted(0);
    }

    public static class LabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView labelView;
        View view;
        CardView mCardView;
        OnLabellistener onLabellistener;

        public LabelViewHolder(View itemview,OnLabellistener onLabellistener) {
            super(itemview);
            labelView = itemview.findViewById(R.id.label_item);
            this.onLabellistener = onLabellistener;
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLabellistener.OnLabelClick(getBindingAdapterPosition(),v);
        }
    }


    public interface OnLabellistener {
        void OnLabelClick(int position,View viewHolder);
    }

    public void addLote(FirebaseLabelModel label) {
        labelList.add(0,label);
        notifyItemInserted(0);
    }

}
