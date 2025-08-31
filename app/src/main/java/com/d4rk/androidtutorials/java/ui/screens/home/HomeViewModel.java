package com.d4rk.androidtutorials.java.ui.screens.home;

import android.content.Intent;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetAppPlayStoreUrlUseCase;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;


@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final GetDailyTipUseCase getDailyTipUseCase;
    private final GetPromotedAppsUseCase getPromotedAppsUseCase;
    private final GetPlayStoreUrlUseCase getPlayStoreUrlUseCase;
    private final GetAppPlayStoreUrlUseCase getAppPlayStoreUrlUseCase;

    private final MutableLiveData<HomeUiState> uiState = new MutableLiveData<>();

    @Inject
    public HomeViewModel(GetDailyTipUseCase getDailyTipUseCase,
                         GetPromotedAppsUseCase getPromotedAppsUseCase,
                         GetPlayStoreUrlUseCase getPlayStoreUrlUseCase,
                         GetAppPlayStoreUrlUseCase getAppPlayStoreUrlUseCase) {
        this.getDailyTipUseCase = getDailyTipUseCase;
        this.getPromotedAppsUseCase = getPromotedAppsUseCase;
        this.getPlayStoreUrlUseCase = getPlayStoreUrlUseCase;
        this.getAppPlayStoreUrlUseCase = getAppPlayStoreUrlUseCase;

        HomeUiState initialState = new HomeUiState(
                "",
                "",
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

    public void setAnnouncements(String title, String subtitle) {
        HomeUiState current = uiState.getValue();
        if (current == null) {
            current = new HomeUiState(title, subtitle, getDailyTipUseCase.invoke(), new ArrayList<>());
        } else {
            current = new HomeUiState(title, subtitle, current.dailyTip(), current.promotedApps());
        }
        uiState.setValue(current);
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
        return buildPlayStoreIntent(getPlayStoreUrlUseCase.invoke());
    }

    public Intent getLearnMoreIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"));
    }

    /**
     * Builds an intent to open the Google Play listing for the provided package.
     */
    public Intent getPromotedAppIntent(String packageName) {
        return buildPlayStoreIntent(getAppPlayStoreUrlUseCase.invoke(packageName));
    }

    private Intent buildPlayStoreIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
}