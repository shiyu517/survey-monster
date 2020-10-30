package com.comp2100.surveymonster;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.filters.SmallTest;

public class AddAllQuestionsTest extends ActivityInstrumentationTestCase2<AddAllQuestions> {
    public AddAllQuestionsTest() {
        super(AddAllQuestions.class);
    }

    /**
     * Class set up
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    @SmallTest
    public void testCardView(){
        CardView cv = (CardView)getActivity().findViewById(R.id.cardViewCreation);
        assertNotNull(cv);
    }

    public void testTextView(){
        TextView tv =  (TextView) getActivity().findViewById(R.id.tvSurveyTitle);
        assertNotNull(tv);
        //assertEquals("", tv.getText().toString());

        tv =  (TextView) getActivity().findViewById(R.id.textView3);
        assertNotNull(tv);

        tv =  (TextView) getActivity().findViewById(R.id.textView4);
        assertNotNull(tv);
    }
    public void testRecycleView(){
        RecyclerView rv = getActivity().findViewById(R.id.recyclerQuestionCreation);
        assertNotNull(rv);
    }



    /**
     * Class Tear Down
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
