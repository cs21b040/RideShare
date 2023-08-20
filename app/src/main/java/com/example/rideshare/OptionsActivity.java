package com.example.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class OptionsActivity extends AppCompatActivity {
    private Button newride;
    private Button shareride;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        newride=findViewById(R.id.newride);
        shareride=findViewById(R.id.shareride);
        auth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        String userId= auth.getCurrentUser().getUid();
        DocumentReference documentReference= fstore.collection("users").document(userId);
        newride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String dln= documentSnapshot.getString("dlno");
                            String vehiclenumber= documentSnapshot.getString("vehiclenumber");
                            if(!Objects.equals(dln, "") && !Objects.equals(vehiclenumber, "")){
                                startActivity(new Intent(OptionsActivity.this, HomePage.class));
                            }
                            else{
                                startActivity(new Intent(OptionsActivity.this,ProfileInfo.class));
                                finish();
                            }
                        }
                    }
                });

            }
        });
        shareride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this,CustomerHomePage.class));
            }
        });
    }
}