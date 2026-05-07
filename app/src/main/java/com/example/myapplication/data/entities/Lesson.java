package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lessons")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String difficultyLevel;
    public String type;
    public String theoryText;

    public Lesson(String title, String description, String difficultyLevel, String type, String theoryText) {
        this.title = title;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.type = type;
        this.theoryText = theoryText;
    }
}
