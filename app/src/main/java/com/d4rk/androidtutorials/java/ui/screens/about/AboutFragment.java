package com.d4rk.androidtutorials.java.ui.screens.about;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentAboutBinding;

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

        binding.adView.setVisibility(android.view.View.VISIBLE);
        AdUtils.loadBanner(binding.adView);

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

        Intent profileIntent = createBrowsableIntent(Uri.parse("https://mihaicristiancondrea.github.io/profile"));
        Intent googleDevIntent = createBrowsableIntent(Uri.parse("https://g.dev/Mihai-Cristian-Condrea"));
        Intent youtubeIntent = createBrowsableIntent(Uri.parse("https://www.youtube.com/c/D4rK7355608"));
        Intent githubIntent = createBrowsableIntent(Uri.parse("https://github.com/MihaiCristianCondrea/" + BuildConfig.APPLICATION_ID));
        Intent xIntent = createBrowsableIntent(Uri.parse("https://x.com/MihaiCristianC"));
        Intent xdaIntent = createBrowsableIntent(Uri.parse("https://forum.xda-developers.com/m/d4rk7355608.10095012"));
        Intent musicIntent = createBrowsableIntent(Uri.parse("https://linktr.ee/D4rKRekords"));

        bindExternalLink(binding.imageViewAppIcon, profileIntent);
        bindExternalLink(binding.chipGoogleDev, googleDevIntent);
        bindExternalLink(binding.chipYoutube, youtubeIntent);
        bindExternalLink(binding.chipGithub, githubIntent);
        bindExternalLink(binding.chipTwitter, xIntent);
        bindExternalLink(binding.chipXda, xdaIntent);
        bindExternalLink(binding.chipMusic, musicIntent);

        return binding.getRoot();
    }

    private void bindExternalLink(@NonNull View view, @NonNull Intent intent) {
        view.setOnClickListener(v -> openUrl(intent));
    }

    @NonNull
    private Intent createBrowsableIntent(@NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        return intent;
    }

    private void openUrl(@NonNull Intent intent) {
        Context context = requireContext();

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(context, R.string.error_no_app_for_link, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
