package com.example.finalproject.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityQuestionsBinding;
import com.example.finalproject.models.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    ActivityQuestionsBinding binding;
    FirebaseDatabase database;
    ArrayList<Question> list;

    private int count = 0;
    private int position = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final long questionTime = 10;
    private long timeLeft;
    private String catId, subCatId;
    Dialog loadingDialog;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        catId = getIntent().getStringExtra("catId");
        subCatId = getIntent().getStringExtra("subCatId");

        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        startTimer();

        database.getReference().child("categories").child(catId).child("subCategories")
                .child(subCatId).child("questions").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Question model = dataSnapshot.getValue(Question.class);
                                model.setKey(dataSnapshot.getKey());
                                list.add(model);

                                loadingDialog.dismiss();
                            }
                            if (!list.isEmpty()) {
                                for (int i = 0; i < 4;i++) {
                                    binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAnswer((Button)view);
                                        }
                                    });
                                }

                                playAnimation(binding.textQuestion, 0, list.get(position).getQuestion());

                                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        enabledOption(true);

                                        position++;

                                        if (position == list.size()) {
                                            long totalTime = questionTime * 60 * 100;
                                            Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                            intent.putExtra("time_taken", totalTime-timeLeft);
                                            intent.putExtra("correct", correctAnswer);
                                            intent.putExtra("wrong", wrongAnswer);
                                            intent.putExtra("total_questions", list.size());
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                        count = 0;
                                        playAnimation(binding.textQuestion, 0, list.get(position).getQuestion());
                                    }
                                });

                                binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        timer.cancel();

                                        long totalTime = questionTime * 60 * 100;
                                        Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                                        intent.putExtra("time_taken", totalTime-timeLeft);
                                        intent.putExtra("correct", correctAnswer);
                                        intent.putExtra("wrong", wrongAnswer);
                                        intent.putExtra("total_questions", list.size());
                                        startActivity(intent);
                                    }
                                });
                            }

                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(QuestionsActivity.this, "questions not exist", Toast.LENGTH_SHORT).show();
                        }
                    }    Button logout_button;


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void enabledOption(boolean enable) {
        for(int i = 0; i < 4;i++) {
            binding.optionContainer.getChildAt(i).setEnabled(enable);
            if(enable) {
                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_second);
            }
        }
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleX(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                        if (value == 0 && count < 4) {
                            String option = "";

                            if(count == 0) {
                                option = list.get(position).getOptionA();
                            } else if (count == 1) {
                                option = list.get(position).getOptionB();
                            } else if (count == 2) {
                                option = list.get(position).getOptionC();
                            } else if (count == 3) {
                                option = list.get(position).getOptionD();
                            }

                            playAnimation(binding.optionContainer.getChildAt(count), 0, option);
                            count++;
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        if (value == 0) {
                            try {
                                ((TextView)view).setText(data);
                                binding.questionCount.setText(position+1+"/"+list.size());
                            }catch (Exception e) {
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view, 1, data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });
    }

    private void checkAnswer(Button selectedOption) {
        enabledOption(false);
        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())) {
            correctAnswer++;
            selectedOption.setBackgroundResource(R.drawable.correct_option);
        } else  {
            wrongAnswer++;
            selectedOption.setBackgroundResource(R.drawable.wrong_option);

            Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOption.setBackgroundResource(R.drawable.correct_option);

        }
    }

    private void startTimer() {
        long time = questionTime * 60 * 100;
         timer = new CountDownTimer(time + 1000, 1000 ) {
             @Override
             public void onTick(long l) {
                 timeLeft = l;
                 String remainingTime = String.format("%02d:%02d min",
                         TimeUnit.MILLISECONDS.toMinutes(l),
                         TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                 binding.timer.setText(remainingTime);
             }

             @Override
             public void onFinish() {
                 long totalTime = questionTime * 60 * 100;
                 Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                 intent.putExtra("time_taken", totalTime-timeLeft);
                 intent.putExtra("correct", correctAnswer);
                 intent.putExtra("wrong", wrongAnswer);
                 intent.putExtra("total_questions", list.size());
                 startActivity(intent);
                 QuestionsActivity.this.finish();
             }
         };
         timer.start();
    }
}