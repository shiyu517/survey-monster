package com.comp2100.surveymonster.DateStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Define the structure that a question should be stored
 *
 */
public class Question {
    private String questionContent;
    public Map<String, Choice> choiceMap = new HashMap<>();

    public Map<String, SurveyInput> surveyInputMap = new HashMap<>();


    public Question(){

    }

    public Question(String questionContent){
        this.questionContent = questionContent;
    }

    /**
     * Multiple choices can be added in a question
     *
     * @param choice the content of the choice
     */
    public void addChoice(Choice choice){
        //choiceList.add(choice);
        choiceMap.put(choiceMap.size()+"a", choice);
    }

    public void addSurveyQuestion(SurveyInput input){
        surveyInputMap.put(surveyInputMap.size()+"a", input);
    }


    public Map<String, SurveyInput> getSurveyInputMap() {
        return surveyInputMap;
    }

    public String getQuestionContent() {
        return questionContent;
    }



    public Map<String, Choice> getChoiceMap() {
        return choiceMap;
    }


    public int countTotal(){
        int count = 0;
        ArrayList<String> keySet = new ArrayList<>(choiceMap.keySet());
        for (String key: keySet){
            Choice choice = choiceMap.get(key);
            count = count+choice.count;
        }
        return count;
    }


}
