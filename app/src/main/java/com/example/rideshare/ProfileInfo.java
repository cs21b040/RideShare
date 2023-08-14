package com.example.rideshare;

import static android.content.Intent.createChooser;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileInfo extends AppCompatActivity {
    private EditText dob;
    private EditText pno;
    private EditText vehicletype;
    private EditText vehiclenumber;
    private Button browse;
    private Button saving;
    private TextView uploadlicense;
    private TextView uploadedornot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        dob=findViewById(R.id.dob);
        pno=findViewById(R.id.pno);
        vehicletype=findViewById(R.id.vehicletype);
        vehiclenumber=findViewById(R.id.vehiclenumber);
        browse=findViewById(R.id.browse);
        saving=findViewById(R.id.saving);
        uploadlicense=findViewById(R.id.uploadlicense);
        uploadedornot=findViewById(R.id.uploadedornot);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileInfo.this,HomePage.class));
            }
        });
    }

    private void selectImage() {
//        Intent intent=new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
    }
}