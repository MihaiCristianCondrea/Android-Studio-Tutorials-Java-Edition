package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.d4rk.androidtutorials.java.databinding.ActivityOnboardingBinding;
import com.d4rk.androidtutorials.java.ui.screens.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new OnboardingPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);
    }

    void nextPage() {
        int current = binding.viewPager.getCurrentItem();
        if (current < adapter.getItemCount() - 1) {
            binding.viewPager.setCurrentItem(current + 1);
        }
    }

    void finishOnboarding() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private static class OnboardingPagerAdapter extends FragmentStateAdapter {

        OnboardingPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ThemeFragment();
                case 1:
                    return new StartPageFragment();
                case 2:
                    return new BottomLabelsFragment();
                default:
                    return new DataFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}

