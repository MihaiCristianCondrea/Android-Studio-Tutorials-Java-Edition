package com.d4rk.androidtutorials.java.ui.screens.support;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;
import com.d4rk.androidtutorials.java.ui.screens.support.repository.SupportRepository;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

import java.util.List;

@HiltViewModel
public class SupportViewModel extends ViewModel {

    private final InitBillingClientUseCase initBillingClientUseCase;
    private final QueryProductDetailsUseCase queryProductDetailsUseCase;
    private final InitiatePurchaseUseCase initiatePurchaseUseCase;
    private final InitMobileAdsUseCase initMobileAdsUseCase;

    @Inject
    public SupportViewModel(InitBillingClientUseCase initBillingClientUseCase,
                            QueryProductDetailsUseCase queryProductDetailsUseCase,
                            InitiatePurchaseUseCase initiatePurchaseUseCase,
                            InitMobileAdsUseCase initMobileAdsUseCase) {
        this.initBillingClientUseCase = initBillingClientUseCase;
        this.queryProductDetailsUseCase = queryProductDetailsUseCase;
        this.initiatePurchaseUseCase = initiatePurchaseUseCase;
        this.initMobileAdsUseCase = initMobileAdsUseCase;
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