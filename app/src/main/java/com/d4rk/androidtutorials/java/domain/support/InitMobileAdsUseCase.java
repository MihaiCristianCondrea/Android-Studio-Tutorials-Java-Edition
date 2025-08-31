package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.data.model.AdLoadParams;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

/** Initializes Google Mobile Ads. */
public class InitMobileAdsUseCase {
    private final SupportRepository repository;

    public InitMobileAdsUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke(AdLoadParams params) {
        repository.initMobileAds(params);
    }
}
