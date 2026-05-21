package com.example.myapplication.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.SandboxActivity;
import com.example.myapplication.data.AppRepository;
import com.example.myapplication.data.entities.DailyActivity;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.ui.adapters.CalendarAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private CalendarAdapter calendarAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupCalendar();
        observeData();

        binding.btnOpenSandbox.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SandboxActivity.class);
            startActivity(intent);
        });

        binding.btnEditProfile.setOnClickListener(v -> 
            Toast.makeText(getContext(), R.string.edit_profile, Toast.LENGTH_SHORT).show());

        binding.btnAccountSettings.setOnClickListener(v -> 
            Toast.makeText(getContext(), R.string.account_info, Toast.LENGTH_SHORT).show());

        binding.btnLogout.setOnClickListener(v -> {
            // Logout logic: Navigate to login and clear all backstack
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build();
            Navigation.findNavController(v).navigate(R.id.loginFragment, null, navOptions);
        });
    }

    private void setupCalendar() {
        calendarAdapter = new CalendarAdapter();
        binding.recyclerCalendar.setLayoutManager(new GridLayoutManager(getContext(), 7));
        binding.recyclerCalendar.setAdapter(calendarAdapter);
    }

    private void observeData() {
        AppRepository repository = new AppRepository(requireContext());
        
        // Observe User Progress
        repository.getUserProgress(1).observe(getViewLifecycleOwner(), progress -> {
            if (progress != null) {
                binding.textXpProfile.setText(getString(R.string.xp_format, progress.xp));
                binding.textStreakProfile.setText(getString(R.string.streak_format, progress.dailyStreak));
                
                // Rank logic
                if (progress.xp > 500) {
                    binding.textProfileRank.setText(R.string.rank_master);
                } else if (progress.xp > 200) {
                    binding.textProfileRank.setText(R.string.rank_advanced);
                } else {
                    binding.textProfileRank.setText(R.string.rank_beginner);
                }
            }
        });

        // Observe Activity for Calendar
        repository.getRecentActivity().observe(getViewLifecycleOwner(), this::updateCalendarUI);
    }

    private void updateCalendarUI(List<DailyActivity> activities) {
        Set<Integer> activeDays = new HashSet<>();
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        
        if (activities != null) {
            for (DailyActivity activity : activities) {
                cal.setTimeInMillis(activity.date);
                if (cal.get(Calendar.MONTH) == currentMonth) {
                    activeDays.add(cal.get(Calendar.DAY_OF_MONTH));
                }
            }
        }

        cal = Calendar.getInstance();
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<CalendarAdapter.CalendarDay> dayList = new ArrayList<>();
        
        for (int i = 1; i <= maxDay; i++) {
            dayList.add(new CalendarAdapter.CalendarDay(i, activeDays.contains(i)));
        }
        
        calendarAdapter.setDays(dayList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
