package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.R;
import com.example.finalproject.models.Quiz;
import com.example.finalproject.models.Question;
import com.google.gson.Gson;

import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private Quiz quiz;
    private TextView txtScore, txtAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtScore = findViewById(R.id.txtScore);
        txtAnswer = findViewById(R.id.txtAnswer);

        setUpViews();
    }

    private void setUpViews() {
        String quizData = getIntent().getStringExtra("QUIZ");
        quiz = new Gson().fromJson(quizData, Quiz.class);
        calculateScore();
        setAnswerView();
    }

    private void setAnswerView() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Question> entry : quiz.getQuestions().entrySet()) {
            Question question = entry.getValue();
            builder.append("<font color='#18206F'><b>Question: ").append(question.getDescription()).append("</b></font><br/><br/>");
            builder.append("<font color='#009688'>Answer: ").append(question.getAnswer()).append("</font><br/><br/>");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAnswer.setText(Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtAnswer.setText(Html.fromHtml(builder.toString()));
        }
    }

    private void calculateScore() {
        int score = 0;

        for (Map.Entry<String, Question> entry : quiz.getQuestions().entrySet()) {
            Question question = entry.getValue();
            if (question.getAnswer().equals(question.getUserAnswer())) {
                score += 10;
            }
        }

        // Hiển thị điểm
        txtScore.setText("Your Score: " + score);
    }
}
