package com.comp2100.surveymonster.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.R;



/**
 * Extract items for recyclerView
 */
public class SurveyTextViewHolder extends RecyclerView.ViewHolder  {
    public TextView tvSurveyText;

    public SurveyTextViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvSurveyText = itemView.findViewById(R.id.tvSurveyTextInput);
    }
}
