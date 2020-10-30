package com.comp2100.surveymonster;

import com.comp2100.surveymonster.DateStorage.Question;
import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.DateStorage.User;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SurveyTest {
    Survey survey = new Survey();

    @Test
    public void testEmptyUser(){
        User user = new User();
        Assert.assertEquals(user.username, null);
    }


    @Test
    public void testUserName(){
        User user = new User("sample user", "@gmail.com");
        Assert.assertEquals("sample user", user.username);
    }

    @Test
    public void testUserEmail(){
        User user = new User("sample user", "@gmail.com");
        Assert.assertEquals("@gmail.com", user.email);
    }

    @Test
    public void testAddQuestion(){
        Question a = new Question();
        survey.addQuestion(a);
        Assert.assertTrue("cannot add question",survey.getQuestion().contains(a));
    }

    @Test
    public void testSurveyTitle(){
        Survey survey = new Survey("asdkjfhsadkhfj", "jiajun.zha","Only for Test", "08/09/2022", "Just for fun");
        Assert.assertEquals("Only for Test", survey.getTopic());
    }

    @Test
    public void testSurveyDeadline(){
        Survey survey = new Survey("asdkjfhsadkhfj", "jiajun.zha","Only for Test", "08/09/2022", "Just for fun");
        Assert.assertEquals("08/09/2022", survey.getDeadline());
    }

    @Test
    public void testSurveyDescription(){
        Survey survey = new Survey("asdkjfhsadkhfj", "jiajun.zha","Only for Test", "08/09/2022", "Just for fun");
        Assert.assertEquals("Just for fun", survey.getDescription());
    }

    @Test
    public void testSurveyAuthor(){
        Survey survey = new Survey("asdkjfhsadkhfj", "jiajun.zha","Only for Test", "08/09/2022", "Just for fun");
        Assert.assertEquals("jiajun.zha", survey.getAuthor());
    }


    @Test
    public void testNewSurvey(){
        Survey new_survey = new Survey("asdkjfhsadkhfj", "jiajun.zha","Only for Test", "08/09/2022", "Just for fun");
        Map<String, Object> result = new HashMap<>();
        result.put("topic", "Only for Test");
        result.put("author","jiajun.zha");
        result.put("deadline", "08/09/2022");
        result.put("description", "Just for fun");
        List<Question> l_q = new LinkedList<>();
        Question p = new Question();
        l_q.add(p);
        result.put("question",l_q);
        new_survey.addQuestion(p);
        Assert.assertEquals(new_survey.toMap(), result);

    }


}
