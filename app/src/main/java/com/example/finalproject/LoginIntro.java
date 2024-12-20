package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginIntro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_intro);
        mAuth = FirebaseAuth.getInstance();
        buttonStart = (Button) findViewById(R.id.btnStart);

        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, "User is already logged in!", Toast.LENGTH_SHORT).show();
            redirect("MAIN");
        }

        buttonStart.setOnClickListener(view -> {
            redirect("LOGIN");
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_intro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void redirect(String name) {
        Intent intent;
        if (name.equals("LOGIN")) {
           intent = new Intent(LoginIntro.this, LoginActivity.class);
           startActivity(intent);
           finish();
        } else if (name.equals("MAIN")) {
           intent =  new Intent(LoginIntro.this, MainActivity.class);
           startActivity(intent);
           finish();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
