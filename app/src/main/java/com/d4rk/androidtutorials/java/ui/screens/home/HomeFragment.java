package com.d4rk.androidtutorials.java.ui.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentHomeBinding;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private PromotedAppsAdapter promotedAppsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        promotedAppsAdapter = new PromotedAppsAdapter(new PromotedAppsAdapter.PromotedAppActionListener() {
            @Override
            public void onOpenApp(@NonNull com.d4rk.androidtutorials.java.data.model.PromotedApp app) {
                startActivity(homeViewModel.getPromotedAppIntent(app.packageName()));
            }

            @Override
            public void onShareApp(@NonNull com.d4rk.androidtutorials.java.data.model.PromotedApp app) {
                shareApp(app);
            }
        });
        initializeAds();
        binding.promotedAppsRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.promotedAppsRecyclerView.setAdapter(promotedAppsAdapter);
        homeViewModel.setAnnouncements(
                getString(com.d4rk.androidtutorials.java.R.string.announcement_title),
                getString(com.d4rk.androidtutorials.java.R.string.announcement_subtitle)
        );
        homeViewModel.getContentState().observe(getViewLifecycleOwner(), contentState -> {
            binding.announcementTitle.setText(contentState.announcementTitle());
            binding.announcementSubtitle.setText(contentState.announcementSubtitle());
            binding.tipText.setText(contentState.dailyTip());
            binding.shareTipButton.setOnClickListener(v -> shareTip(contentState.dailyTip()));
        });
        homeViewModel.getPromotedAppsState().observe(getViewLifecycleOwner(), apps -> {
            binding.scrollView.clearFocus();
            binding.promotedAppsRecyclerView.clearFocus();
            promotedAppsAdapter.submitList(apps);
        });
        new FastScrollerBuilder(binding.scrollView)
                .useMd2Style()
                .build();
        binding.btnGooglePlay.setOnClickListener(v ->
                startActivity(homeViewModel.getOpenPlayStoreIntent()));
        binding.btnLearnMore.setOnClickListener(v ->
                startActivity(homeViewModel.getLearnMoreIntent()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.scrollView.clearFocus();
            binding.promotedAppsRecyclerView.setAdapter(null);
        }
        promotedAppsAdapter = null;
        binding = null;
    }

    private void initializeAds() {
        binding.smallBannerAd.setVisibility(View.VISIBLE);
        binding.largeBannerAd.setVisibility(View.VISIBLE);
        AdUtils.loadBanner(binding.smallBannerAd);
        AdUtils.loadBanner(binding.largeBannerAd);
    }

    private void shareTip(String tip) {
        android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, tip);
        startActivity(android.content.Intent.createChooser(shareIntent, getString(com.d4rk.androidtutorials.java.R.string.share_using)));
    }

    private void shareApp(com.d4rk.androidtutorials.java.data.model.PromotedApp app) {
        android.content.Intent sharingIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        android.net.Uri data = homeViewModel.getPromotedAppIntent(app.packageName()).getData();
        String shareLink = data != null ? data.toString() : "";
        String shareMessage = getString(com.d4rk.androidtutorials.java.R.string.share_message, shareLink);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(com.d4rk.androidtutorials.java.R.string.share_subject));
        startActivity(android.content.Intent.createChooser(sharingIntent, getString(com.d4rk.androidtutorials.java.R.string.share_using)));
    }

}
