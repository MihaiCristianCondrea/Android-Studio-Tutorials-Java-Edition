package com.d4rk.androidtutorials.java.domain.home;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.ui.screens.home.repository.HomeRepository;

import java.util.List;

/** Use case that fetches promoted apps from the repository. */
public class GetPromotedAppsUseCase {
    private final HomeRepository repository;

    public interface Callback {
        void onResult(List<PromotedApp> apps);
    }

    public GetPromotedAppsUseCase(HomeRepository repository) {
        this.repository = repository;
    }

    /**
     * Fetches the promoted apps and returns them via the callback.
     */
    public void invoke(Callback callback) {
        repository.fetchPromotedApps(callback::onResult);
    }
}
