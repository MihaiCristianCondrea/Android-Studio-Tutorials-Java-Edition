package com.d4rk.androidtutorials.java.ui.screens.support;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

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

    public SupportRepository.BillingFlowLauncher initiatePurchase(String productId) {
        return initiatePurchaseUseCase.invoke(productId);
    }

    public AdRequest initMobileAds() {
        return initMobileAdsUseCase.invoke();
    }
}