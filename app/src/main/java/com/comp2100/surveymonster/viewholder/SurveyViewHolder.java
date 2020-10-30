package com.comp2100.surveymonster.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.R;

import java.util.List;

/**
 *
 * View holder for survey listing fragments
 *
 */
public class SurveyViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    //public ImageView starView;
    public TextView deadlineView;
    public TextView bodyView;


    public SurveyViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.surveyTitle);
        authorView = itemView.findViewById(R.id.surveyAuthor);
        //starView = itemView.findViewById(R.id.star);
        bodyView = itemView.findViewById(R.id.postBody);
        deadlineView = itemView.findViewById(R.id.deadlineLayout);
    }


    public void bindToPost(Survey survey) {
        titleView.setText("Survey title: "+survey.getTopic());
        authorView.setText(survey.getAuthor());
        deadlineView.setText("deadline: "+survey.getDeadline());
        bodyView.setText("Description: "+survey.getDescription());
    }

}
