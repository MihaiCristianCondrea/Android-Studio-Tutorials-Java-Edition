package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityOnboardingBinding;
import com.d4rk.androidtutorials.java.ui.screens.main.MainActivity;
import com.d4rk.androidtutorials.java.ui.screens.startup.StartupViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

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

        // Fallback: show the consent form again if required.
        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(this);
        if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
            StartupViewModel consentViewModel = new ViewModelProvider(this).get(StartupViewModel.class);
            ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
            consentViewModel.requestConsentInfoUpdate(this, params,
                    () -> consentViewModel.loadConsentForm(this, null), null);
        }

        adapter = new OnboardingPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        int startPage = viewModel.getCurrentPage();
        binding.viewPager.setCurrentItem(startPage, false);
        currentPosition = startPage;

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
                    } else if (fragment instanceof DataFragment) {
                        ((DataFragment) fragment).saveSelection();
                    }
                }
                currentPosition = position;
                viewModel.setCurrentPage(position);
            }
        });

        new TabLayoutMediator(binding.tabIndicator, binding.viewPager, (tab, position) -> {
            ImageView dot = new ImageView(this);
            dot.setImageResource(R.drawable.onboarding_dot_unselected);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            dot.setLayoutParams(params);
            tab.setCustomView(dot);
        }).attach();

        TabLayout.Tab startTab = binding.tabIndicator.getTabAt(startPage);
        if (startTab != null && startTab.getCustomView() instanceof ImageView) {
            ((ImageView) startTab.getCustomView()).setImageResource(R.drawable.onboarding_dot_selected);
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
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        binding.buttonBack.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            if (current > 0) {
                binding.viewPager.setCurrentItem(current - 1);
            }
        });

        binding.buttonSkip.setOnClickListener(v -> {
            viewModel.markOnboardingComplete();
            finishOnboarding();
        });

        binding.buttonNext.setOnClickListener(v -> {
            int current = binding.viewPager.getCurrentItem();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + current);
            if (fragment instanceof ThemeFragment) {
                ((ThemeFragment) fragment).saveSelection();
            } else if (fragment instanceof StartPageFragment) {
                ((StartPageFragment) fragment).saveSelection();
            } else if (fragment instanceof DataFragment) {
                ((DataFragment) fragment).saveSelection();
            }

            if (current < adapter.getItemCount() - 1) {
                binding.viewPager.setCurrentItem(current + 1);
            } else {
                viewModel.markOnboardingComplete();
                finishOnboarding();
            }
        });

        updateButtons(startPage);
    }

    void finishOnboarding() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void updateButtons(int position) {
        if (position == adapter.getItemCount() - 1) {
            binding.bottomBar.setVisibility(View.GONE);
            binding.buttonSkip.setVisibility(View.GONE);
        } else {
            binding.bottomBar.setVisibility(View.VISIBLE);
            binding.buttonSkip.setVisibility(View.VISIBLE);
            binding.buttonBack.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            binding.buttonNext.setText(R.string.next);
            binding.buttonNext.setIconResource(R.drawable.ic_arrow_forward);
        }
    }

    private static class OnboardingPagerAdapter extends FragmentStateAdapter {

        OnboardingPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return switch (position) {
                case 0 -> new ThemeFragment();
                case 1 -> new StartPageFragment();
                case 2 -> new DataFragment();
                default -> new DoneFragment();
            };
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}

