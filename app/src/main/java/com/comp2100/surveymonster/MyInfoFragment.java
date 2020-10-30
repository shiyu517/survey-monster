package com.comp2100.surveymonster;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


/**
 * Page contains user information
 * 1. show the current login state:
 * 2. by click my survey bottom, user can view all the surveys he/she created
 *
 */
public class MyInfoFragment extends Fragment {
    private FirebaseAuth mAuth;

    CardView cardView;
    TextView tvUsername;
    TextView tvLogOut;

    public MyInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_my_info, container, false);

        //Extract items
        tvUsername = rootView.findViewById(R.id.myUsername);
        tvLogOut = rootView.findViewById(R.id.statusIndicater);
        cardView = rootView.findViewById(R.id.cardViewMe);

        // get authentication info and state
        mAuth = FirebaseAuth.getInstance();

        // check authentication state and change interface base on the authentication state
        FirebaseUser user = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() != null){
            String username = usernameFromEmail(user.getEmail());
            tvUsername.setText(username);
            tvLogOut.setText("Log out");
        } else {
            tvUsername.setText("You need to login");
            tvLogOut.setText("Click to login");
        }

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // set activity to show all the surveys that the user posts
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser()==null){
                    Toast.makeText(getActivity(), "Please login to see your surveys", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), MySurvey.class);
                    startActivity(intent);
                }
            }
        });


        // set logout bottom enable user to logout
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvLogOut.getText() == "Log out") {
                    FirebaseAuth.getInstance().signOut();
                    tvUsername.setText("You need to login");
                    tvLogOut.setText("Click to login");
                } else {
                    if (tvLogOut.getText() == "Click to login") {
                        String key = "success";
                        Intent intent = new Intent(getActivity(), LoginPage.class);
                        startActivityForResult(intent, 0);
                    }
                }
            }
        });
    }


    // if user login successfully, modify interface to show user information
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            FirebaseUser user = mAuth.getCurrentUser();
            String username = usernameFromEmail(user.getEmail());
            tvUsername.setText(username);
            tvLogOut.setText("Log out");
        }
    }

    // extract user name from email string
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}
