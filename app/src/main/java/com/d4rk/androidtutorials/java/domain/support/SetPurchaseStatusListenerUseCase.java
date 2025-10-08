package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

/**
 * Registers a listener that receives entitlement changes.
 */
public class SetPurchaseStatusListenerUseCase {

    private final SupportRepository repository;

    public SetPurchaseStatusListenerUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke(SupportRepository.PurchaseStatusListener listener) {
        repository.setPurchaseStatusListener(listener);
    }
}
