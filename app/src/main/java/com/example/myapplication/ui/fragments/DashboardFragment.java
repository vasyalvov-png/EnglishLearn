package com.example.myapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.JsonDataLoader;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.data.entities.Question;
import com.example.myapplication.data.entities.UserProgress;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.ui.adapters.LessonAdapter;

import java.util.List;
import java.util.concurrent.Executors;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private LessonAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        observeData();

        binding.fabContinue.setOnClickListener(v -> {
            startRandomPractice();
        });

        binding.cardRandomPractice.setOnClickListener(v -> {
            startRandomPractice();
        });

        seedDataIfNecessary();
    }

    private void startRandomPractice() {
        Bundle args = new Bundle();
        args.putInt("lessonId", 0); // 0 means random practice
        Navigation.findNavController(requireView()).navigate(R.id.action_dashboard_to_lesson, args);
    }

    private void setupRecyclerView() {
        adapter = new LessonAdapter(lesson -> {
            showTheoryDialog(lesson);
        });
        binding.recyclerLearningPath.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerLearningPath.setAdapter(adapter);
    }

    private void showTheoryDialog(Lesson lesson) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_theory, null);
        TextView textTitle = dialogView.findViewById(R.id.text_theory_title);
        TextView textContent = dialogView.findViewById(R.id.text_theory_content);
        View startButton = dialogView.findViewById(R.id.button_theory_start);

        textTitle.setText(lesson.title);
        textContent.setText(lesson.theoryText);

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.Theme_MyApplication_Dialog)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        startButton.setOnClickListener(v -> {
            dialog.dismiss();
            Bundle args = new Bundle();
            args.putInt("lessonId", lesson.id);
            Navigation.findNavController(requireView()).navigate(R.id.action_dashboard_to_lesson, args);
        });

        dialog.show();
    }

    private void observeData() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());
        db.appDao().getAllLessons().observe(getViewLifecycleOwner(), lessons -> {
            adapter.setLessons(lessons);
        });

        db.appDao().getUserProgress(1).observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                binding.textStreakValue.setText(getString(R.string.streak_format, progress.dailyStreak));
                binding.textXpValue.setText(getString(R.string.xp_format, progress.xp));
                binding.progressXp.setProgress(progress.xp % 100);
            }
        });
    }

    private void seedDataIfNecessary() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            List<Lesson> lessons = db.appDao().getAllLessonsSync();
            if (lessons == null || lessons.isEmpty()) {
                JsonDataLoader.loadInitialData(requireContext(), db);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
