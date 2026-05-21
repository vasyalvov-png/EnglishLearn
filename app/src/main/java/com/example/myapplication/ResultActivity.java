package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.entities.StudentData;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textReceived = findViewById(R.id.text_received_data);
        
        // Receiving Intent Data (Parcelable)
        StudentData student = getIntent().getParcelableExtra("student_data");
        if (student != null) {
            textReceived.setText("Получено из Intent:\n" + student.getName() + " (XP: " + student.getScore() + ")");
        }

        findViewById(R.id.btn_return_result).setOnClickListener(v -> {
            // Task: Activity Result API
            Intent resultIntent = new Intent();
            resultIntent.putExtra("status", "Успешно пройдено!");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
