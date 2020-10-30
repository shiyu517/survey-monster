package com.comp2100.surveymonster.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.R;
import com.comp2100.surveymonster.viewholder.ChoiceViewHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Set adapter to list all choices in the voting page
 *
 */
public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceViewHolder> {

    public Context mContext;
    public int itemPosition = -1;

    List<String> mChoiceIds = new ArrayList<>();
    List<Choice> mChoice = new ArrayList<>();


    public int getItemPosition() {
        return itemPosition;
    }
    public List<String> getmChoiceIds() {
        return mChoiceIds;
    }


    /**
     *
     * Import context and choice data reference for adapter
     *
     * @param context choice recycler view holder
     * @param ref firebase reference to extract choice data
     */
    public ChoiceAdapter(final Context context, DatabaseReference ref) {
        mContext = context;

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // A new choice has been added, add it to the displayed list
                Choice choice = dataSnapshot.getValue(Choice.class);

                mChoiceIds.add(dataSnapshot.getKey());
                mChoice.add(choice);
                notifyItemInserted(mChoice.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // modify choice when the choice is changed
                Choice newChoice = dataSnapshot.getValue(Choice.class);
                String choiceKey = dataSnapshot.getKey();

                int choiceIndex = mChoiceIds.indexOf(choiceKey);
                if (choiceIndex > -1) {
                    // Replace with the new data
                    mChoice.set(choiceIndex, newChoice);

                    // Update the RecyclerView
                    notifyItemChanged(choiceIndex);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String choiceKey = dataSnapshot.getKey();

                int choiceIndex = mChoiceIds.indexOf(choiceKey);
                if (choiceIndex > -1) {
                    // Remove data from the list
                    mChoiceIds.remove(choiceIndex);
                    mChoice.remove(choiceIndex);

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
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_single_choice, parent, false);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder holder, final int position) {
        Choice choice = mChoice.get(position);
        holder.singleChoice.setText(choice.choiceDescription);

        // set single choice
        holder.singleChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    itemPosition = position;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        if (itemPosition == position) {
            holder.singleChoice.setChecked(true);
        } else {
            holder.singleChoice.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mChoice.size();
    }
}
