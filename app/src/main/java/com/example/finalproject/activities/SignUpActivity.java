package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextNewEmail, editTextNewPassWord, editTextConfirmPassword;
    private TextView textViewSignIn;
    Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        editTextNewEmail = (EditText) findViewById(R.id.editText_CreateEmail);
        editTextNewPassWord = (EditText) findViewById(R.id.editText_CreatePassWord);
        editTextConfirmPassword = (EditText) findViewById(R.id.editText_ConfirmPassword);
        buttonCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        buttonCreateAccount.setOnClickListener(view -> {
            signUpUser();
        });

        textViewSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signUpUser() {
        String email = editTextNewEmail.getText().toString();
        String password = editTextNewPassWord.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if(email.isBlank() || password.isBlank() || confirmPassword.isBlank())  {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}