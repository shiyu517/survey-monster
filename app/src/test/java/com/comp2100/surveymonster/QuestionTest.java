package com.comp2100.surveymonster;



import com.comp2100.surveymonster.DateStorage.Choice;
import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.SurveyInput;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuestionTest {


    Question question = new Question();

    @Test
    public void testAddChoice(){
        question = new Question();
        Choice a = new Choice();
        question.addChoice(a);
        Assert.assertTrue("add isn't work",question.getChoiceMap().containsValue(a));
    }

    @Test
    public void testChoice(){
        Choice a = new Choice("CHOICE");
        Assert.assertEquals(0,a.count);
    }

    @Test
    public void testSurveyInput(){
        SurveyInput a = new SurveyInput("This is feedback");
        Assert.assertEquals("This is feedback",a.mSurveyInput);
    }

    @Test
    public void testEmptySurveyInput(){
        SurveyInput a = new SurveyInput();
        Assert.assertEquals(null, a.mSurveyInput);
    }



    @Test
    public void testQuestion(){
        Question t = new Question();
        Choice a = new Choice("CHOICE");
        t.addChoice(a);
        Assert.assertEquals(t.getChoiceMap().get("0a"), a);


        Choice b = new Choice("CHOICE 2");
        t.addChoice(b);

        Choice c = new Choice("CHOICE 3");
        t.addChoice(c);

        Assert.assertEquals( t.getChoiceMap().get("2a"), c);
    }

    @Test
    public void testQuestionCount(){
        Question t = new Question();
        Choice a = new Choice("CHOICE");
        t.addChoice(a);
        Assert.assertEquals(t.getChoiceMap().get("0a"), a);


        Choice b = new Choice("CHOICE 2");
        t.addChoice(b);

        Choice c = new Choice("CHOICE 3");
        t.addChoice(c);

        Assert.assertEquals(0,t.countTotal());
    }

    @Test
    public void testQuestionContent() {
        Question question = new Question("Sample question");
        Assert.assertEquals("Sample question",question.getQuestionContent());
    }


    @Test
    public void testAddSurveyQuestion() {
        Question question = new Question("Sample question");
        SurveyInput surveyInput = new SurveyInput("This is test feedback");
        question.addSurveyQuestion(surveyInput);

        Map<String, SurveyInput> surveyInputMap = new HashMap<>();
        surveyInputMap.put(surveyInputMap.size()+"a", surveyInput);

        Assert.assertEquals(surveyInputMap, question.getSurveyInputMap());
    }

}
