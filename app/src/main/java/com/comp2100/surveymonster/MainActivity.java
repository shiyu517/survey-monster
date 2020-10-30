package com.comp2100.surveymonster;

import android.content.Intent;
import android.os.Bundle;


import com.comp2100.surveymonster.databinding.ActivityMainBinding;
import com.comp2100.surveymonster.fragment.AllSurveysFragment;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;


import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


/**
 *
 * Home page:
 *
 * Home page holds two fragment, the survey post fragment and my information page
 * Home page also holds menu to allow login, logout and register
 *
 *
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create an adapter to hold two fragment
        FragmentPagerAdapter mainPageAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            //Set up fragments
            Fragment[] mFragments = new Fragment[]{
                    new AllSurveysFragment(),
                    new MyInfoFragment()};

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            //Set name for each fragment
            @Override
            public CharSequence getPageTitle(int position) {
                String[] mFragmentNames = new String[]{
                        "Recent",
                        "My Info",
                };
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the adapter.
        binding.container.setAdapter(mainPageAdapter);
        binding.tabs.setupWithViewPager(binding.container);
    }


    /**
     *  Onclick activity for fab which switches main page to survey creation page
      */
    public void createSurvey(View view){
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            Intent intent = new Intent(this, CreateBasicSurveyInfo.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please login to create survey", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *  Create option menu
      */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     *  set onclick activity for each menu option selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.manual){
            Intent intent = new Intent(this, Manual.class);
            startActivity(intent);
        }

        // app shifts to login page when the user haven't login and the login option is clicked
        if (id == R.id.action_Login) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Intent intent = new Intent(this, LoginPage.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "You are in login state", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        // logout the current user if logout option is clicked
        if (id == R.id.action_Logout) {
            FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        // Shift to register page if there are no current user and the register option is clicked
        if (id == R.id.action_Register){
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Intent intent = new Intent(this, RegisterPage.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please logout current user to register", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
