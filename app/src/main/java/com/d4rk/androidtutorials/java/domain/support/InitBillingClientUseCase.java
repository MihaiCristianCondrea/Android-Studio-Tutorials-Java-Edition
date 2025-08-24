package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.ui.screens.support.repository.SupportRepository;

/** Initializes billing client and invokes callback when connected. */
public class InitBillingClientUseCase {
    private final SupportRepository repository;

    public InitBillingClientUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke(Runnable onConnected) {
        repository.initBillingClient(onConnected);
    }
}
