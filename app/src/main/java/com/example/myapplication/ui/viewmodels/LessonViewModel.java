package com.example.myapplication.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.AppRepository;
import com.example.myapplication.data.entities.DailyActivity;
import com.example.myapplication.data.entities.Question;
import com.example.myapplication.data.entities.UserProgress;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class LessonViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private LiveData<List<Question>> questions;
    private final MutableLiveData<Integer> currentQuestionIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> lessonCompleted = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> xpEarned = new MutableLiveData<>(0);
    
    private long startTime;
    private int errorCount = 0;
    private int correctCount = 0;
    private long timeTakenSeconds = 0;

    public LessonViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void init(int lessonId) {
        if (lessonId == 0) {
            questions = repository.getRandomQuestions();
        } else {
            questions = repository.getQuestionsForLesson(lessonId);
        }
        startTime = System.currentTimeMillis();
        errorCount = 0;
        correctCount = 0;
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }

    public LiveData<Integer> getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public LiveData<Boolean> isLessonCompleted() {
        return lessonCompleted;
    }

    public LiveData<Integer> getXpEarned() {
        return xpEarned;
    }

    public void nextQuestion() {
        correctCount++;
        List<Question> questionList = questions.getValue();
        if (questionList != null) {
            int nextIndex = currentQuestionIndex.getValue() + 1;
            if (nextIndex < questionList.size()) {
                currentQuestionIndex.setValue(nextIndex);
            } else {
                completeLesson();
            }
        }
    }

    public void recordError() {
        errorCount++;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public long getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    private void completeLesson() {
        timeTakenSeconds = (System.currentTimeMillis() - startTime) / 1000;
        xpEarned.setValue(20); // Base XP for completing a lesson
        lessonCompleted.setValue(true);
        updateUserProgress();
    }

    private void updateUserProgress() {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserProgress progress = repository.getDatabase().appDao().getUserProgressSync(1);
            if (progress != null) {
                progress.xp += 20;
                
                long now = System.currentTimeMillis();
                long last = progress.lastLessonDate;
                
                if (last == 0) {
                    // Самый первый пройденный урок
                    progress.dailyStreak = 1;
                } else {
                    Calendar lastDate = Calendar.getInstance();
                    lastDate.setTimeInMillis(last);
                    Calendar currentDate = Calendar.getInstance();
                    currentDate.setTimeInMillis(now);
                    
                    boolean sameDay = lastDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                                     lastDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR);
                                     
                    if (!sameDay) {
                        // Переходим на следующий день
                        lastDate.add(Calendar.DAY_OF_YEAR, 1);
                        boolean isNextDay = lastDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                                           lastDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR);
                        
                        if (isNextDay) {
                            progress.dailyStreak += 1;
                        } else {
                            // Был пропуск дня или более
                            progress.dailyStreak = 1;
                        }
                    }
                    // Если тот же день, серия (streak) не увеличивается, просто копим XP
                }
                
                progress.lastLessonDate = now;
                repository.getDatabase().appDao().updateUserProgress(progress);
                
                // Track daily activity
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(now);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long todayStart = cal.getTimeInMillis();
                
                DailyActivity activity = repository.getDatabase().appDao().getDailyActivitySync(todayStart);
                if (activity == null) {
                    repository.getDatabase().appDao().insertDailyActivity(new DailyActivity(todayStart, 20));
                } else {
                    activity.xpEarned += 20;
                    repository.getDatabase().appDao().insertDailyActivity(activity);
                }
            }
        });
    }
}
