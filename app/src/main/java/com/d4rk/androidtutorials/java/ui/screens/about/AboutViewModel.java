package com.d4rk.androidtutorials.java.ui.screens.about;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.d4rk.androidtutorials.java.ui.screens.about.repository.AboutRepository;
import com.d4rk.androidtutorials.java.domain.about.GetVersionStringUseCase;
import com.d4rk.androidtutorials.java.domain.about.GetCurrentYearUseCase;


/**
 * ViewModel for the About screen. Delegates data/logic to AboutRepository.
 */
public class AboutViewModel extends AndroidViewModel {

    private final AboutRepository repository;
    private final GetVersionStringUseCase getVersionStringUseCase;
    private final GetCurrentYearUseCase getCurrentYearUseCase;

    public AboutViewModel(@NonNull Application application) {
        super(application);
        repository = new AboutRepository(application);
        getVersionStringUseCase = new GetVersionStringUseCase(repository);
        getCurrentYearUseCase = new GetCurrentYearUseCase(repository);
    }

    /**
     * Returns a formatted version string, e.g. "v1.2.3 (45)".
     */
    public String getVersionString() {
        return getVersionStringUseCase.invoke();
    }

    /**
     * Returns the current year, e.g. "2024".
     */
    public String getCurrentYear() {
        return getCurrentYearUseCase.invoke();
    }
}
