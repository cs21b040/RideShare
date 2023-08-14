package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private TextView textView;
    private EditText username;
    private EditText password;
    private Button signin;
    private Button signinwithgoogle;
    private Button signinwithfb;
    private FirebaseAuth auth;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        textView=findViewById(R.id.textView);
        signin=findViewById(R.id.signin);
        signinwithgoogle=findViewById(R.id.signinwithgoogle);
        signinwithfb=findViewById(R.id.signinwithfb);
        username=findViewById(R.id.email);
        password=findViewById(R.id.password);
        auth= FirebaseAuth.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useremail= username.getText().toString();
                String userpassword= password.getText().toString();
                auth.signInWithEmailAndPassword(useremail,userpassword).addOnSuccessListener(LogInActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(LogInActivity.this, OptionsActivity.class));
                        finish();
                    }
                }).addOnFailureListener(LogInActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogInActivity.this, "SignIn Failed.. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signinwithgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, OptionsActivity.class));
            }
        });
        signinwithfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, OptionsActivity.class));
            }
        });
    }
}