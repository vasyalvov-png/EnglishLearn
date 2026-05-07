package com.example.myapplication.data;

import android.content.Context;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.data.entities.Question;
import com.example.myapplication.data.entities.UserProgress;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonDataLoader {

    public static class JsonData {
        @SerializedName("lessons")
        public List<LessonJson> lessons;
    }

    public static class LessonJson {
        public String title;
        public String description;
        public String difficultyLevel;
        public String type;
        public String theoryText;
        public List<QuestionJson> questions;
    }

    public static class QuestionJson {
        public String text;
        public String type;
        public String options;
        public String correctAnswer;
    }

    public static void loadInitialData(Context context, AppDatabase db) {
        String json = loadJSONFromAsset(context, "questions.json");
        if (json == null) return;

        Gson gson = new Gson();
        JsonData data = gson.fromJson(json, JsonData.class);

        if (data != null && data.lessons != null) {
            db.clearAllTables();
            for (LessonJson lJson : data.lessons) {
                long lessonId = db.appDao().insertLesson(new Lesson(
                        lJson.title,
                        lJson.description,
                        lJson.difficultyLevel,
                        lJson.type,
                        lJson.theoryText
                ));

                if (lJson.questions != null) {
                    for (QuestionJson qJson : lJson.questions) {
                        db.appDao().insertQuestion(new Question(
                                (int) lessonId,
                                qJson.text,
                                qJson.type,
                                qJson.options,
                                qJson.correctAnswer
                        ));
                    }
                }
            }
            
            // Initialize user progress
            if (db.appDao().getUserProgressSync(1) == null) {
                db.appDao().insertUserProgress(new UserProgress(1, 0, 0, 0));
            }
        }
    }

    private static String loadJSONFromAsset(Context context, String fileName) {
        String json;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
