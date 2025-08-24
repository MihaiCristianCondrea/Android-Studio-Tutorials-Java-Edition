package com.d4rk.androidtutorials.java.ui.screens.home;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.ui.screens.home.repository.HomeRepository;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;

import java.util.ArrayList;
import java.util.List;


public class HomeViewModel extends AndroidViewModel {

    private final HomeRepository homeRepository;
    private final GetDailyTipUseCase getDailyTipUseCase;
    private final GetPromotedAppsUseCase getPromotedAppsUseCase;

    private final MutableLiveData<String> announcementTitle = new MutableLiveData<>();
    private final MutableLiveData<String> announcementSubtitle = new MutableLiveData<>();
    private final MutableLiveData<String> dailyTip = new MutableLiveData<>();
    private final MutableLiveData<List<PromotedApp>> promotedApps = new MutableLiveData<>(new ArrayList<>());

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepository = new HomeRepository(application);
        getDailyTipUseCase = new GetDailyTipUseCase(homeRepository);
        getPromotedAppsUseCase = new GetPromotedAppsUseCase(homeRepository);

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
        return homeRepository.getPlayStoreIntent();
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
        return homeRepository.getAppPlayStoreIntent(packageName);
    }
}