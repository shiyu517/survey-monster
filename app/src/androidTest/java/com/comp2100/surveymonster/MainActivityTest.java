package com.comp2100.surveymonster;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.test.filters.SmallTest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.testng.reporters.jq.Main;

public class MainActivityTest  extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Test class setup
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

//    /**
//     * Check if click the button leads to another view
//     */
//    public void testButton(){
//        getActivity().findViewById(R.id.fabNewPost).performClick();
//        TextView tv = getActivity().findViewById(R.id.tvCreationTopic);
//        assertNotNull(tv);
//    }

    /**
     * Check the floating button exists
     */

    @SmallTest
    public void testFloatingActionButton(){
        FloatingActionButton bt = (FloatingActionButton)getActivity().findViewById(R.id.fabNewPost);
        assertNotNull(bt);
    }
    /**
     * Check the tab exists
     */
    public void testTabs(){
        TabLayout tab = (TabLayout) getActivity().findViewById(R.id.tabs);
        assertNotNull(tab);
    }
    /**
     * Test class tear down
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
