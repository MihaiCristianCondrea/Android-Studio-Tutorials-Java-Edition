package com.d4rk.androidtutorials.java.domain.support;

import android.app.Activity;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

/** Launches billing flow for a product. */
public class InitiatePurchaseUseCase {
    private final SupportRepository repository;

    public InitiatePurchaseUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke(Activity activity, String productId) {
        repository.initiatePurchase(activity, productId);
    }
}
