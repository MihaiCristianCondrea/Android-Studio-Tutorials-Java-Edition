package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons.tabs;

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
import com.d4rk.androidtutorials.java.databinding.FragmentButtonsLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeHighlighter;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;
import com.d4rk.androidtutorials.java.utils.FontManager;
import com.d4rk.androidtutorials.java.ads.AdUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ButtonsTabLayoutFragment extends Fragment {
    private final Map<Integer, CodeView> buttonXMLResources = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentButtonsLayoutBinding binding = FragmentButtonsLayoutBinding.inflate(inflater, container, false);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        AdUtils.loadBanner(binding.adView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Typeface monospaceFont = FontManager.getMonospaceFont(requireContext(), prefs);
        CodeViewUtils.applyDefaults(monospaceFont, buttonXMLResources.values().toArray(new CodeView[0]));
        buttonXMLResources.put(R.raw.text_button_normal_xml, binding.codeViewButtonNormalXml);
        buttonXMLResources.put(R.raw.text_button_outlined_xml, binding.codeViewButtonOutlinedXml);
        buttonXMLResources.put(R.raw.text_button_elevated_xml, binding.codeViewButtonElevatedXml);
        buttonXMLResources.put(R.raw.text_button_normal_icon_xml, binding.codeViewButtonNormalIconXml);
        buttonXMLResources.put(R.raw.text_button_outlined_icon_xml, binding.codeViewButtonOutlinedIconXml);
        buttonXMLResources.put(R.raw.text_button_elevated_icon_xml, binding.codeViewButtonElevatedIconXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_primary_xml, binding.codeViewExtendedFloatingButtonPrimaryXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_secondary_xml, binding.codeViewExtendedFloatingButtonSecondaryXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_surface_xml, binding.codeViewExtendedFloatingButtonSurfaceXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_tertiary_xml, binding.codeViewExtendedFloatingButtonTertiaryXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_primary_icon_xml, binding.codeViewExtendedFloatingButtonPrimaryIconXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_secondary_icon_xml, binding.codeViewExtendedFloatingButtonSecondaryIconXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_surface_icon_xml, binding.codeViewExtendedFloatingButtonSurfaceIconXml);
        buttonXMLResources.put(R.raw.text_extended_floating_button_tertiary_icon_xml, binding.codeViewExtendedFloatingButtonTertiaryIconXml);
        buttonXMLResources.put(R.raw.text_floating_button_primary_xml, binding.codeViewFloatingButtonPrimaryXml);
        buttonXMLResources.put(R.raw.text_floating_button_secondary_xml, binding.codeViewFloatingButtonSecondaryXml);
        buttonXMLResources.put(R.raw.text_floating_button_surface_xml, binding.codeViewFloatingButtonSurfaceXml);
        buttonXMLResources.put(R.raw.text_floating_button_tertiary_xml, binding.codeViewFloatingButtonTertiaryXml);
        for (Map.Entry<Integer, CodeView> entry : buttonXMLResources.entrySet()) {
            Integer resourceId = entry.getKey();
            CodeView codeView = entry.getValue();
            try (InputStream inputStream = getResources().openRawResource(resourceId);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }
                codeView.setText(builder.toString());
                CodeHighlighter.applyXmlTheme(codeView);
            } catch (IOException e) {
                Log.e("ButtonsTab", "Error reading button resource", e);
            }
        }
        return binding.getRoot();
    }

}