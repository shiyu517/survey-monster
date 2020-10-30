package com.comp2100.surveymonster;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.DateStorage.SurveyInput;
import com.comp2100.surveymonster.DateStorage.User;
import com.comp2100.surveymonster.adapter.ChoiceAdapter;

import com.comp2100.surveymonster.databinding.ActivityVotingPageBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Page for users to cast vote, currently only support single choice
 *
 */
public class VotingPage extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_POST_KEY = "post_key";
    public ChoiceAdapter mAdapter;
    public FirebaseAuth mAuth;

    private DatabaseReference mPostReference;
    private DatabaseReference mChoiceReference;
    private DatabaseReference mDatabase;
    private String mPostKey;
    private ActivityVotingPageBinding binding;
    private TextInputLayout surveyInput;
    DatabaseReference mSurveyQuestionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVotingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        surveyInput = findViewById(R.id.votingTextInput);

        //get authentication information
        mAuth = FirebaseAuth.getInstance();


        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);

        mChoiceReference = FirebaseDatabase.getInstance().getReference()
                .child("survey-questions").child(mPostKey).child("0a").child("choiceMap");
        mSurveyQuestionReference = mChoiceReference.getParent();
        binding.recyclerChoice.setLayoutManager(new LinearLayoutManager(this));



        // Extract survey data and update basic survey information
        ValueEventListener surveyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Survey survey = dataSnapshot.getValue(Survey.class);
                if (survey!=null) {
                    binding.basicInfo.postAuthorLayout.surveyAuthor.setText(survey.getAuthor());
                    binding.basicInfo.surveyTitle.setText("Survey Title: "+survey.getTopic());
                    binding.basicInfo.postBody.setText("Description: " +survey.getDescription());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(VotingPage.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mPostReference.addValueEventListener(surveyListener);



        //Extract question and update to interface
        ValueEventListener questionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Question question = dataSnapshot.getValue(Question.class);
                if (question !=null) {
                    if (question.getChoiceMap().isEmpty()) {
                        surveyInput.setVisibility(View.VISIBLE);
                    }
                    binding.tvPostDetailQuestion.setText("Question: " + question.getQuestionContent());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VotingPage.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mSurveyQuestionReference.addValueEventListener(questionListener);


        //mAdapter = new ChoiceAdapter(this, mChoiceReference);
        mAdapter = new ChoiceAdapter(this, mChoiceReference);
        binding.recyclerChoice.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //enable deletion activity, only creator can delete
        if (id == R.id.action_Delete) {
            if (FirebaseAuth.getInstance().getCurrentUser()!= null) {
                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("/users/").child(Uid);

                // Extract creator and compare it with the current user
                // if the current user is the creator, process deletion
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        User user = dataSnapshot.getValue(User.class);
                        if (binding.basicInfo.postAuthorLayout.surveyAuthor.getText().equals(user.username)) {

                            // delete post
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/posts/" + mPostKey, null);
                            childUpdates.put("/user-posts/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + mPostKey, null);
                            childUpdates.put("/survey-questions/" + mPostKey, null);
                            mDatabase.updateChildren(childUpdates);

                            Toast.makeText(VotingPage.this, "Deletion Success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(VotingPage.this, "Only creator can delete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };

                userReference.addValueEventListener(postListener);
            } else {
                Toast.makeText(VotingPage.this, "Only creator can delete.", Toast.LENGTH_SHORT).show();
            }
        }

        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

    }


    // set activity to show the result
    public void submitAndShowResult(final View view) {
        final Intent intent = new Intent(this, SurveyResultPage.class);

        // add input to firebase base one the type
        if (surveyInput.getVisibility() == View.INVISIBLE) {
            final int itemPosition = mAdapter.getItemPosition();
            List<String> keyList = mAdapter.getmChoiceIds();

            if (mAdapter.itemPosition == -1) {
                Toast.makeText(this, "Please make choice", Toast.LENGTH_SHORT).show();
            } else {
                // count vote and update to database
                mChoiceReference.child(keyList.get(itemPosition)).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Choice choice = mutableData.getValue(Choice.class);
                        if (choice == null) {
                            return Transaction.success(mutableData);
                        }
                        choice.count += 1;
                        mutableData.setValue(choice);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean committed,
                                           DataSnapshot currentData) {
                    }
                });
                intent.putExtra(VotingPage.EXTRA_POST_KEY, mPostKey);
                startActivity(intent);
                finish();
            }

        } else {
            String getSurveyInput = surveyInput.getEditText().getText().toString();
            if (getSurveyInput.isEmpty()){
                Toast.makeText(this, "Please input text", Toast.LENGTH_SHORT).show();
            } else {
                SurveyInput mSurveyInput = new SurveyInput(getSurveyInput);
                mSurveyQuestionReference.child("surveyInputMap").push().setValue(mSurveyInput);
                intent.putExtra(VotingPage.EXTRA_POST_KEY, mPostKey);
                startActivity(intent);
                finish();
            }
        }

    }

    public void onlyShowResult(View v){
        Intent intent = new Intent(this, SurveyResultPage.class);
        intent.putExtra(VotingPage.EXTRA_POST_KEY, mPostKey);
        startActivity(intent);
    }
}
