package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.databinding.ItemLessonBinding;

import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessons = new ArrayList<>();
    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonPointClick(Lesson lesson);
    }

    public LessonAdapter(OnLessonClickListener listener) {
        this.listener = listener;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLessonBinding binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LessonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.bind(lesson, listener);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        private final ItemLessonBinding binding;

        public LessonViewHolder(ItemLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Lesson lesson, OnLessonClickListener listener) {
            binding.textLessonName.setText(lesson.title);
            binding.textLessonDesc.setText(lesson.description);
            binding.chipDifficulty.setText(lesson.difficultyLevel);
            
            // Random progress and status for visual effect
            int progress = (lesson.title.length() * 11) % 101;
            binding.progressLesson.setProgress(progress);
            
            if (progress >= 100) {
                binding.textStatus.setText(binding.getRoot().getContext().getString(R.string.status_completed));
            } else if (progress > 0) {
                binding.textStatus.setText(binding.getRoot().getContext().getString(R.string.status_in_progress));
            } else {
                binding.textStatus.setText(binding.getRoot().getContext().getString(R.string.status_not_started));
            }
            
            // Restore click listener for theory
            binding.getRoot().setOnClickListener(v -> listener.onLessonPointClick(lesson));
        }
    }
}
