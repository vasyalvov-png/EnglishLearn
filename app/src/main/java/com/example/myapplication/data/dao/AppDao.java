package com.example.myapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.entities.DailyActivity;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.data.entities.Question;
import com.example.myapplication.data.entities.UserProgress;

import java.util.List;

@Dao
public interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLesson(Lesson lesson);

    @Query("SELECT * FROM lessons")
    LiveData<List<Lesson>> getAllLessons();

    @Query("SELECT * FROM lessons")
    List<Lesson> getAllLessonsSync();

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    LiveData<Lesson> getLessonById(int lessonId);

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    Lesson getLessonByIdSync(int lessonId);

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    LiveData<UserProgress> getUserProgress(int userId);

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    UserProgress getUserProgressSync(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserProgress(UserProgress userProgress);

    @Update
    void updateUserProgress(UserProgress userProgress);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    @Query("SELECT * FROM questions WHERE lessonId = :lessonId")
    LiveData<List<Question>> getQuestionsForLesson(int lessonId);

    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT 10")
    LiveData<List<Question>> getRandomQuestions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDailyActivity(DailyActivity activity);

    @Query("SELECT * FROM daily_activity ORDER BY date DESC LIMIT 30")
    LiveData<List<DailyActivity>> getRecentActivity();

    @Query("SELECT * FROM daily_activity WHERE date = :date")
    DailyActivity getDailyActivitySync(long date);
}
