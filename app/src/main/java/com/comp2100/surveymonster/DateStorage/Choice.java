package com.comp2100.surveymonster.DateStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * Choice class set up to enable question add choice and enable recyclerview
 */
public class Choice {
    public String choiceDescription;
    public int count;

    public Choice() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Choice(String choiceDescription) {
        this.choiceDescription = choiceDescription;
        this.count=0;
    }


}
