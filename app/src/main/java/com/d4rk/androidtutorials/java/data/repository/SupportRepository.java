package com.d4rk.androidtutorials.java.data.repository;

import android.app.Activity;
import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import java.util.List;

public interface SupportRepository {
    void initBillingClient(Runnable onConnected);
    void queryProductDetails(List<String> productIds, OnProductDetailsListener listener);
    void initiatePurchase(Activity activity, String productId);
    void initMobileAds(ActivitySupportBinding binding);

    interface OnProductDetailsListener {
        void onProductDetailsRetrieved(List<ProductDetails> productDetailsList);
    }
}
