package com.trioglobe.developers_kit_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText EmailAddress,Password;
    Button login;
    TextView textViewsignup;

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EmailAddress = findViewById(R.id.editTextTextEmailAddress);
        Password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.login);
        textViewsignup = findViewById(R.id.textViewsignup);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String email = EmailAddress.getText().toString();
                String pass = Password.getText().toString();
                
                if (email.isEmpty()){
                    EmailAddress.setError("Please Enter Email id");
                    EmailAddress.requestFocus();
                }
                else if (pass.isEmpty()){
                    Password.setError("Please Enter Email id");
                    Password.requestFocus();
                }
                else if (email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else  if (!(email.isEmpty() && pass.isEmpty())){
                    mAuth.signInWithEmailAndPassword(email , pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        // Sign in: success
                                        // update UI for current User
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        if(mAuth.getCurrentUser().isEmailVerified()) {


                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    } else {
                                        // Sign in: fail
                                        Toast.makeText(getApplicationContext(),"Authentication failed!",Toast.LENGTH_SHORT).show();

                                        updateUI(null);
                                    }
                                }
                            }) ;

                }
            }
        });
textViewsignup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent( LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }
});



    }
    public void onStart() {
        // ...
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

    }
}