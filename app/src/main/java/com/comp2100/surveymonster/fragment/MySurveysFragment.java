package com.comp2100.surveymonster.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 *
 * Fragment that lists all the survey the current user created
 */
public class MySurveysFragment extends SurveyListFragment {

    public MySurveysFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-posts")
                .child(getUid());
    }
}
