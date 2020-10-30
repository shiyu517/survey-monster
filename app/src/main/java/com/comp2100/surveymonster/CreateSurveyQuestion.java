package com.comp2100.surveymonster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.SurveyInput;
import com.comp2100.surveymonster.DateStorage.User;
import com.comp2100.surveymonster.adapter.InputChoiceAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Page creation Step 2:
 *
 * Create question for survey
 *
 */
public class CreateSurveyQuestion extends AppCompatActivity {

    public String mSurveyKey;
    public String mSurveyTopic;
    public DatabaseReference mChoiceReference;

    InputChoiceAdapter mAdapter;

    TextInputEditText questionContent;

    CardView addChoiceCardView;
    Button castTypeButton;
    RecyclerView choiceRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey_question);

        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // extract items
        questionContent =findViewById(R.id.textInputEditTextQuestion);
        addChoiceCardView = findViewById(R.id.addChoiceCareView);
        castTypeButton = findViewById(R.id.castTypeBottom);

        // get key and topic for the survey
        mSurveyKey = getIntent().getStringExtra("Extra_Survey_Key");
        mSurveyTopic = getIntent().getStringExtra("Extra_Survey_Topic");

        mChoiceReference = FirebaseDatabase.getInstance().getReference().
                child("survey-questions").child(mSurveyKey).child("0a").child("choiceMap");

        //set up choice recycler view
        choiceRecyclerView = findViewById(R.id.recyclerInputChoice);
        choiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InputChoiceAdapter(this, mChoiceReference);
        choiceRecyclerView.setAdapter(mAdapter);

    }


    // change interface when question type is changed
    public void createSurveyQuestion(View view){
        if (addChoiceCardView.getVisibility() == View.VISIBLE) {
            mChoiceReference.setValue(null);
            addChoiceCardView.setVisibility(View.INVISIBLE);
            castTypeButton.setText("make it a voting question");
            Toast.makeText(this, "Simply press add button to create survey question", Toast.LENGTH_SHORT).show();
        } else {
            mChoiceReference.setValue("choiceMap");
            addChoiceCardView.setVisibility(View.VISIBLE);
            castTypeButton.setText("make it a survey question");
            // reset recycler view
            mAdapter = new InputChoiceAdapter(this, mChoiceReference);
            choiceRecyclerView.setAdapter(mAdapter);
        }
    }

    public void addChoice(View view){
        Choice choice = new Choice("choice");
        mChoiceReference.push().setValue(choice);
    }

    // click to post question
    public void addSingleQuestion(View view){
        Question newQuestion = new Question(questionContent.getText().toString());
        DatabaseReference mQuestionReference = FirebaseDatabase.getInstance().
                getReference().child("survey-questions").child(mSurveyKey).child("0a");

        //question can't be empty
        if (questionContent.getText().toString().isEmpty()){
            Toast.makeText(this, "Question can't be null", Toast.LENGTH_SHORT).show();
        }
        

        // update question to firebase base on the question type
        if (addChoiceCardView.getVisibility()==View.INVISIBLE){
            Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();
            mQuestionReference.setValue(newQuestion);
            finish();
        }else {
            HashMap<Integer, String> getMyInputs = mAdapter.getChoiceInputs;
            ArrayList<Integer> inputKeys = new ArrayList<>(getMyInputs.keySet());

            if (!questionContent.getText().toString().isEmpty()) {
                // ensure each choice is completed otherwise toast
                String a = "empty";
                Boolean isEmpty = false;
                Boolean isChoiceGreaterThanTwo = true;
                if (inputKeys.size() == 0 || inputKeys.size() < mAdapter.mChoiceIds.size()) {
                    isEmpty = true;
                } else if (mAdapter.mChoiceIds.size() < 2) {
                    isChoiceGreaterThanTwo = false;
                } else {
                    for (int i = 0; i < inputKeys.size(); i++) {
                        Choice choice = new Choice(getMyInputs.get(inputKeys.get(i)));
                        newQuestion.addChoice(choice);
                    }
                }

                if (isEmpty) {
                    Toast.makeText(this, "Choice can't be null", Toast.LENGTH_SHORT).show();
                }

                // ensure at least two choice
                if (!isChoiceGreaterThanTwo) {
                    Toast.makeText(this, "You should input at least 2 choice", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();
                    mQuestionReference.setValue(newQuestion);
                    finish();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            // when the survey creation have't finish, remove current survey data from firebase
            deleteSurvey();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteSurvey(){
            final String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("/users/").child(Uid);


            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get user object and use the values to update the UI
                    User user = dataSnapshot.getValue(User.class);

                    //delete child
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/posts/" + mSurveyKey, null);
                    childUpdates.put("/user-posts/" + Uid + "/" + mSurveyKey, null);
                    childUpdates.put("/survey-questions/" + mSurveyKey, null);
                    FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CreateSurveyQuestion.this, "Fail to back, please try again", Toast.LENGTH_SHORT).show();
                }
            };

            userReference.addValueEventListener(postListener);
    }
}
