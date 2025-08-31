package com.d4rk.androidtutorials.java.ui.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.databinding.FragmentHomeBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initializeAds();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setAnnouncements(
                getString(com.d4rk.androidtutorials.java.R.string.announcement_title),
                getString(com.d4rk.androidtutorials.java.R.string.announcement_subtitle)
        );
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        homeViewModel.getUiState().observe(getViewLifecycleOwner(), state -> {
            binding.announcementTitle.setText(state.announcementTitle());
            binding.announcementSubtitle.setText(state.announcementSubtitle());
            binding.tipText.setText(state.dailyTip());
            binding.shareTipButton.setOnClickListener(v -> shareTip(state.dailyTip()));

            ViewGroup promotedContainer = binding.promotedAppsContainer;
            binding.scrollView.clearFocus();
            promotedContainer.clearFocus();
            promotedContainer.removeAllViews();
            for (com.d4rk.androidtutorials.java.data.model.PromotedApp app : state.promotedApps()) {
                com.d4rk.androidtutorials.java.databinding.PromotedAppItemBinding itemBinding =
                        com.d4rk.androidtutorials.java.databinding.PromotedAppItemBinding.inflate(inflater, promotedContainer, false);
                loadImage(app.iconUrl(), itemBinding.appIcon);
                itemBinding.appName.setText(app.name());
                itemBinding.appDescription.setVisibility(android.view.View.GONE);
                itemBinding.appButton.setOnClickListener(v -> startActivity(homeViewModel.getPromotedAppIntent(app.packageName())));
                promotedContainer.addView(itemBinding.getRoot());
            }
        });
        new FastScrollerBuilder(binding.scrollView)
                .useMd2Style()
                .build();
        binding.btnGooglePlay.setOnClickListener(v -> startActivity(homeViewModel.getOpenPlayStoreIntent()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.scrollView.clearFocus();
        }
        binding = null;
    }

    private void initializeAds() {
        MobileAds.initialize(requireContext());
        binding.smallBannerAd.loadAd(new AdRequest.Builder().build());
        binding.largeBannerAd.loadAd(new AdRequest.Builder().build());
    }

    private void shareTip(String tip) {
        android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, tip);
        startActivity(android.content.Intent.createChooser(shareIntent, getString(com.d4rk.androidtutorials.java.R.string.share_using)));
    }

    private void loadImage(String url, android.widget.ImageView imageView) {
        com.android.volley.toolbox.ImageRequest request = new com.android.volley.toolbox.ImageRequest(
                url,
                imageView::setImageBitmap,
                0,
                0,
                android.widget.ImageView.ScaleType.CENTER_INSIDE,
                android.graphics.Bitmap.Config.ARGB_8888,
                error -> {});
        com.android.volley.toolbox.Volley.newRequestQueue(requireContext()).add(request);
    }
}