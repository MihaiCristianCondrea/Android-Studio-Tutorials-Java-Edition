package com.d4rk.androidtutorials.java.ui.components.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.BottomSheetMenuBinding;
import com.d4rk.androidtutorials.java.ui.screens.settings.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetMenuFragment extends BottomSheetDialogFragment {

    private BottomSheetMenuBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BottomSheetMenuBinding.inflate(inflater, container, false);

        binding.menuSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SettingsActivity.class);
            startActivity(intent);
            dismiss();
        });

        binding.menuHelpFeedback.setOnClickListener(v -> {
            Intent helpIntent = new Intent();
            helpIntent.setClassName(
                    "com.d4rk.androidtutorials.java",
                    "com.d4rk.androidtutorials.java.ui.screens.help.HelpActivity"
            );
            if (helpIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(helpIntent);
            }
            dismiss();
        });

        binding.menuUpdates.setOnClickListener(v -> {
            Uri changelogUri = Uri.parse("https://github.com/D4rK7355608/com.d4rk.androidtutorials.java/blob/main/CHANGELOG.md");
            Intent openChangelog = new Intent(Intent.ACTION_VIEW, changelogUri);
            if (openChangelog.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivity(openChangelog);
            }
            dismiss();
        });

        binding.menuShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");

            String shareLink = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));

            startActivity(
                    Intent.createChooser(sharingIntent, getString(R.string.share_using))
            );
            dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}