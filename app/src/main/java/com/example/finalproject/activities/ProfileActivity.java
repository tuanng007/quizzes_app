package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    TextView userEmail;
    FirebaseAuth auth;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userEmail = (TextView) findViewById(R.id.userEmail);
        auth = FirebaseAuth.getInstance();
        userEmail.setText(""+auth.getCurrentUser().getEmail());


    }

}