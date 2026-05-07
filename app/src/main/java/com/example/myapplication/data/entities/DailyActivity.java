package com.example.myapplication.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_activity")
public class DailyActivity {
    @PrimaryKey
    public long date; // Midnight timestamp in millis
    public int xpEarned;

    public DailyActivity(long date, int xpEarned) {
        this.date = date;
        this.xpEarned = xpEarned;
    }
}
