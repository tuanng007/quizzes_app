package com.example.finalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalproject.R;
import com.example.finalproject.adapters.OptionAdapter;
import com.example.finalproject.models.Question;
import com.example.finalproject.models.Quiz;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private List<Quiz> quizzes;
    private Map<String, Question> questions;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        setUpFirestore();
        setUpEventListener();
    }

    private void setUpEventListener() {
        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            index--;
            bindViews();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            index++;
            bindViews();
        });

        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            Log.d("FINALQUIZ", questions.toString());

            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            String json = new Gson().toJson(quizzes.get(0));  // Convert the first quiz to JSON
            intent.putExtra("QUIZ", json);
            startActivity(intent);
        });
    }

    private void setUpFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String date = getIntent().getStringExtra("DATE");
        if (date != null) {
            firestore.collection("quizzes")
                    .whereEqualTo("title", date)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            quizzes = queryDocumentSnapshots.toObjects(Quiz.class);
                            questions = quizzes.get(0).getQuestions();  // Get questions from the first quiz
                            bindViews();
                        }
                    });
        }
    }

    private void bindViews() {
        findViewById(R.id.btnPrevious).setVisibility(View.GONE);
        findViewById(R.id.btnSubmit).setVisibility(View.GONE);
        findViewById(R.id.btnNext).setVisibility(View.GONE);

        if (index == 1) { // First question
            findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
        } else if (index == questions.size()) { // Last question
            findViewById(R.id.btnSubmit).setVisibility(View.VISIBLE);
            findViewById(R.id.btnPrevious).setVisibility(View.VISIBLE);
        } else { // Middle questions
            findViewById(R.id.btnPrevious).setVisibility(View.VISIBLE);
            findViewById(R.id.btnNext).setVisibility(View.VISIBLE);
        }

        Question question = questions.get("question" + index);
        if (question != null) {
            ((TextView) findViewById(R.id.description)).setText(question.getDescription());

            OptionAdapter optionAdapter = new OptionAdapter(this, question);
            RecyclerView optionList = findViewById(R.id.optionList);
            optionList.setLayoutManager(new LinearLayoutManager(this));
            optionList.setAdapter(optionAdapter);
            optionList.setHasFixedSize(true);
        }
    }
}
