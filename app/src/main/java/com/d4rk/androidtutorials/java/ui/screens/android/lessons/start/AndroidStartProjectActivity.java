package com.d4rk.androidtutorials.java.ui.screens.android.lessons.start;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityAndroidStartProjectBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import androidx.core.text.HtmlCompat;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class AndroidStartProjectActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAndroidStartProjectBinding binding = ActivityAndroidStartProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        AdUtils.loadBanner(binding.adViewBottom);
        AdUtils.loadBanner(binding.adView);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.textViewThirdStepSummary.setText(HtmlCompat.fromHtml(
                getString(R.string.summary_third_step),
                HtmlCompat.FROM_HTML_MODE_LEGACY
        ));
        binding.textViewThirdStepSummary.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
