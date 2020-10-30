package com.comp2100.surveymonster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.DateStorage.SurveyInput;
import com.comp2100.surveymonster.R;
import com.comp2100.surveymonster.viewholder.SurveyTextViewHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * Set adapter for all feedback in the result page
 *
 */
public class SurveyTextAdapter extends RecyclerView.Adapter<SurveyTextViewHolder> {

    public Context mContext;
    List<String> mSurveyInputIDs = new ArrayList<>();
    List<SurveyInput> mSurveyInputs = new ArrayList<>();



    public SurveyTextAdapter(final Context context, DatabaseReference ref) {
        mContext = context;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                SurveyInput surveyInput = dataSnapshot.getValue(SurveyInput.class);

                mSurveyInputIDs.add(dataSnapshot.getKey());
                mSurveyInputs.add(surveyInput);
                notifyItemInserted(mSurveyInputs.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SurveyInput surveyInput = dataSnapshot.getValue(SurveyInput.class);
                String inputKey = dataSnapshot.getKey();

                int choiceIndex = mSurveyInputs.indexOf(inputKey);
                if (choiceIndex > -1) {
                    // Replace with the new data
                    mSurveyInputs.set(choiceIndex, surveyInput);
                    // Update the RecyclerView
                    notifyItemChanged(choiceIndex);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String inputKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int choiceIndex = mSurveyInputs.indexOf(inputKey);
                if (choiceIndex > -1) {
                    // Remove data from the list
                    mSurveyInputs.remove(choiceIndex);
                    mSurveyInputs.remove(choiceIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(choiceIndex);
                } else {
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Failed to load choices.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        ref.addChildEventListener(childEventListener);
    }


    @NonNull
    @Override
    public SurveyTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_single_survey_input, parent, false);
        return new SurveyTextViewHolder(view);
    }

    // update information to textview
    @Override
    public void onBindViewHolder(@NonNull SurveyTextViewHolder holder, final int position) {
        SurveyInput thisSurveyInput = mSurveyInputs.get(position);
        holder.tvSurveyText.setText(thisSurveyInput.mSurveyInput);
    }

    @Override
    public int getItemCount() {
        return mSurveyInputs.size();
    }
}
