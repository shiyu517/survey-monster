package com.comp2100.surveymonster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.DateStorage.User;
import com.comp2100.surveymonster.adapter.SurveyTextAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This page presents the histogram of survey result
 *
 */
public class SurveyResultPage extends AppCompatActivity {

    public static final String EXTRA_POST_KEY = "post_key";

    // histogram items
    private BarChart barChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private Legend legend;


    private String mPostKey;
    private DatabaseReference mPostReference;
    private DatabaseReference mQuestionReference;


    SurveyTextAdapter mAdapter;
    TextView tvCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result_page);


        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // get post key and database reference
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mQuestionReference = FirebaseDatabase.getInstance().getReference()
                .child("survey-questions").child(mPostKey).child("0a");


        // extract items
        final TextView title = findViewById(R.id.surveyTitle);
        final TextView username = findViewById(R.id.surveyAuthor);
        final TextView deadline = findViewById(R.id.deadlineLayout);
        final TextView description = findViewById(R.id.postBody);


        barChart = findViewById(R.id.resultBarChart);
        initBarChart(barChart);

        // extract data from database and update interface
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get survey object and use the values to update the UI
                Survey survey = dataSnapshot.getValue(Survey.class);
                if (survey != null) {
                    title.setText("Survey Title: " + survey.getTopic());
                    username.setText(survey.getAuthor());
                    deadline.setText("deadline: " + survey.getDeadline());
                    description.setText("Description: "+survey.getDescription());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SurveyResultPage.this, "Failed to load result.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mPostReference.addValueEventListener(postListener);



        // extract question and choice data from database and update to histogram
        final ValueEventListener choiceListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // [START_EXCLUDE]
                Question question = dataSnapshot.getValue(Question.class);


                TextView tvQuestion = findViewById(R.id.resultQuestion);
                tvQuestion.setText("Question: "+question.getQuestionContent());

                TextView tvCount = findViewById(R.id.resultCount);
                tvCount.setText("Total count: "+ question.countTotal());

                if (question.choiceMap.isEmpty()){
                    barChart.setVisibility(View.INVISIBLE);
                    tvCount.setText("All feedback are listed below");
                }



                if (question!=null) {
                    Map<String, Choice> choiceMap = question.getChoiceMap();


                    TypedValue typedValue = new TypedValue();
                    getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);


                    showBarChart(choiceMap, question.getQuestionContent(), typedValue.data);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SurveyResultPage.this, "Failed to load result.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mQuestionReference.addValueEventListener(choiceListener);

        RecyclerView recyclerView = findViewById(R.id.resultRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAdapter = new SurveyTextAdapter(this, mQuestionReference.child("surveyInputMap"));
        recyclerView.setAdapter(mAdapter);

        tvCount = findViewById(R.id.resultCount);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    public void showCount(View view){
        tvCount.setText(mAdapter.getItemCount());
    }


    // enable deletion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Delete) {
            deleteSurvey();
        }

        // set up back bottom
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteSurvey(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("/users/").child(Uid);


            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get user object and use the values to update the UI
                    User user = dataSnapshot.getValue(User.class);

                    TextView postAuthor = findViewById(R.id.surveyAuthor);
                    if (postAuthor.getText().equals(user.username)) {

                        //delete child
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/posts/" + mPostKey, null);
                        childUpdates.put("/user-posts/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + mPostKey, null);
                        childUpdates.put("/survey-questions/" + mPostKey, null);
                        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
                        Toast.makeText(SurveyResultPage.this, "Deletion Success", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(SurveyResultPage.this, "Only creator can delete.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            userReference.addValueEventListener(postListener);
        } else {
            Toast.makeText(SurveyResultPage.this, "Only creator can delete.", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     *
     * initiate the empty bar chart
     *
     * @param barChart barChart UI item
     */
    public void initBarChart(BarChart barChart){
        // set no background color
        barChart.setBackgroundColor(Color.TRANSPARENT);
        // set no grid
        barChart.setDrawGridBackground(false);
        // set no shadow
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        // set no frame
        barChart.setDrawBorders(false);
        // set animation
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);


        //unable touch
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleXEnabled(false);
        barChart.setScaleYEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setTouchEnabled(false);


        // set x axis
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // set y axis
        leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextSize(14f);
        rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);


        // set legend
        legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        // set legend location
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
    }


    /**
     * initiate bar chart from barDataSet
     *
     * @param barDataSet data set
     * @param color      color for each bar
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setDrawValues(true);;
    }




    // set each entries for barchart
    public void showBarChart(final Map<String, Choice> resultData, String name, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();


        final List<String> valuesList = new ArrayList<String>(resultData.keySet());

        int maxCount = -1;

        for (int i = 0; i < valuesList.size(); i++) {
            Choice choice = resultData.get(valuesList.get(i));
            int count = choice.count;
            BarEntry barEntry = new BarEntry((float) (i), count );
            entries.add(barEntry);

            if (count >maxCount)  maxCount = count;
        }

        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);
        leftAxis.setAxisMaximum(maxCount);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Choice choice = resultData.get(valuesList.get((int) value));
                return choice.choiceDescription;
            }
        });




        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barData.setValueTextSize(14f);
        barChart.setData(barData);
    }


}
