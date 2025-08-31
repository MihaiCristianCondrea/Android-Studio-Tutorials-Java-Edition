package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.google.android.gms.ads.AdRequest;

/** Initializes Google Mobile Ads. */
public class InitMobileAdsUseCase {
    private final SupportRepository repository;

    public InitMobileAdsUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public AdRequest invoke() {
        return repository.initMobileAds();
    }
}
