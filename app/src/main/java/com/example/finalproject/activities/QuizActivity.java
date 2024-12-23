package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.finalproject.R;
import com.example.finalproject.adapters.OptionAdapter;
import com.example.finalproject.models.Question;
import com.example.finalproject.models.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {
    RecyclerView quiz_recycler;
    TextView description;
    FirebaseFirestore firestore;

    // Quiz and Questions
    ArrayList<Quiz> quizzes = new ArrayList<>();
    HashMap<String, Question> quesions = new HashMap<>();
    int index = 1;

    Button submit;
    Button previous;
    Button next;

    Question question = new Question();
    // Shows Questions
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        // Got The recycler view
        quiz_recycler = findViewById(R.id.question_recycler);
        description = findViewById(R.id.quiz_description);
        firestore = FirebaseFirestore.getInstance();
        // Buttons found
        submit = findViewById(R.id.submit_btn);
        previous = findViewById(R.id.prev_btn);
        next = findViewById(R.id.next_btn);

        submit.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        previous.setVisibility(View.GONE);
//        setUpView();
        setUpFireStore();
        setUpEventListner();


    }
    public void setUpEventListner(){
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                index++;
                setUpView();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                index--;
                setUpView();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO
                 * 1) make 2 activity layouts -> results and logout
                 *  */
                Log.d("DATA","FINAL RESULTS : "+ quesions);
                Log.d("DATA","FINAL RESULTS : "+ quizzes.get(0));

                Gson gson = new Gson();
                String data = gson.toJson(quizzes.get(0));
                Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                intent.putExtra("QUIZ",data);
                startActivity(intent);
                finish();

            }
        });
    }
    public void setUpFireStore() {
        Intent intent = getIntent();
        String date = intent.getStringExtra("DATE");
        if(date != null){
            firestore.collection("quizzes").whereEqualTo("title",date)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.R)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d("DATA","In onComplete");
                            if(task.isSuccessful()){
                                if(task.getResult() != null && !task.getResult().isEmpty()){
                                    Log.d("DATA",""+task.getResult().toObjects(Quiz.class).toString());
                                    quizzes = (ArrayList<Quiz>) task.getResult().toObjects(Quiz.class);
                                    quesions = (HashMap<String, Question>) quizzes.get(0).getQuestions();

                                    Log.d("DATA","Hello array"+quizzes);
                                    Log.d("DATA","QUESTIONS "+ quesions.get("question1"));
                                    setUpView();
                                }else{
                                    Log.d("DATA","No Data");
                                }


                            }
                        }
                    });
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setUpView(){

        if(quesions.size() != 0 || quesions != null) {

            if (index == 1 && index == quesions.size()) {
                submit.setVisibility(View.VISIBLE);
            } else if (index == 1 && quesions.size() > 1) {
                next.setVisibility(View.VISIBLE);
            } else if (index == quesions.size()) {
                previous.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            } else {
                previous.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            }

            question = quesions.get("question"+index);
            Log.d("DATA", "Hello DAta" + question);
            Log.d("DATA", "Hello DAta" + quesions.size());
        }
        setUpRecycler();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void setUpRecycler() {
        if(question != null) {
            description.setText(question.getDescription());
            quiz_recycler.setLayoutManager(new LinearLayoutManager(this));
            quiz_recycler.setAdapter(new OptionAdapter(QuizActivity.this, question));

            quiz_recycler.setHasFixedSize(true);
        }
    }
}