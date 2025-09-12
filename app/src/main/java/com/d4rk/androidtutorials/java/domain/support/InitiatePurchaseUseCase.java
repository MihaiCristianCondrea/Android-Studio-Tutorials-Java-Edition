package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

/**
 * Launches billing flow for a product.
 */
public class InitiatePurchaseUseCase {
    private final SupportRepository repository;

    public InitiatePurchaseUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public SupportRepository.BillingFlowLauncher invoke(String productId) {
        return repository.initiatePurchase(productId);
    }
}
