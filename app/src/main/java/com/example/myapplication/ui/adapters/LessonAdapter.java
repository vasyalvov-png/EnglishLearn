package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.Lesson;
import com.example.myapplication.databinding.ItemLessonBinding;

public class LessonAdapter extends ListAdapter<Lesson, LessonAdapter.LessonViewHolder> {

    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonPointClick(Lesson lesson);
    }

    public LessonAdapter(OnLessonClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Lesson> DIFF_CALLBACK = new DiffUtil.ItemCallback<Lesson>() {
        @Override
        public boolean areItemsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Lesson oldItem, @NonNull Lesson newItem) {
            return oldItem.title.equals(newItem.title) &&
                    oldItem.description.equals(newItem.description) &&
                    oldItem.difficultyLevel.equals(newItem.difficultyLevel);
        }
    };

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLessonBinding binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LessonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
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
            
            // Progress and status for visual effect
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
