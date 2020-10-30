package com.comp2100.surveymonster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

/**
 *
 * Haven't done yet..
 *
 * Set up to enable create multiple question survey
 *
 */
public class AddAllQuestions extends AppCompatActivity {

    public String mSurveyKey;
    public String mQuestionKey;
    public DatabaseReference mQuestionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_all_questions);

        mSurveyKey = getIntent().getStringExtra("Extra_Survey_Key");



    }

    public void addQuestion(View view){
        Intent intent = new Intent(this, CreateSurveyQuestion.class);
        startActivity(intent);
    }
}
