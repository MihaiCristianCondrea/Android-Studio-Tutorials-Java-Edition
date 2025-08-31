package com.d4rk.androidtutorials.java.data.repository;

import android.app.Activity;
import com.google.android.gms.ads.AdRequest;
import com.android.billingclient.api.ProductDetails;
import java.util.List;

public interface SupportRepository {
    void initBillingClient(Runnable onConnected);
    void queryProductDetails(List<String> productIds, OnProductDetailsListener listener);
    BillingFlowLauncher initiatePurchase(String productId);
    AdRequest initMobileAds();

    interface OnProductDetailsListener {
        void onProductDetailsRetrieved(List<ProductDetails> productDetailsList);
    }

    interface BillingFlowLauncher {
        void launch(Activity activity);
    }
}
