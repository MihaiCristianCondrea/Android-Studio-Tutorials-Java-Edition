package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityTabLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear.tabs.LinearLayoutTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear.tabs.LinearLayoutTabLayoutFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class LinearLayoutCodeActivity extends UpNavigationActivity {
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.d4rk.androidtutorials.java.databinding.ActivityTabLayoutBinding binding = ActivityTabLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.enableEdgeToEdge(this.getWindow());

        viewPager2 = binding.viewpager;

        setupViewPager();

        new TabLayoutMediator(binding.tabs, viewPager2, (tab, position) -> {
            ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager2.getAdapter();
            if (adapter != null) {
                tab.setText(adapter.getPageTitle(position));
            }
        }).attach();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new LinearLayoutTabCodeFragment(), getString(R.string.code_java));
        adapter.addFragment(new LinearLayoutTabLayoutFragment(), getString(R.string.layout_xml));
        viewPager2.setAdapter(adapter);
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

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