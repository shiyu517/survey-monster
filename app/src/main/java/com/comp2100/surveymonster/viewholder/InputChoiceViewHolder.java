package com.comp2100.surveymonster.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


/**
 * Extract items for recyclerView
 */
public class InputChoiceViewHolder extends RecyclerView.ViewHolder {

    //public TextInputLayout inputChoice;
    public TextInputEditText textInputEditText;
    public ImageButton deleteChoice;

    public TextView tvChoice;


    public InputChoiceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvChoice = itemView.findViewById(R.id.tvAddSingleChoice);
        this.textInputEditText = itemView.findViewById(R.id.realInputChoice);
        this.deleteChoice = itemView.findViewById(R.id.deleteChoice);
    }
}
