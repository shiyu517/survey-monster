package com.comp2100.surveymonster;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.filters.SmallTest;

public class LoginPageTest extends ActivityInstrumentationTestCase2<LoginPage> {
    public LoginPageTest() {
        super(LoginPage.class);
    }

    /**
     * Test class setup
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test 3 buttons exist: login, register, return
     */
    @SmallTest
    public void testBtn() {
        Button bt = (Button)getActivity().findViewById(R.id.action_Login);
        assertNotNull(bt);

        bt = (Button)getActivity().findViewById(R.id.action_Register);
        assertNotNull(bt);

        //bt = (Button)getActivity().findViewById(R.id.action_Back);
        //assertNotNull(bt);
    }

    /**
     * Test 2 edittext exist: email and password
     */
    public void testTextEdit(){
        EditText et = (EditText)getActivity().findViewById(R.id.tInputEmail);
        assertNotNull(et);

        et = (EditText)getActivity().findViewById(R.id.tInputPassword);
        assertNotNull(et);
    }
    /**
     * Test if the label is "Login"
     */
    public void testTextView(){
        TextView tv = (TextView)getActivity().findViewById(R.id.loginTextView);
        assertNotNull(tv);

        assertEquals("Login", tv.getText().toString());
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
