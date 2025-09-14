package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs;

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

import com.amrdeveloper.codeview.CodeView;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentClockLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeHighlighter;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;
import com.d4rk.androidtutorials.java.utils.FontManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ClockTabLayoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentClockLayoutBinding binding = FragmentClockLayoutBinding.inflate(inflater, container, false);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        AdUtils.loadBanner(binding.adView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Typeface monospaceFont = FontManager.getMonospaceFont(requireContext(), prefs);
        CodeViewUtils.applyDefaults(monospaceFont,
                binding.codeViewDigitalClockXml,
                binding.codeViewTextClockXml,
                binding.codeViewAnalogClockXml);
        setCodeView(binding.codeViewDigitalClockXml, R.raw.text_clock_digital_xml);
        setCodeView(binding.codeViewTextClockXml, R.raw.text_clock_xml);
        setCodeView(binding.codeViewAnalogClockXml, R.raw.text_clock_analog_xml);
        return binding.getRoot();
    }

    private void setCodeView(CodeView codeView, int rawResource) {
        try (InputStream inputStream = getResources().openRawResource(rawResource)) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String text = result.toString();
            codeView.setText(text);
            CodeHighlighter.applyXmlTheme(codeView);
        } catch (IOException e) {
            Log.e("ClockTab", "Error reading clock layout", e);
        }
    }
}