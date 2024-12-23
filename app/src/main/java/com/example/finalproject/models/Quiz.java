package com.example.finalproject.models;

import java.util.HashMap;

public class Quiz {
    public String id ="";
    public String title = "";
    public HashMap<String,Question> questions = new HashMap<String,Question>();

    public Quiz(){}
    // Constructor
    public  Quiz( String id, String title){
        this.id = id;
        this.title = title;
    }
    public Quiz( String id, String title,HashMap<String,Question> questions){
        this.id = id;
        this.title = title;
        this.questions = questions;

    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", questions=" + questions +
                '}';
    }
}