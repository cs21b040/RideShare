package com.example.rideshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class AnimationActivity extends AppCompatActivity {
    private Button logIn,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsta);
//        getSupportActionBar().show();
        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AnimationActivity.this,LogInActivity.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnimationActivity.this,SignUpActivity.class));
            }
        });
    }
}
