package com.example.myapplication.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.myapplication.data.entities.DailyActivity;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.data.entities.Question;
import com.example.myapplication.data.entities.UserProgress;
import java.util.List;

public class AppRepository {
    private final AppDatabase db;

    public AppRepository(Context context) {
        db = AppDatabase.getDatabase(context);
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return db.appDao().getAllLessons();
    }

    public LiveData<List<Question>> getQuestionsForLesson(int lessonId) {
        return db.appDao().getQuestionsForLesson(lessonId);
    }

    public LiveData<List<Question>> getRandomQuestions() {
        return db.appDao().getRandomQuestions();
    }

    public LiveData<UserProgress> getUserProgress(int userId) {
        return db.appDao().getUserProgress(userId);
    }

    public LiveData<List<DailyActivity>> getRecentActivity() {
        return db.appDao().getRecentActivity();
    }
    
    public AppDatabase getDatabase() {
        return db;
    }
}
