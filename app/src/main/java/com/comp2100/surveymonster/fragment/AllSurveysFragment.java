package com.comp2100.surveymonster.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 *
 * Fragment that lists all the created survey from the most recent to the latest
 *
 */
public class AllSurveysFragment extends SurveyListFragment {

    public AllSurveysFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Set limit the list to the first 100
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);
        return recentPostsQuery;
    }
}
