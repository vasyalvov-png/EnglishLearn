package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String iconEmoji;
    public boolean isUnlocked;
    public int requirementValue;
    public String type; // "XP", "STREAK", "LESSONS"

    public Achievement(String title, String description, String iconEmoji, int requirementValue, String type) {
        this.title = title;
        this.description = description;
        this.iconEmoji = iconEmoji;
        this.requirementValue = requirementValue;
        this.type = type;
        this.isUnlocked = false;
    }
}
