package com.comp2100.surveymonster.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.VotingPage;
import com.comp2100.surveymonster.R;
import com.comp2100.surveymonster.SurveyResultPage;
import com.comp2100.surveymonster.viewholder.SurveyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
;import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This is an abstract fragment class set up to list the survey from input data.
 *
 *
 */
public abstract class SurveyListFragment extends Fragment {


    private DatabaseReference mDatabase;


    private FirebaseRecyclerAdapter<Survey, SurveyViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Fragment class requires an empty public constructor
    public SurveyListFragment() {}


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_surveys, container, false);


        // Set up database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = rootView.findViewById(R.id.messagesList);

        // Avoid unnecessary layout process as changing the contents of
        // the adapter does not change it's height or the width.
        mRecycler.setHasFixedSize(true);

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout so that the latest survey listed on top
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Survey>()
                .setQuery(postsQuery, Survey.class)
                .build();


        // Set up adapter
        mAdapter = new FirebaseRecyclerAdapter<Survey, SurveyViewHolder>(options) {

            // Set up the contents for each listed survey
            @Override
            protected void onBindViewHolder(@NonNull SurveyViewHolder viewHolder, int position, @NonNull Survey survey) {
                // get key for the current survey
                final DatabaseReference surveyRef = getRef(position);
                final String surveyKey = surveyRef.getKey();

                // Set up basic survey information
                viewHolder.bindToPost(survey);

                // Set up survey deadline
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                final Date date = new Date(System.currentTimeMillis());
                String deadlineString = survey.getDeadline();
                String timeString = " 24:60:60";
                deadlineString = deadlineString+timeString;
                try {
                    final Date deadline = simpleDateFormat.parse(deadlineString);

                    // Set activity when the item is clicked
                    // Shift to voting page of the current time is before the deadline
                    // Shift to voting result page if the deadline has passed
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (date.before(deadline)) {
                                // Launch PostDetailActivity
                                Intent intent = new Intent(getActivity(), VotingPage.class);
                                intent.putExtra(VotingPage.EXTRA_POST_KEY, surveyKey);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), SurveyResultPage.class);
                                intent.putExtra(VotingPage.EXTRA_POST_KEY, surveyKey);
                                startActivity(intent);
                            }
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Set holder
            @Override
            public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SurveyViewHolder(inflater.inflate(R.layout.item_survey, viewGroup, false));
            }

        };

        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
