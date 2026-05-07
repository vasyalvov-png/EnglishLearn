package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ItemOptionBinding;

import java.util.ArrayList;
import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private List<String> options = new ArrayList<>();
    private final OnOptionClickListener listener;
    private int selectedPosition = -1;

    public interface OnOptionClickListener {
        void onOptionClick(String option, int position);
    }

    public OptionAdapter(OnOptionClickListener listener) {
        this.listener = listener;
    }

    public void setOptions(List<String> options) {
        this.options = options;
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedOption() {
        if (selectedPosition != -1 && selectedPosition < options.size()) {
            return options.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOptionBinding binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OptionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        String option = options.get(position);
        holder.bind(option, position == selectedPosition);
        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            int oldPos = selectedPosition;
            selectedPosition = currentPos;
            if (oldPos != -1) notifyItemChanged(oldPos);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onOptionClick(option, currentPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        private final ItemOptionBinding binding;

        public OptionViewHolder(ItemOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String option, boolean isSelected) {
            binding.textOption.setText(option);
            binding.getRoot().setChecked(isSelected);
        }
    }
}
