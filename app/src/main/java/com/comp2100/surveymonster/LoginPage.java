package com.comp2100.surveymonster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.comp2100.surveymonster.DateStorage.User;
import com.comp2100.surveymonster.databinding.ActivityLoginPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Login page:
 *
 * Allow user login;
 * Set bottom which shift to register page for user to register
 *
 */
public class LoginPage extends AppCompatActivity implements View.OnClickListener {


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ActivityLoginPageBinding binding;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // get firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mProgressBar = findViewById(R.id.progressBar);

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    public void onClick(View v) {
        if (!validateForm()) {
            return;
        }

        // indicator that indicates the bottom is pressed successfully
        mProgressBar.setVisibility(View.VISIBLE);

        String email = binding.tInputEmail.getText().toString();
        String password = binding.tInputPassword.getText().toString();

        // login through firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                        mProgressBar.setVisibility(View.INVISIBLE);

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginPage.this, "Sign In Failed please register or go back",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    // back to main page if the authentication success
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        Intent intent=new Intent();

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        Toast.makeText(LoginPage.this, "Already login", Toast.LENGTH_SHORT).show();
        setResult(1, intent);
        finish();
    }

    // upload new user to database
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }


    // extract username
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // check whether the information is complete
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.tInputEmail.getText().toString())) {
            binding.tInputEmail.setError("Required");
            result = false;
        } else {
            binding.tInputEmail.setError(null);
        }

        if (TextUtils.isEmpty(binding.tInputPassword.getText().toString())) {
            binding.tInputPassword.setError("Required");
            result = false;
        } else {
            binding.tInputPassword.setError(null);
        }

        return result;
    }


    public void toRegister(View view){
        Intent intent = new Intent(this, RegisterPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
