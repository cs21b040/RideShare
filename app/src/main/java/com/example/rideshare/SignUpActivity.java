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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private TextView textView;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button signup;
    private Button signupwithgoogle;
    private Button signupwithfb;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        textView=findViewById(R.id.textView);
        signup=findViewById(R.id.signup);
        signupwithgoogle=findViewById(R.id.signupwithgoogle);
        signupwithfb=findViewById(R.id.signupwithfb);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        auth= FirebaseAuth.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= name.getText().toString();
                String useremail= email.getText().toString();
                String userpassword= password.getText().toString();
                if (username.isEmpty() || useremail.isEmpty() || userpassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Invalid Credentials.. Please Try Again", Toast.LENGTH_SHORT).show();
                }
                else if(userpassword.length()<6) {
                    Toast.makeText(SignUpActivity.this, "Password should be atleast 6 characters long", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, OptionsActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed.. Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        signupwithgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, OptionsActivity.class));
            }
        });
        signupwithfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, OptionsActivity.class));
            }
        });
    }
}