package com.comp2100.surveymonster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.app.PendingIntent.getActivity;

/**
 * Register page enable user to register
 *
 */
public class RegisterPage extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button registerButton;
    FirebaseAuth firebaseAuth;

    /**
     * A big function for the REGISTER button and also filling email and password
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // set back bottom
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.tInputEmail);
        etPassword = findViewById(R.id.tInputPassword);


        registerButton = findViewById(R.id.RegButton);
        /**
         * Set the behaviour for REGISTER button
         */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                /**
                 * Check if any field is empty
                 */
                if (email.isEmpty()){
                    etEmail.setError("Please enter you email");
                    etEmail.requestFocus();
                } else if (password.isEmpty()){
                    etPassword.setError("Please enter you password");
                    etPassword.requestFocus();
                }
                else {
                    /**
                     * nonempty fields
                     */

                    ProgressBar mProgressBar =findViewById(R.id.rProgressBar);
                    mProgressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                /**
                                 * A registered email will fail the registration, switch to the login interface
                                 */
                                Toast.makeText(RegisterPage.this, "Already registered, please login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                            } else {
                                /**
                                 * Register success
                                 */
                                Toast.makeText(RegisterPage.this, "Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
