package com.comp2100.surveymonster;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.DateStorage.User;

import com.comp2100.surveymonster.databinding.ActivityCreateBasicSurveyInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Page creation step 1:
 *
 * Input basic survey information such as survey topic, survey deadline, survey description
 *
 */
public class CreateBasicSurveyInfo extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ActivityCreateBasicSurveyInfoBinding binding;

    private TextView DeadlineDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public String mSurveyKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBasicSurveyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set selectable deadline
        DeadlineDate = (TextView) findViewById(R.id.deadline_date);
        DeadlineDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateBasicSurveyInfo.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // show the selected deadline date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                DeadlineDate.setText(date);
            }
        };

        // set activity for add survey question
        binding.bottomAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surveyCreationFinish();
            }
        });
    }


    /**
     * The activity for add survey question.
     * (Nor the Topic, deadline and the description can be empty)
     */
    public void surveyCreationFinish() {
        final String topic = Objects.requireNonNull(binding.tInputTopic.getEditText()).getText().toString();
        final String description = Objects.requireNonNull(binding.tInputDescription.getEditText()).getText().toString();

        DeadlineDate = (TextView) findViewById(R.id.deadline_date);
        String[] date_part = DeadlineDate.getText().toString().split("/", 3);

        // check whether all required field is complete
        if (!isRequirementComplete(topic, description, date_part)) {
            return;
        }

        // check whether the user has login (only login user can create survey
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            Toast.makeText(this, "Please Login to post a new survey", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extract date string
        String date = date_part[0];
        String month = date_part[1];
        String year = date_part[2];
        final String dateString = date + "." + month + "." + year;

        // check whether deadline is valid
        if (!isDeadlineValid(dateString)) {
            return;
        }


        // Disable button so there are no multi-posts
        setEditingEnabled(false);


        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // Add the current user to the database if the user isn't in the database
                        if (user == null) {
                            FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                            String username = usernameFromEmail(newUser.getEmail());
                            mDatabase.child("users").child(userId).setValue(new User(username, newUser.getEmail()));
                        } else {
                            // Write new survey
                            writeNewSurvey(userId, user.username, topic, dateString, description);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);

                        // Start add question activity
                        Intent intent = new Intent(CreateBasicSurveyInfo.this, CreateSurveyQuestion.class);
                        intent.putExtra("Extra_Survey_Key", mSurveyKey);
                        startActivity(intent);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        setEditingEnabled(true);
                    }
                });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }


    /**
     * Check whether all required field is completed
     *
     * @param topic  survey topic
     * @param description survey description
     * @param date_part extracted deadline array
     */
    private boolean isRequirementComplete(String topic, String description, String[] date_part) {
        // Title is required
        if (TextUtils.isEmpty(topic)) {
            Toast.makeText(this, "Topic is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Body is required
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date_part.length == 1) {
            Toast.makeText(this, "Deadline is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     *
     * Check whether the input deadline is valid
     *
     * @param dateString the input deadline
     * @return true if the deadline is later than the current date else false
     */
    public Boolean isDeadlineValid(String dateString) {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String time = " 00:00:00";
        String completeDateString = dateString + time;
        try {
            Date deadline = simpleDateFormat.parse(completeDateString);
            if (deadline.before(currentDate)){
                Toast.makeText(this, "Deadline should later than current date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Set unable to modify when posting to avoid bug
     *
     * @param enabled indicator to tell whether it is able to modify the input
     */
    private void setEditingEnabled(boolean enabled) {
        binding.tInputTopic.getEditText().setEnabled(enabled);
        binding.tInputDescription.getEditText().setEnabled(enabled);
        binding.deadlineDate.setEnabled(enabled);
    }


    /**
     * Send the survey data to the database
     *
     * @param userId creator user ID
     * @param username creator user name
     * @param topic the topic of the survey
     * @param dateString the deadline of the survey
     * @param description the description of the survey
     */
    private void writeNewSurvey(String userId,String username, String topic,String dateString, String description){
        // get unique key for the survey
        mSurveyKey = mDatabase.child("posts").push().getKey();

        // set up survey
        Survey survey = new Survey(userId, username, topic, dateString, description);
        Map<String, Object> surveyValues = survey.toMap();

        // Initialize the question to have 2 choices
        Question question = new Question("question");
        Map<String, Object> questionList = new HashMap<>();
        questionList.put(questionList.size()+"a", question);

        // upload the survey to database
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + mSurveyKey, surveyValues);
        childUpdates.put("/user-posts/" + userId + "/" + mSurveyKey, surveyValues);
        childUpdates.put("/survey-questions/"+mSurveyKey, questionList);

        mDatabase.updateChildren(childUpdates);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
