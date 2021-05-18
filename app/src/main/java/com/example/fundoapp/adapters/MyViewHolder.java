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
//        updateButton.setOnClickListener( v -> {
//
//            final EditText resetMail = new EditText(v.getContext());
//            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
//            passwordResetDialog.setTitle("Reset Password ?");
//            passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
//            passwordResetDialog.setView(resetMail);
//
//            passwordResetDialog.setPositiveButton("Conform", (dialog, which) -> {
//                // extract the email and send reset link
//                String mail = resetMail.getText().toString();
//                mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid ->
//
//                        .show())
//                        .addOnFailureListener(e -> Toast.makeText(, "Error ! Reset Link is Not Sent" + e.getMessage(),
//                                Toast.LENGTH_SHORT).show());
//
//            });
//
//            passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> {
//                // close the dialog
//            });
//
//            passwordResetDialog.create().show();
//
//
//        });
//        }
//
//
//
//        );
    }



    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getBindingAdapterPosition(),v);
    }

    public interface OnNoteListener {
        void onNoteClick(int position, View viewHolder);
    }
}
