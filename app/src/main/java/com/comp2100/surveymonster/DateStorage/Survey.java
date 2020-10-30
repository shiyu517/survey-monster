package com.comp2100.surveymonster.DateStorage;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * the define the structure of the survey should be stored
 *
 */
// [START survey_class]
@IgnoreExtraProperties
public class Survey {
    String topic;
    String uid;
    String author;
    String deadline;


    String description;
    List<Question> question = new LinkedList<>();;

    public Survey() {
        // Default constructor required for calls to DataSnapshot.getValue(Survey.class)
    }

    public Survey(String uid, String author,  String topic, String deadline, String description){
        this.uid = uid;
        this.author = author;
        this.topic = topic;
        this.deadline = deadline;
        this.description = description;
    }

    public void addQuestion(Question newQuestion){
        this.question.add(newQuestion);
    }


    public String getTopic() {
        return topic;
    }

    public String getAuthor() {
        return author;
    }

    public String getDeadline() {
        return deadline;
    }

    public List<Question> getQuestion() {
        return question;
    }



    public String getDescription() {
        return description;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("topic", topic);
        result.put("author", author);
        result.put("deadline", deadline);
        result.put("description", description);
        result.put("question", question);

        return result;
    }
    // [END post_to_map]
}
