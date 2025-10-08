package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amrdeveloper.codeview.CodeView;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentClockLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;

public class ClockTabLayoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentClockLayoutBinding binding = FragmentClockLayoutBinding.inflate(inflater, container, false);
        AdUtils.loadBanner(binding.adView);
        setCodeView(binding.codeViewDigitalClockXml, R.raw.text_clock_digital_xml);
        setCodeView(binding.codeViewTextClockXml, R.raw.text_clock_xml);
        setCodeView(binding.codeViewAnalogClockXml, R.raw.text_clock_analog_xml);
        return binding.getRoot();
    }

    private void setCodeView(CodeView codeView, int rawResource) {
        CodeViewUtils.populateFromRawResource(
                codeView,
                rawResource,
                CodeViewUtils.HighlightMode.XML,
                "ClockTabLayout");
    }
}
