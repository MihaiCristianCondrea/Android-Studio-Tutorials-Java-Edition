package com.d4rk.androidtutorials.java.ui.screens.android.tabs;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeHighlighter;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;
import com.d4rk.androidtutorials.java.utils.FontManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class LayoutFragment extends Fragment {
    private FragmentLayoutBinding binding;
    private int layoutResId;

    public static LayoutFragment newInstance(int layoutResId) {
        LayoutFragment fragment = new LayoutFragment();
        Bundle args = new Bundle();
        args.putInt("layoutResId", layoutResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layoutResId = getArguments().getInt("layoutResId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater, container, false);
        setupUI();
        loadLayout();
        return binding.getRoot();
    }

    private void setupUI() {
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        AdUtils.loadBanner(binding.adView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Typeface monospaceFont = FontManager.getMonospaceFont(requireContext(), prefs);
        CodeViewUtils.applyDefaults(monospaceFont, binding.codeView);
    }

    private void loadLayout() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getResources().openRawResource(layoutResId)))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            binding.codeView.setText(builder.toString());
            CodeHighlighter.applyXmlTheme(binding.codeView);
        } catch (IOException e) {
            Log.e("LayoutFragment", "Error loading layout", e);
            binding.codeView.setText(getString(R.string.error_loading_layout));
        }
    }
}