package com.example.finalproject.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewForgotPassword;
    TextView textViewSignUp;
    Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        buttonLogin.setOnClickListener(view -> {
            loadingDialog.show();
            loginUser();
        });

        textViewSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        textViewForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });

    }

}