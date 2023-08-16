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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private TextView textView;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText pno;
    private EditText aadhaarnumber;
    private Button signup;
    private Button signupwithgoogle;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        textView=findViewById(R.id.textView);
        signup=findViewById(R.id.signup);
        signupwithgoogle=findViewById(R.id.signupwithgoogle);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        pno=findViewById(R.id.pno);
        aadhaarnumber=findViewById(R.id.aadhaarnumber);
        auth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();

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
                String userpno= pno.getText().toString();
                String useraadhaarnumber= aadhaarnumber.getText().toString();

                if (username.isEmpty() || useremail.isEmpty() || userpassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                HashMap<String, Object> user = new HashMap<>();
                                String userId= auth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(userId);

                                user.put("name", username);
                                user.put("email", useremail);
                                user.put("password", userpassword);
                                user.put("pno", userpno);
                                user.put("aadhaarnumber", useraadhaarnumber);
                                user.put("dob", "");
                                user.put("vehicletype", "");
                                user.put("vehiclenumber", "");
                                user.put("dlno", "");

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "Data Stored", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                
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
    }
}