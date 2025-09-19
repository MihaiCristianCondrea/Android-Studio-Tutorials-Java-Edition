package com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityTabLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar.tabs.ProgressBarTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar.tabs.ProgressBarTabLayoutFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ProgressBarCodeActivity extends UpNavigationActivity {
    private ActivityTabLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.enableEdgeToEdge(this.getWindow());

        setupViewPager();

        new TabLayoutMediator(binding.tabs, binding.viewpager, (tab, position) -> {
            ViewPagerAdapter adapter = (ViewPagerAdapter) binding.viewpager.getAdapter();
            if (adapter != null) {
                tab.setText(adapter.getPageTitle(position));
            }
        }).attach();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new ProgressBarTabCodeFragment(), getString(R.string.code_java));
        adapter.addFragment(new ProgressBarTabLayoutFragment(), getString(R.string.layout_xml));
        binding.viewpager.setAdapter(adapter);
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        private final ArrayList<Fragment> fragmentList = new ArrayList<>();
        private final ArrayList<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull UpNavigationActivity activity) {
            super(activity);
        }

        public void addFragment(@NonNull Fragment fragment, @NonNull String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}