package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {
    private TextView textView;
    private EditText username;
    private EditText password;
    private Button signin;
    private Button signinwithgoogle;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    private GoogleSignInClient mGoogleSignInClient;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        textView=findViewById(R.id.textView);
        signin=findViewById(R.id.signin);
        signinwithgoogle=findViewById(R.id.signinwithgoogle);
        username=findViewById(R.id.email);
        password=findViewById(R.id.password);
        auth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

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
                if(useremail.isEmpty()){
                    username.setError("Please Enter Email");
                    username.requestFocus();
                    return;
                }
                if(userpassword.isEmpty()){
                    password.setError("Please Enter Password");
                    password.requestFocus();
                    return;
                }

                auth.signInWithEmailAndPassword(useremail,userpassword).addOnSuccessListener(LogInActivity.this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        finish();
                        startActivity(new Intent(LogInActivity.this, OptionsActivity.class));
                    }
                }).addOnFailureListener(LogInActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            // This exception is thrown if the account does not exist.
                            Toast.makeText(LogInActivity.this, "Account not found. Please sign up.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogInActivity.this, "Sign In Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        signinwithgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                finish();
                startActivity(new Intent(LogInActivity.this, OptionsActivity.class));
            }
            catch (ApiException e) {
                if (e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_REQUIRED) {
                    Toast.makeText(this, "Google Sign In: Account not found.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Failed to Sign In: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                    Log.e("GoogleSignInError", "Status code: " + e.getStatusCode(), e);
                }
            }
        }
    }

}