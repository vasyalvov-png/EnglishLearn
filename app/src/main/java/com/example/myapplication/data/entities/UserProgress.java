package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey
    public int userId;
    public int xp;
    public int dailyStreak;
    public long lastLessonDate;

    public UserProgress(int userId, int xp, int dailyStreak, long lastLessonDate) {
        this.userId = userId;
        this.xp = xp;
        this.dailyStreak = dailyStreak;
        this.lastLessonDate = lastLessonDate;
    }
}
