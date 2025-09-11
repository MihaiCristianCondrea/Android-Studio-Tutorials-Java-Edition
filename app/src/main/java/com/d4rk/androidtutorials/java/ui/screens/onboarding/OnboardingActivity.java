package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityOnboardingBinding;
import com.d4rk.androidtutorials.java.ui.screens.main.MainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private OnboardingPagerAdapter adapter;
    private OnboardingViewModel viewModel;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

        adapter = new OnboardingPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position > currentPosition) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + currentPosition);
                    if (fragment instanceof ThemeFragment) {
                        ((ThemeFragment) fragment).saveSelection();
                    } else if (fragment instanceof StartPageFragment) {
                        ((StartPageFragment) fragment).saveSelection();
                    } else if (fragment instanceof BottomLabelsFragment) {
                        ((BottomLabelsFragment) fragment).saveSelection();
                    }
                }
                currentPosition = position;
            }
        });

        new TabLayoutMediator(binding.tabIndicator, binding.viewPager, (tab, position) -> {
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.onboarding_dot_unselected);
            tab.setCustomView(dot);
        }).attach();

        TabLayout.Tab firstTab = binding.tabIndicator.getTabAt(0);
        if (firstTab != null && firstTab.getCustomView() instanceof ImageView) {
            ((ImageView) firstTab.getCustomView()).setImageResource(R.drawable.onboarding_dot_selected);
        }

        binding.tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getCustomView() instanceof ImageView) {
                    ((ImageView) tab.getCustomView()).setImageResource(R.drawable.onboarding_dot_selected);
                }
                updateButtons(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getCustomView() instanceof ImageView) {
                    ((ImageView) tab.getCustomView()).setImageResource(R.drawable.onboarding_dot_unselected);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        binding.buttonBack.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current > 0) {
                binding.viewPager.setCurrentItem(current - 1);
            }
        });

        binding.buttonNext.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + current);
            if (fragment instanceof ThemeFragment) {
                ((ThemeFragment) fragment).saveSelection();
            } else if (fragment instanceof StartPageFragment) {
                ((StartPageFragment) fragment).saveSelection();
            } else if (fragment instanceof BottomLabelsFragment) {
                ((BottomLabelsFragment) fragment).saveSelection();
            }

            if (current < adapter.getItemCount() - 1) {
                binding.viewPager.setCurrentItem(current + 1);
            } else {
                viewModel.markOnboardingComplete();
                finishOnboarding();
            }
        });

        updateButtons(0);
    }

    void finishOnboarding() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void updateButtons(int position) {
        binding.buttonBack.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        if (position == adapter.getItemCount() - 1) {
            binding.buttonNext.setText(R.string.finish);
        } else {
            binding.buttonNext.setText(R.string.next);
        }
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

