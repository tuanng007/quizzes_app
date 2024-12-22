package com.example.finalproject.activities;

import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapters.QuizAdapter;
import com.example.finalproject.models.Question;
import com.example.finalproject.models.Quiz;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private QuizAdapter adapter;
    List<Quiz> quizList = new ArrayList<>();
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setUpViews();
        populateQuizzes();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainDrawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void setUpViews() {
       setUpFireStore();
        setUpDrawerLayout();
        setUpRecyclerView();
        // setUpDatePicker();
    }

//    private void setUpDatePicker() {
//        findViewById(R.id.btnDatePicker).setOnClickListener(view -> {
//            MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().build();
//            datePicker.show(getSupportFragmentManager(), "DatePicker");
//            datePicker.addOnPositiveButtonClickListener(selection -> {
//                Log.d("DATEPICKER", datePicker.getHeaderText());
//                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
//                String date = dateFormatter.format(new Date((Long) selection));
//                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
//                intent.putExtra("DATE", date);
//                startActivity(intent);
//            });
//            datePicker.addOnNegativeButtonClickListener(dialog -> {
//                Log.d("DATEPICKER", datePicker.getHeaderText());
//            });
//            datePicker.addOnCancelListener(dialog -> {
//                Log.d("DATEPICKER", "Date Picker Cancelled");
//            });
//        });
//    }

    private void setUpFireStore() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("quizzes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Question> questions = queryDocumentSnapshots.toObjects(Question.class);
                    // Process your questions here

                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Toast.makeText(this, "Firebase Error", Toast.LENGTH_SHORT).show();
    });
    }

    private void populateQuizzes() {
        quizList.add(new Quiz("123", "12341"));
        quizList.add(new Quiz("123", "12341"));

        quizList.add(new Quiz("123", "12341"));

        quizList.add(new Quiz("123", "12341"));

    }


    private void setUpRecyclerView() {
        adapter = new QuizAdapter(this, quizList);
        RecyclerView quizRecyclerView = findViewById(R.id.quizRecyclerView);
        quizRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        quizRecyclerView.setAdapter(adapter);
    }

    private void setUpDrawerLayout() {
        setSupportActionBar(findViewById(R.id.appBar));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, findViewById(R.id.mainDrawer), R.string.open_drawer, R.string.close_drawer);
        actionBarDrawerToggle.syncState();
//        NavigationView navigationView = findViewById(R.id.navigation);
//        navigationView.setNavigationItemSelectedListener(item -> {
//            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            findViewById(R.id.mainDrawer).closeDrawers();
//            return true;
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
