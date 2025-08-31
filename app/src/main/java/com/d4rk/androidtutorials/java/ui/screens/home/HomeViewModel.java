package com.d4rk.androidtutorials.java.ui.screens.home;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;


@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final Application application;
    private final HomeRepository homeRepository;
    private final GetDailyTipUseCase getDailyTipUseCase;
    private final GetPromotedAppsUseCase getPromotedAppsUseCase;

    private final MutableLiveData<String> announcementTitle = new MutableLiveData<>();
    private final MutableLiveData<String> announcementSubtitle = new MutableLiveData<>();
    private final MutableLiveData<String> dailyTip = new MutableLiveData<>();
    private final MutableLiveData<List<PromotedApp>> promotedApps = new MutableLiveData<>(new ArrayList<>());

    @Inject
    public HomeViewModel(Application application,
                         HomeRepository homeRepository,
                         GetDailyTipUseCase getDailyTipUseCase,
                         GetPromotedAppsUseCase getPromotedAppsUseCase) {
        this.application = application;
        this.homeRepository = homeRepository;
        this.getDailyTipUseCase = getDailyTipUseCase;
        this.getPromotedAppsUseCase = getPromotedAppsUseCase;

        announcementTitle.setValue(application.getString(R.string.announcement_title));
        announcementSubtitle.setValue(application.getString(R.string.announcement_subtitle));
        dailyTip.setValue(getDailyTipUseCase.invoke());

        getPromotedAppsUseCase.invoke(apps -> {
            if (apps.isEmpty()) {
                promotedApps.postValue(apps);
                return;
            }
            int startIndex = (int) ((System.currentTimeMillis() / (24L * 60 * 60 * 1000)) % apps.size());
            List<PromotedApp> rotated = new ArrayList<>();
            for (int i = 0; i < Math.min(4, apps.size()); i++) {
                rotated.add(apps.get((startIndex + i) % apps.size()));
            }
            promotedApps.postValue(rotated);
        });
    }

    /**
     * Provides a LiveData for the announcement title.
     */
    public LiveData<String> getAnnouncementTitle() {
        return announcementTitle;
    }

    /**
     * Provides a LiveData for the announcement subtitle.
     */
    public LiveData<String> getAnnouncementSubtitle() {
        return announcementSubtitle;
    }

    /**
     * Provides a LiveData for the tip of the day text.
     */
    public LiveData<String> getDailyTip() {
        return dailyTip;
    }

    /**
     * Returns an Intent that opens the Google Play Store page for your app.
     * The HomeFragment can startActivity(...) on it.
     */
    public Intent getOpenPlayStoreIntent() {
        return buildPlayStoreIntent(homeRepository.getPlayStoreUrl());
    }

    /**
     * List of apps to promote on the Home screen.
     */
    public LiveData<List<PromotedApp>> getPromotedApps() {
        return promotedApps;
    }

    /**
     * Builds an intent to open the Google Play listing for the provided package.
     */
    public Intent getPromotedAppIntent(String packageName) {
        return buildPlayStoreIntent(homeRepository.getAppPlayStoreUrl(packageName));
    }

    private Intent buildPlayStoreIntent(String url) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        playStoreIntent.setPackage("com.android.vending");
        if (playStoreIntent.resolveActivity(application.getPackageManager()) != null) {
            return playStoreIntent;
        }
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
}