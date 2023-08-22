package com.example.rideshare;

import static com.example.rideshare.OptionsActivity.isDriver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class Travel_Details extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    private TextView traveler, from1, to1, phone1, from2, to2, phone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_details);
        auth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        traveler= findViewById(R.id.traveler);
        from1= findViewById(R.id.from1);
        to1= findViewById(R.id.to1);
        phone1= findViewById(R.id.phone1);
        from2= findViewById(R.id.from2);
        to2= findViewById(R.id.to2);
        phone2= findViewById(R.id.phone2);
        String userId= auth.getCurrentUser().getUid();

        if(isDriver) {
            traveler.setText("Travelling as : Driver");


        }
        else {
            traveler.setText("Travelling as : Passenger");
        }

    }
}