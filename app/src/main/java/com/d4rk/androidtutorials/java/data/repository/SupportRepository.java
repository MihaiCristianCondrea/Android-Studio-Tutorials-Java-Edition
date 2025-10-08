package com.d4rk.androidtutorials.java.data.repository;

import android.app.Activity;

import com.android.billingclient.api.ProductDetails;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

public interface SupportRepository {
    void initBillingClient(Runnable onConnected);

    void queryProductDetails(List<String> productIds, OnProductDetailsListener listener);

    BillingFlowLauncher initiatePurchase(String productId);

    AdRequest initMobileAds();

    void setPurchaseStatusListener(PurchaseStatusListener listener);

    void refreshPurchases();

    interface OnProductDetailsListener {
        void onProductDetailsRetrieved(List<ProductDetails> productDetailsList);
    }

    interface BillingFlowLauncher {
        void launch(Activity activity);
    }

    interface PurchaseStatusListener {
        void onPurchaseAcknowledged(String productId, boolean isNewPurchase);

        void onPurchaseRevoked(String productId);
    }
}
