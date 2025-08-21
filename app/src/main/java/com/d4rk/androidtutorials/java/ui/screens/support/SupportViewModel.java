package com.d4rk.androidtutorials.java.ui.screens.support;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import com.d4rk.androidtutorials.java.ui.screens.support.repository.SupportRepository;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;

import java.util.List;

public class SupportViewModel extends AndroidViewModel {

    private final SupportRepository repository;
    private final InitBillingClientUseCase initBillingClientUseCase;
    private final QueryProductDetailsUseCase queryProductDetailsUseCase;
    private final InitiatePurchaseUseCase initiatePurchaseUseCase;
    private final InitMobileAdsUseCase initMobileAdsUseCase;

    public SupportViewModel(@NonNull Application application) {
        super(application);
        repository = new SupportRepository(application);
        initBillingClientUseCase = new InitBillingClientUseCase(repository);
        queryProductDetailsUseCase = new QueryProductDetailsUseCase(repository);
        initiatePurchaseUseCase = new InitiatePurchaseUseCase(repository);
        initMobileAdsUseCase = new InitMobileAdsUseCase(repository);
    }

    public void initBillingClient(Runnable onConnected) {
        initBillingClientUseCase.invoke(onConnected);
    }

    public void queryProductDetails(List<String> productIds,
                                   SupportRepository.OnProductDetailsListener listener) {
        queryProductDetailsUseCase.invoke(productIds, listener);
    }

    public void initiatePurchase(Activity activity, String productId) {
        initiatePurchaseUseCase.invoke(activity, productId);
    }

    public void initMobileAds(ActivitySupportBinding binding) {
        initMobileAdsUseCase.invoke(binding);
    }
}