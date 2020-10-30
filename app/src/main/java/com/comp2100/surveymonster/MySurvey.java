package com.comp2100.surveymonster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.comp2100.surveymonster.DateStorage.Survey;
import com.comp2100.surveymonster.databinding.ActivityMySurveyBinding;
import com.comp2100.surveymonster.fragment.MySurveysFragment;
import com.comp2100.surveymonster.viewholder.SurveyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class MySurvey extends AppCompatActivity {
    private static final String TAG = "PostListFragment";


    // [START define_database_reference]
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Survey, SurveyViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMySurveyBinding binding = ActivityMySurveyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Create the my survey adapter
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return new MySurveysFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "My Survey";
            }
        };

        // Set up the ViewPager with the sections adapter.
        binding.mcontainer.setAdapter(mPagerAdapter);

    }

    // enable deletion
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // set up back bottom
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
