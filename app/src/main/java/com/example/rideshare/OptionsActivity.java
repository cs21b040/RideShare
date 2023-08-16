package com.example.rideshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class OptionsActivity extends AppCompatActivity {
    private Button newride;
    private Button shareride;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_options);
        newride=findViewById(R.id.newride);
        shareride=findViewById(R.id.shareride);
        String userid= auth.getCurrentUser().getUid();
        DocumentReference documentReference= fstore.collection("users").document(userid);

        newride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(documentReference);
            }
        });
        shareride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this,HomePage.class));
            }
        });
    }
    public void check (DocumentReference documentReference){
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String s= value.getString("dlno");
                if(s==""){
                    startActivity(new Intent(OptionsActivity.this,ProfileInfo.class));
                }
                else startActivity(new Intent(OptionsActivity.this,HomePage.class));
            }
        });
    }
}