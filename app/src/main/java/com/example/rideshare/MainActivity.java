package com.example.rideshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView wel,learning;
    private static int Splash_timeout=3500;
    public static boolean hasLoggedIn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wel=findViewById(R.id.textview1);
        learning=findViewById(R.id.textview2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("HelloHi", "run: "+hasLoggedIn);
                SharedPreferences sharedPreferences =getSharedPreferences(LogInActivity.PREFS_NAME,0);
                hasLoggedIn=sharedPreferences.getBoolean("hasLoggedIn",false);

                if(hasLoggedIn!=false)
                {
                    Toast.makeText(MainActivity.this, "hasLoggedIn = true", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(MainActivity.this,OptionsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {

                    Toast.makeText(MainActivity.this, "HasLoggedIn = false", Toast.LENGTH_SHORT).show();
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
