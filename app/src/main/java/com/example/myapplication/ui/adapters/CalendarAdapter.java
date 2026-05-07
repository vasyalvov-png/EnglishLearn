package com.example.myapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemCalendarDayBinding;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<CalendarDay> days = new ArrayList<>();

    public static class CalendarDay {
        public int day;
        public boolean isActive;

        public CalendarDay(int day, boolean isActive) {
            this.day = day;
            this.isActive = isActive;
        }
    }

    public void setDays(List<CalendarDay> newDays) {
        days.clear();
        days.addAll(newDays);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCalendarDayBinding binding = ItemCalendarDayBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CalendarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        CalendarDay day = days.get(position);
        holder.binding.textDayNumber.setText(String.valueOf(day.day));
        if (day.isActive) {
            holder.binding.textDayNumber.setBackgroundResource(R.drawable.shape_day_active);
            holder.binding.textDayNumber.setTextColor(holder.itemView.getContext().getColor(android.R.color.white));
        } else {
            holder.binding.textDayNumber.setBackgroundResource(R.drawable.shape_day_inactive);
            holder.binding.textDayNumber.setTextColor(holder.itemView.getContext().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        final ItemCalendarDayBinding binding;
        CalendarViewHolder(ItemCalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
