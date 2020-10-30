package com.comp2100.surveymonster;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.textfield.TextInputLayout;

public class createBasicSurveyInfoTest extends ActivityInstrumentationTestCase2<CreateBasicSurveyInfo> {
    public createBasicSurveyInfoTest() {
        super(CreateBasicSurveyInfo.class);
    }

    /**
     * Set up the class
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test the existence of the bottom button
     */
    public void testButton(){
        Button bt = getActivity().findViewById(R.id.bottomAddQuestion);
        assertNotNull(bt);

    }
    public void testTextView(){
        TextView tv = getActivity().findViewById(R.id.tvCreationTopic);
        assertNotNull(tv);

        tv = getActivity().findViewById(R.id.deadline_date);
        assertNotNull(tv);

        tv = getActivity().findViewById(R.id.tvCreationDescription);
        assertNotNull(tv);
    }
    public void testTextInputLayout(){
        TextInputLayout til = getActivity().findViewById(R.id.tInputTopic);
        assertNotNull(til);

        til = getActivity().findViewById(R.id.tInputDate);
        assertNotNull(til);

        til = getActivity().findViewById(R.id.tInputDescription);
        assertNotNull(til);
    }

    //public void test

    /**
     * Tear down the class
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
