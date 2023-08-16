package com.example.rideshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView wel,learning;
    private static int Splash_timeout=3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wel=findViewById(R.id.textview1);
        learning=findViewById(R.id.textview2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences =getSharedPreferences(LogInActivity.PREFS_NAME,0);
                boolean hasLoggedIn=sharedPreferences.getBoolean("hasLoggedIn",false);

                if(hasLoggedIn)
                {
                    Intent intent =new Intent(MainActivity.this,OptionsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent splashintent=new Intent(MainActivity.this, AnimationActivity.class);
                    startActivity(splashintent);
                    finish();
                }
            }
        },Splash_timeout);
        Animation myanimation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.animat2);
        wel.startAnimation(myanimation);
        Animation myanimation2= AnimationUtils.loadAnimation(MainActivity.this,R.anim.animat1);
        learning.startAnimation(myanimation2);
    }
}
