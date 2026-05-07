package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int lessonId;
    public String text;
    public String type;
    public String options; // Comma-separated options
    public String correctAnswer;

    public Question(int lessonId, String text, String type, String options, String correctAnswer) {
        this.lessonId = lessonId;
        this.text = text;
        this.type = type;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}
