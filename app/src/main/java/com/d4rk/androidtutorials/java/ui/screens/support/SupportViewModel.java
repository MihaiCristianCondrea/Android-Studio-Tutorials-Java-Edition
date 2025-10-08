package com.d4rk.androidtutorials.java.ui.screens.support;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.d4rk.androidtutorials.java.domain.support.RefreshPurchasesUseCase;
import com.d4rk.androidtutorials.java.domain.support.SetPurchaseStatusListenerUseCase;
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
    private final RefreshPurchasesUseCase refreshPurchasesUseCase;
    private final SetPurchaseStatusListenerUseCase setPurchaseStatusListenerUseCase;
    private final MutableLiveData<SupportPurchaseStatus> purchaseStatus = new MutableLiveData<>();

    @Inject
    public SupportViewModel(InitBillingClientUseCase initBillingClientUseCase,
                            QueryProductDetailsUseCase queryProductDetailsUseCase,
                            InitiatePurchaseUseCase initiatePurchaseUseCase,
                            InitMobileAdsUseCase initMobileAdsUseCase,
                            RefreshPurchasesUseCase refreshPurchasesUseCase,
                            SetPurchaseStatusListenerUseCase setPurchaseStatusListenerUseCase) {
        this.initBillingClientUseCase = initBillingClientUseCase;
        this.queryProductDetailsUseCase = queryProductDetailsUseCase;
        this.initiatePurchaseUseCase = initiatePurchaseUseCase;
        this.initMobileAdsUseCase = initMobileAdsUseCase;
        this.refreshPurchasesUseCase = refreshPurchasesUseCase;
        this.setPurchaseStatusListenerUseCase = setPurchaseStatusListenerUseCase;
    }

    public void initBillingClient(Runnable onConnected) {
        initBillingClientUseCase.invoke(() -> {
            refreshPurchasesUseCase.invoke();
            if (onConnected != null) {
                onConnected.run();
            }
        });
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

    public LiveData<SupportPurchaseStatus> getPurchaseStatus() {
        return purchaseStatus;
    }

    public void registerPurchaseStatusListener() {
        setPurchaseStatusListenerUseCase.invoke(new SupportRepository.PurchaseStatusListener() {
            @Override
            public void onPurchaseAcknowledged(String productId, boolean isNewPurchase) {
                purchaseStatus.postValue(new SupportPurchaseStatus(
                        productId,
                        SupportPurchaseStatus.State.GRANTED,
                        isNewPurchase
                ));
            }

            @Override
            public void onPurchaseRevoked(String productId) {
                purchaseStatus.postValue(new SupportPurchaseStatus(
                        productId,
                        SupportPurchaseStatus.State.REVOKED,
                        false
                ));
            }
        });
    }

    public void refreshPurchases() {
        refreshPurchasesUseCase.invoke();
    }
}