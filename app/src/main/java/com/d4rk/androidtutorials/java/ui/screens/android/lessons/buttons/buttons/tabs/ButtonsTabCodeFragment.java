package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons.tabs;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentSameCodeBinding;
import com.d4rk.androidtutorials.java.ui.components.NoCodeAdFragment;
import com.d4rk.androidtutorials.java.utils.CodeHighlighter;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;
import com.d4rk.androidtutorials.java.utils.FontManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ButtonsTabCodeFragment extends NoCodeAdFragment<FragmentSameCodeBinding> {

    @Override
    @NonNull
    protected FragmentSameCodeBinding inflateBinding(@NonNull LayoutInflater inflater, ViewGroup container) {
        return FragmentSameCodeBinding.inflate(inflater, container, false);
    }

    @Override
    protected void onBindingCreated(@NonNull FragmentSameCodeBinding binding, Bundle savedInstanceState) {
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Typeface monospaceFont = FontManager.getMonospaceFont(requireContext(), prefs);
        CodeViewUtils.applyDefaults(monospaceFont, binding.codeView);

        StringBuilder builder = new StringBuilder();
        InputStream inputStream = getResources().openRawResource(R.raw.text_buttons_java);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            binding.codeView.setText(builder.toString());
            CodeHighlighter.applyJavaTheme(binding.codeView);
        } catch (IOException e) {
            Log.e("ButtonsTabCode", "Error reading code", e);
        }
        binding.textViewWarning.setText(R.string.same_code_buttons);
    }
}