package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

/**
 * Forces a refresh of Google Play Billing purchases to ensure entitlements are up-to-date.
 */
public class RefreshPurchasesUseCase {

    private final SupportRepository repository;

    public RefreshPurchasesUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke() {
        repository.refreshPurchases();
    }
}
