package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityScoreBinding;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    ActivityScoreBinding binding;
    private long timeTaken;
    private int totalQuestions, correctAnswer, wrongAnswer, skipQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        totalQuestions = getIntent().getIntExtra("total_questions", 0);
        correctAnswer = getIntent().getIntExtra("correct", 0);
        wrongAnswer = getIntent().getIntExtra("wrong", 0);

        String remainingTime = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));


        skipQuestion = totalQuestions - (correctAnswer + wrongAnswer);
        binding.timeTaken.setText(remainingTime);
        binding.correctAnswers.setText(String.valueOf(correctAnswer));
        binding.wrongAnswers.setText(String.valueOf(wrongAnswer));
        binding.totalQuestions.setText(String.valueOf(totalQuestions));
        binding.skipQuestion.setText(String.valueOf(skipQuestion));
        binding.yourScore.setText(String.valueOf(correctAnswer));

        binding.btnReAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnReturnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}