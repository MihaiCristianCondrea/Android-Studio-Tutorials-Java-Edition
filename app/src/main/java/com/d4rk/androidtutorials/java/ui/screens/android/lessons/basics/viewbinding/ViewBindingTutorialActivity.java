package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.viewbinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityViewBindingTutorialBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.CodeHighlighter;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.d4rk.androidtutorials.java.utils.FontManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ViewBindingTutorialActivity extends UpNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.d4rk.androidtutorials.java.databinding.ActivityViewBindingTutorialBinding binding =
                ActivityViewBindingTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        AdUtils.loadBanner(binding.adViewBottom);
        AdUtils.loadBanner(binding.adView);
        binding.moreAboutViewBindingButton.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/topic/libraries/view-binding#java"))));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Typeface monospaceFont = FontManager.getMonospaceFont(this, prefs);
        CodeViewUtils.applyDefaults(monospaceFont,
                binding.codeViewBindingGradle,
                binding.codeViewBindingActivities,
                binding.codeViewBindingFragments);

        InputStream bindingGradle = getResources().openRawResource(R.raw.text_binding_gradle);
        binding.codeViewBindingGradle.setText(readTextFromInputStream(bindingGradle));
        CodeHighlighter.applyJavaTheme(binding.codeViewBindingGradle);

        InputStream bindingActivity = getResources().openRawResource(R.raw.text_binding_activity);
        binding.codeViewBindingActivities.setText(readTextFromInputStream(bindingActivity));
        CodeHighlighter.applyJavaTheme(binding.codeViewBindingActivities);

        InputStream bindingFragment = getResources().openRawResource(R.raw.text_binding_fragment);
        binding.codeViewBindingFragments.setText(readTextFromInputStream(bindingFragment));
        CodeHighlighter.applyJavaTheme(binding.codeViewBindingFragments);
    }

    private String readTextFromInputStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e("ViewBindingActivity", "Error reading file", e);
        }
        return builder.toString();
    }
}
