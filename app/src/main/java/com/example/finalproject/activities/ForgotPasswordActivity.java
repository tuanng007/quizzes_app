package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText editTextEmailReset;
    Button buttonReset;
    Button buttonBack;
    FirebaseAuth mAuth;
    private String emailReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        editTextEmailReset = (EditText) findViewById(R.id.editTextEmailReset);
        buttonReset = (Button) findViewById(R.id.btnReset);
        buttonBack = (Button) findViewById(R.id.btnBack);
        mAuth = FirebaseAuth.getInstance();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 emailReset = editTextEmailReset.getText().toString();
                 if(!TextUtils.isEmpty(emailReset)) {
                     resetPassword();
                 } else {
                     Toast.makeText(ForgotPasswordActivity.this, "Email field can't be empty", Toast.LENGTH_SHORT).show();
                 }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_password), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void resetPassword() {
        mAuth.sendPasswordResetEmail(emailReset)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ForgotPasswordActivity.this, "Reset Password link has been sent to your registered Email!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}