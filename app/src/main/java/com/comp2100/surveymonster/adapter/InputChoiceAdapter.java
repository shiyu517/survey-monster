package com.comp2100.surveymonster.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.comp2100.surveymonster.CreateSurveyQuestion;
import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.R;
import com.comp2100.surveymonster.viewholder.InputChoiceViewHolder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create recycler view adapter for input choice
 */
public class InputChoiceAdapter extends RecyclerView.Adapter<InputChoiceViewHolder>{
    public List<Choice> mChoiceList = new ArrayList<>();
    public List<String> mChoiceIds = new ArrayList<>();
    Context mContext;
    //public TextInputLayout textInputChoice;
    public String key;
    public int selectedPos;

    DatabaseReference mChoiceReference;

    /**
     * extract choice content and choice key from firebase for bind the view holder latter
     * & extract page context for set up inflater latter
     *
     * @param context  add single question page
     * @param databaseReference the reference for choice in the firebase realtime database
     */
    public InputChoiceAdapter(Context context, final DatabaseReference databaseReference){
        mContext = context;
        mChoiceReference = databaseReference;

        // update recycler view when the choice have been added, removed or changed
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Choice choice = dataSnapshot.getValue(Choice.class);
                mChoiceList.add(choice);
                mChoiceIds.add(dataSnapshot.getKey());

                if (key == null) {
                    key = dataSnapshot.getKey();
                }
                notifyItemInserted(mChoiceIds.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Choice newChoice = dataSnapshot.getValue(Choice.class);
                int changedChoiceIndex = mChoiceIds.indexOf(dataSnapshot.getKey());
                if (changedChoiceIndex < mChoiceList.size()) {
                    mChoiceList.set(changedChoiceIndex, newChoice);
                }
                notifyItemChanged(changedChoiceIndex);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Choice removedChoice = dataSnapshot.getValue(Choice.class);
                int removedChoiceIndex = mChoiceIds.indexOf(dataSnapshot.getKey());
                mChoiceList.remove(mChoiceIds.get(removedChoiceIndex));
                mChoiceIds.remove(removedChoiceIndex);
                getChoiceInputs.remove(removedChoiceIndex);
                notifyItemRemoved(removedChoiceIndex);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }


    @NonNull
    @Override
    public InputChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_input_choice, parent, false);
        return new InputChoiceViewHolder(view);
    }

    public HashMap<Integer, String> getChoiceInputs = new HashMap<>();

    // set up content for each view holder
    @Override
    public void onBindViewHolder(@NonNull final InputChoiceViewHolder holder, final int position) {
        final int pos = position+1;
        final int deletePosition = position;
        String key = mChoiceIds.get(position);
        holder.tvChoice.setText("Choice "+pos+":");


        TextInputEditText textInputEditText = holder.textInputEditText;
        textInputEditText.addTextChangedListener(new MyTextChangedListener(holder, getChoiceInputs));

        selectedPos = position;

        // set deletion activity
        holder.deleteChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoiceReference.child(mChoiceIds.get(position)).setValue(null);
            }
        });


    }


    /**
     * Modified text watcher to extract input text
     */
    public static class MyTextChangedListener implements TextWatcher {

        public InputChoiceViewHolder holder;
        public HashMap<Integer, String> contents;

        public MyTextChangedListener(InputChoiceViewHolder holder,  HashMap<Integer, String> contents){
            this.holder = holder;
            this.contents = contents;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(holder != null && contents != null){
                int adapterPosition = holder.getAdapterPosition();
                contents.put(adapterPosition, editable.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChoiceIds.size();
    }
}