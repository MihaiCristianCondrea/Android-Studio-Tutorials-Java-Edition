package com.d4rk.androidtutorials.java.ui.screens.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentAboutBinding;
import com.d4rk.androidtutorials.java.utils.ConsentUtils;
import com.google.android.gms.ads.MobileAds;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@AndroidEntryPoint
public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater,
                                          @Nullable android.view.ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);

        AboutViewModel aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        if (ConsentUtils.canShowAds(requireContext())) {
            MobileAds.initialize(requireContext());
            binding.adView.setVisibility(android.view.View.VISIBLE);
            binding.adView.loadAd();
        } else {
            binding.adView.setVisibility(android.view.View.GONE);
        }

        String version = aboutViewModel.getVersionString();
        binding.textViewAppVersion.setText(version);

        binding.textViewAppVersion.setOnLongClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(
                    ClipData.newPlainText("Label", binding.textViewAppVersion.getText())
            );
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                Toast.makeText(getContext(),
                        R.string.snack_copied_to_clipboard,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        binding.imageViewAppIcon.setOnClickListener(
                v -> openUrl("https://mihaicristiancondrea.github.io/profile"));
        binding.chipGoogleDev.setOnClickListener(
                v -> openUrl("https://g.dev/D4rK7355608"));
        binding.chipYoutube.setOnClickListener(
                v -> openUrl("https://www.youtube.com/c/D4rK7355608"));
        binding.chipGithub.setOnClickListener(
                v -> openUrl("https://github.com/D4rK7355608/" + BuildConfig.APPLICATION_ID));
        binding.chipTwitter.setOnClickListener(
                v -> openUrl("https://twitter.com/D4rK7355608"));
        binding.chipXda.setOnClickListener(
                v -> openUrl("https://forum.xda-developers.com/m/d4rk7355608.10095012"));
        binding.chipMusic.setOnClickListener(
                v -> openUrl("https://linktr.ee/D4rKRekords"));

        return binding.getRoot();
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}