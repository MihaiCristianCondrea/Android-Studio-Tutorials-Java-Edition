package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amrdeveloper.codeview.CodeView;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentButtonsLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class ButtonsTabLayoutFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentButtonsLayoutBinding binding = FragmentButtonsLayoutBinding.inflate(inflater, container, false);
        AdUtils.loadBanner(binding.adView);
        Map<Integer, CodeView> buttonXmlResources = new LinkedHashMap<>();
        buttonXmlResources.put(R.raw.text_button_normal_xml, binding.codeViewButtonNormalXml);
        buttonXmlResources.put(R.raw.text_button_outlined_xml, binding.codeViewButtonOutlinedXml);
        buttonXmlResources.put(R.raw.text_button_elevated_xml, binding.codeViewButtonElevatedXml);
        buttonXmlResources.put(R.raw.text_button_normal_icon_xml, binding.codeViewButtonNormalIconXml);
        buttonXmlResources.put(R.raw.text_button_outlined_icon_xml, binding.codeViewButtonOutlinedIconXml);
        buttonXmlResources.put(R.raw.text_button_elevated_icon_xml, binding.codeViewButtonElevatedIconXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_primary_xml, binding.codeViewExtendedFloatingButtonPrimaryXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_secondary_xml, binding.codeViewExtendedFloatingButtonSecondaryXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_surface_xml, binding.codeViewExtendedFloatingButtonSurfaceXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_tertiary_xml, binding.codeViewExtendedFloatingButtonTertiaryXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_primary_icon_xml, binding.codeViewExtendedFloatingButtonPrimaryIconXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_secondary_icon_xml, binding.codeViewExtendedFloatingButtonSecondaryIconXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_surface_icon_xml, binding.codeViewExtendedFloatingButtonSurfaceIconXml);
        buttonXmlResources.put(R.raw.text_extended_floating_button_tertiary_icon_xml, binding.codeViewExtendedFloatingButtonTertiaryIconXml);
        buttonXmlResources.put(R.raw.text_floating_button_primary_xml, binding.codeViewFloatingButtonPrimaryXml);
        buttonXmlResources.put(R.raw.text_floating_button_secondary_xml, binding.codeViewFloatingButtonSecondaryXml);
        buttonXmlResources.put(R.raw.text_floating_button_surface_xml, binding.codeViewFloatingButtonSurfaceXml);
        buttonXmlResources.put(R.raw.text_floating_button_tertiary_xml, binding.codeViewFloatingButtonTertiaryXml);
        for (Map.Entry<Integer, CodeView> entry : buttonXmlResources.entrySet()) {
            CodeViewUtils.populateFromRawResource(
                    entry.getValue(),
                    entry.getKey(),
                    CodeViewUtils.HighlightMode.XML,
                    "ButtonsTabLayout");
        }
        return binding.getRoot();
    }

}