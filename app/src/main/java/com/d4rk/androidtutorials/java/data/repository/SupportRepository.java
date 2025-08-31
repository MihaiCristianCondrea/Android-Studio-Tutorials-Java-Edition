package com.d4rk.androidtutorials.java.data.repository;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.data.model.AdLoadParams;
import java.util.List;

public interface SupportRepository {
    void initBillingClient(Runnable onConnected);
    void queryProductDetails(List<String> productIds, OnProductDetailsListener listener);
    void initiatePurchase(String productId, BillingFlowLauncher launcher);
    void initMobileAds(AdLoadParams params);

    interface OnProductDetailsListener {
        void onProductDetailsRetrieved(List<ProductDetails> productDetailsList);
    }

    interface BillingFlowLauncher {
        void launch(BillingClient billingClient, BillingFlowParams params);
    }
}
