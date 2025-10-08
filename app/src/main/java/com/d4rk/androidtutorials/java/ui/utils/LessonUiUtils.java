package com.d4rk.androidtutorials.java.ui.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.LessonDescriptionSectionBinding;

public final class LessonUiUtils {

    private LessonUiUtils() {
        // Utility class
    }

    public static void setupDescriptionSection(
            @NonNull LessonDescriptionSectionBinding descriptionSectionBinding,
            @StringRes int bodyTextResId,
            boolean showLayoutPreviewHeader
    ) {
        descriptionSectionBinding.descriptionHeader.getRoot().setText(R.string.description);
        descriptionSectionBinding.descriptionText.setText(bodyTextResId);
        AdUtils.loadBanner(descriptionSectionBinding.adView);

        View layoutPreviewHeader = descriptionSectionBinding.getRoot()
                .getRootView()
                .findViewById(R.id.layout_preview_header);
        if (layoutPreviewHeader == null) {
            return;
        }

        if (showLayoutPreviewHeader) {
            layoutPreviewHeader.setVisibility(View.VISIBLE);
            if (layoutPreviewHeader instanceof TextView) {
                ((TextView) layoutPreviewHeader).setText(R.string.layout_preview);
            }
        } else {
            layoutPreviewHeader.setVisibility(View.GONE);
        }
    }
}
