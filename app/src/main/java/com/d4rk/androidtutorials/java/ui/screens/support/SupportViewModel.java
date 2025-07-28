package com.d4rk.androidtutorials.java.ui.screens.support;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import com.d4rk.androidtutorials.java.ui.screens.support.repository.SupportRepository;

import java.util.List;

public class SupportViewModel extends AndroidViewModel {

    private final SupportRepository repository;

    public SupportViewModel(@NonNull Application application) {
        super(application);
        repository = new SupportRepository(application);
    }

    public void initBillingClient(Runnable onConnected) {
        repository.initBillingClient(onConnected);
    }

    public void queryProductDetails(List<String> productIds,
                                   SupportRepository.OnProductDetailsListener listener) {
        repository.queryProductDetails(productIds, listener);
    }

    public void initiatePurchase(Activity activity, String productId) {
        repository.initiatePurchase(activity, productId);
    }

    public void initMobileAds(ActivitySupportBinding binding) {
        repository.initMobileAds(binding);
    }
}