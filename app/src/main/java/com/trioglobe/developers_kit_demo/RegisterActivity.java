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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText EmailAddress,Password;
    Button register;
    TextView textView;
    user user1;
    DatabaseReference db;
    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
            if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EmailAddress = findViewById(R.id.editTextTextEmailAddress);
        Password = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.register);
        textView = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = EmailAddress.getText().toString();
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
                    Toast.makeText(RegisterActivity.this,"Fields are empty",Toast.LENGTH_SHORT).show();
                }
                else  if (!(email.isEmpty() && pass.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(email , pass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        // Sign in: success
                                        // update UI for current User
                                        final FirebaseUser user = mAuth.getCurrentUser();


                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            String id = mAuth.getCurrentUser().getUid();
                                                            user1 = new user();
                                                            user1.setId(id);
                                                            db.child("users").child(id).setValue(user1);
                                                            Toast.makeText(RegisterActivity.this,"Please Check your Email!!!",Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                                            startActivity(i);
                                                        }

                                                    }
                                                });
                                        updateUI(user);

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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(RegisterActivity.this, LoginActivity.class);
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
