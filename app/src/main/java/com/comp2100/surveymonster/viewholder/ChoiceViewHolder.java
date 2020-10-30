package com.comp2100.surveymonster.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.R;

/**
 * Extract items for recyclerView
 */
public class ChoiceViewHolder extends RecyclerView.ViewHolder  {

    public RadioButton singleChoice;


    public ChoiceViewHolder(@NonNull View itemView) {
        super(itemView);
        singleChoice = itemView.findViewById(R.id.singleChoice);

    }

}

