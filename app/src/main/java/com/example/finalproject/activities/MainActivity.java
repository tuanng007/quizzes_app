package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.finalproject.R;
import com.example.finalproject.adapters.QuizAdapter;
import com.example.finalproject.models.Quiz;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar appbar;

    ActionBarDrawerToggle action_toggle;
    DrawerLayout drawer;
    NavigationView nav_view;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    ArrayList<Quiz> quiz_data = new ArrayList<>();
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        appbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        recyclerView = (RecyclerView) findViewById(R.id.quiz_recycler);
        fab = (FloatingActionButton) findViewById(R.id.btn_date_picker);
        nav_view = (NavigationView) findViewById(R.id.nav_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
//        populateDummyData();
        setUpViews();

    }
    // Populating Dummy data
    public void populateDummyData(){
        quiz_data.add(new Quiz("10/10/2021","10/10/2021"));
        quiz_data.add(new Quiz("10/10/2021","10/10/2021"));
        quiz_data.add(new Quiz("10/10/2021","10/10/2021"));
        quiz_data.add(new Quiz("10/10/2021","10/10/2021"));
        quiz_data.add(new Quiz("10/10/2021","10/10/2021"));
        quiz_data.add(new Quiz("10/10/2021","10/10/2021"));
    }
    public void setUpViews(){
        // First View
        setUpDrawerLayout();
        // second view
        setUpRecycler();
        //
        setUpFireStore();
        // setting up datePicker
        //  setUpDatePicker();
    }
//    public void setUpDatePicker(){
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
//                datePicker.show(getSupportFragmentManager(),"DataPicker");
//                // Listener for Pressing ok
//                datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener) selection -> {
//                    Log.d("DATE",selection.toString());
//                    Log.d("DATE",datePicker.getHeaderText());
//                    SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy");
//
//                    String date = dateFormater.format(new Date((Long) selection));
//
//                    Log.d("DATE","DATE FORMAT"+date);
//                    Intent intent = new Intent(MainActivity.this,QuizActivity.class);
//                    intent.putExtra("DATE",date);
//                    startActivity(intent);
//                });
//
//                // Cancel button Pressed Of DatePicker
//                datePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("DATE",datePicker.getHeaderText());
//                    }
//                });
//
//            }
//        });
//    }
    public void setUpFireStore() {
        CollectionReference collection = firebaseFirestore.collection("quizzes");
        collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot value, FirebaseFirestoreException error) {
                if(value == null || error != null){
                    Toast.makeText(MainActivity.this,"Error in Fetching Data",Toast.LENGTH_SHORT).show();
                    return;
                }
//                HashMap<String,Question> test = (HashMap<String,Question>) value.toObjects(Quiz.class).get(0).questions;
//
//                Log.d("TAG",""+test.toString());
                Log.d("TAG",value.toObjects(Quiz.class).get(0).questions.toString());
                Log.d("TAG",""+value.toObjects(Quiz.class).toString());
                quiz_data.clear();
                quiz_data.addAll(value.toObjects(Quiz.class));

                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void setUpRecycler(){
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new QuizAdapter(this,quiz_data));
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    public void setUpDrawerLayout(){
        // setting action Bar
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                drawer.closeDrawers();
                return true;
            }
        });
        setSupportActionBar(appbar);
        // Adding 'Toggle'
        action_toggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawer,
                R.string.app_name,
                R.string.app_name
        );
    }
    // For toggling 'override'

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        action_toggle.onOptionsItemSelected(item);
        return true;
    }
}