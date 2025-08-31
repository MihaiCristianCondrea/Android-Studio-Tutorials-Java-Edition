package com.d4rk.androidtutorials.java.ui.screens.home;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    private final MutableLiveData<HomeUiState> uiState = new MutableLiveData<>();

    @Inject
    public HomeViewModel(Application application,
                         HomeRepository homeRepository,
                         GetDailyTipUseCase getDailyTipUseCase,
                         GetPromotedAppsUseCase getPromotedAppsUseCase) {
        this.application = application;
        this.homeRepository = homeRepository;
        this.getDailyTipUseCase = getDailyTipUseCase;
        this.getPromotedAppsUseCase = getPromotedAppsUseCase;

        HomeUiState initialState = new HomeUiState(
                application.getString(R.string.announcement_title),
                application.getString(R.string.announcement_subtitle),
                getDailyTipUseCase.invoke(),
                new ArrayList<>()
        );
        uiState.setValue(initialState);

        getPromotedAppsUseCase.invoke(apps -> {
            List<PromotedApp> result;
            if (apps.isEmpty()) {
                result = apps;
            } else {
                int startIndex = (int) ((System.currentTimeMillis() / (24L * 60 * 60 * 1000)) % apps.size());
                result = new ArrayList<>();
                for (int i = 0; i < Math.min(4, apps.size()); i++) {
                    result.add(apps.get((startIndex + i) % apps.size()));
                }
            }
            HomeUiState current = uiState.getValue();
            if (current == null) {
                current = new HomeUiState("", "", "", result);
            } else {
                current = new HomeUiState(
                        current.announcementTitle(),
                        current.announcementSubtitle(),
                        current.dailyTip(),
                        result
                );
            }
            uiState.postValue(current);
        });
    }

    /**
     * Exposes the UI state for the Home screen.
     */
    public LiveData<HomeUiState> getUiState() {
        return uiState;
    }

    /**
     * Returns an Intent that opens the Google Play Store page for your app.
     * The HomeFragment can startActivity(...) on it.
     */
    public Intent getOpenPlayStoreIntent() {
        return buildPlayStoreIntent(homeRepository.getPlayStoreUrl());
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