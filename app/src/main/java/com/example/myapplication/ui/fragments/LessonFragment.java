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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.data.AppDatabase;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.data.entities.Question;
import com.example.myapplication.databinding.FragmentLessonBinding;
import com.example.myapplication.ui.adapters.OptionAdapter;
import com.example.myapplication.ui.viewmodels.LessonViewModel;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class LessonFragment extends Fragment {

    private FragmentLessonBinding binding;
    private LessonViewModel viewModel;
    private OptionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLessonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private StringBuilder currentSelection = new StringBuilder();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
        
        int lessonId = 0;
        if (getArguments() != null) {
            lessonId = getArguments().getInt("lessonId");
        }
        viewModel.init(lessonId);

        checkTheory(lessonId);

        setupRecyclerView();
        observeViewModel();

        binding.toolbarLesson.setNavigationOnClickListener(v -> {
            Navigation.findNavController(requireView()).navigateUp();
        });

        binding.buttonCheck.setOnClickListener(v -> {
            checkAnswer();
        });

        binding.buttonClear.setOnClickListener(v -> {
            currentSelection.setLength(0);
            binding.textTargetSentence.setText("");
            binding.buttonClear.setVisibility(View.GONE);
        });

        binding.textTargetSentence.setOnClickListener(v -> {
            // Already handled by button_clear, keeping for legacy compatibility if needed
        });
    }

    private void checkTheory(int lessonId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            Lesson lesson = db.appDao().getLessonByIdSync(lessonId);
            if (lesson != null && lesson.theoryText != null && !lesson.theoryText.isEmpty()) {
                getActivity().runOnUiThread(() -> showTheoryDialog(lesson.theoryText));
            }
        });
    }

    private void showTheoryDialog(String theory) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_theory, null);
        TextView textContent = dialogView.findViewById(R.id.text_theory_content);
        View startButton = dialogView.findViewById(R.id.button_theory_start);

        textContent.setText(theory);

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.Theme_MyApplication_Dialog)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        startButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void setupRecyclerView() {
        adapter = new OptionAdapter((option, position) -> {
            List<Question> questions = viewModel.getQuestions().getValue();
            if (questions != null && viewModel.getCurrentQuestionIndex().getValue() != null) {
                int currentIndex = viewModel.getCurrentQuestionIndex().getValue();
                if (currentIndex < questions.size()) {
                    Question current = questions.get(currentIndex);
                    if ("SentenceTranslation".equals(current.type)) {
                        if (currentSelection.length() > 0) {
                            currentSelection.append(" ");
                        }
                        currentSelection.append(option);
                        binding.textTargetSentence.setText(currentSelection.toString());
                        binding.buttonClear.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        binding.recyclerOptions.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerOptions.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            if (questions != null && !questions.isEmpty()) {
                updateQuestionUI(questions.get(viewModel.getCurrentQuestionIndex().getValue()));
                binding.progressLessonQuiz.setMax(questions.size());
            }
        });

        viewModel.getCurrentQuestionIndex().observe(getViewLifecycleOwner(), index -> {
            List<Question> questions = viewModel.getQuestions().getValue();
            if (questions != null && index < questions.size()) {
                updateQuestionUI(questions.get(index));
                binding.progressLessonQuiz.setProgress(index);
            }
        });

        viewModel.isLessonCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (completed) {
                showCompletionDialog();
            }
        });
    }

    private void updateQuestionUI(Question question) {
        binding.textQuestionContent.setText(question.text);
        List<String> options = new ArrayList<>(Arrays.asList(question.options.split("\\|")));
        Collections.shuffle(options);
        adapter.setOptions(options);

        if ("SentenceTranslation".equals(question.type)) {
            binding.cardTargetSentence.setVisibility(View.VISIBLE);
            binding.textTargetSentence.setText("");
            binding.buttonClear.setVisibility(View.GONE);
            currentSelection.setLength(0);
        } else {
            binding.cardTargetSentence.setVisibility(View.GONE);
        }
        
        // Ensure RecyclerView is visible and has layout manager
        binding.recyclerOptions.setVisibility(View.VISIBLE);
    }

    private void checkAnswer() {
        List<Question> questions = viewModel.getQuestions().getValue();
        if (questions == null || viewModel.getCurrentQuestionIndex().getValue() == null) return;
        
        int currentIndex = viewModel.getCurrentQuestionIndex().getValue();
        if (currentIndex >= questions.size()) return;
        
        Question current = questions.get(currentIndex);
        boolean isCorrect = false;

        if ("SentenceTranslation".equals(current.type)) {
            String selection = currentSelection.toString().trim();
            if (selection.isEmpty()) {
                Snackbar.make(binding.getRoot(), R.string.select_words, Snackbar.LENGTH_SHORT).show();
                return;
            }
            isCorrect = selection.equalsIgnoreCase(current.correctAnswer.trim());
        } else {
            // MultipleChoice, FillInTheBlank, WordChoice
            String selected = adapter.getSelectedOption();
            if (selected == null) {
                Snackbar.make(binding.getRoot(), R.string.select_option, Snackbar.LENGTH_SHORT).show();
                return;
            }
            isCorrect = selected.trim().equalsIgnoreCase(current.correctAnswer.trim());
        }

        if (isCorrect) {
            Snackbar.make(binding.getRoot(), R.string.correct, Snackbar.LENGTH_SHORT).show();
            currentSelection.setLength(0);
            binding.textTargetSentence.setText("");
            binding.buttonClear.setVisibility(View.GONE);
            viewModel.nextQuestion();
        } else {
            viewModel.recordError();
            Snackbar.make(binding.getRoot(), R.string.incorrect, Snackbar.LENGTH_SHORT).show();
            if ("SentenceTranslation".equals(current.type)) {
                currentSelection.setLength(0);
                binding.textTargetSentence.setText("");
                binding.buttonClear.setVisibility(View.GONE);
            }
        }
    }

    private void showCompletionDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_lesson_result, null);
        
        TextView textXp = dialogView.findViewById(R.id.text_result_xp);
        TextView textErrors = dialogView.findViewById(R.id.text_result_errors);
        TextView textTime = dialogView.findViewById(R.id.text_result_time);
        View finishButton = dialogView.findViewById(R.id.button_result_finish);

        long totalSeconds = viewModel.getTimeTakenSeconds();
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        textXp.setText(getString(R.string.xp_format, viewModel.getXpEarned().getValue()));
        textErrors.setText(String.valueOf(viewModel.getErrorCount()));
        textTime.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.Theme_MyApplication_Dialog)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        finishButton.setOnClickListener(v -> {
            dialog.dismiss();
            Navigation.findNavController(requireView()).navigateUp();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
