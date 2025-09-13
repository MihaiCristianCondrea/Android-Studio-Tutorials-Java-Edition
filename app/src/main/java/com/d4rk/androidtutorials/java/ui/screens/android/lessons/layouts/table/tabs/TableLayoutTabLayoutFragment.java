package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.table.tabs;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeHighlighter;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;
import com.d4rk.androidtutorials.java.utils.FontManager;
import com.d4rk.androidtutorials.java.ads.AdUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class TableLayoutTabLayoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLayoutBinding binding = FragmentLayoutBinding.inflate(inflater, container, false);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        AdUtils.loadBanner(binding.adView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Typeface monospaceFont = FontManager.getMonospaceFont(requireContext(), prefs);
        CodeViewUtils.applyDefaults(monospaceFont, binding.codeView);

        StringBuilder builder = new StringBuilder();
        InputStream inputStream = getResources().openRawResource(R.raw.text_table_layout_xml);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            binding.codeView.setText(builder.toString());
            CodeHighlighter.applyXmlTheme(binding.codeView);
        } catch (IOException e) {
            Log.e("TableLayoutTab", "Error reading layout", e);
        }
        return binding.getRoot();
    }

}