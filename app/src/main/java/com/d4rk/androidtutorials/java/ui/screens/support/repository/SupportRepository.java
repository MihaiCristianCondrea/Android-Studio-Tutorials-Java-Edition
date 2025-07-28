package com.d4rk.androidtutorials.java.ui.screens.support.repository;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @noinspection deprecation
 */
public class SupportRepository {

    private final Context context;
    private final Map<String, ProductDetails> productDetailsMap = new HashMap<>();
    private BillingClient billingClient;

    public SupportRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Initialize the billing client and start the connection.
     *
     * @param onConnected Callback once the billing service is connected.
     */
    public void initBillingClient(Runnable onConnected) {
        billingClient = BillingClient.newBuilder(context)
                .setListener((billingResult, purchases) -> {
                })
                .enablePendingPurchases(
                        PendingPurchasesParams.newBuilder()
                                .enableOneTimeProducts()
                                .build())
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Billing service connected
                    if (onConnected != null) {
                        onConnected.run();
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Attempt reconnection or handle gracefully
            }
        });
    }

    /**
     * Query your product details for in-app items.
     * Typically called after billing client is connected.
     */
    public void queryProductDetails(List<String> productIds, OnProductDetailsListener listener) {
        if (billingClient == null || !billingClient.isReady()) {
            return;
        }

        List<QueryProductDetailsParams.Product> products = new ArrayList<>();
        for (String id : productIds) {
            products.add(QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build());
        }

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, result) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && result != null) {
                List<ProductDetails> productDetailsList = result.getProductDetailsList();
                if (productDetailsList != null) {
                    for (ProductDetails productDetails : productDetailsList) {
                        productDetailsMap.put(productDetails.getProductId(), productDetails);
                    }
                    if (listener != null) {
                        listener.onProductDetailsRetrieved(productDetailsList);
                    }
                }
            }
        });
    }

    /**
     * Launch the billing flow for a particular product.
     */
    public void initiatePurchase(Activity activity, String productId) {
        if (productDetailsMap.containsKey(productId)) {
            ProductDetails details = productDetailsMap.get(productId);
            if (details != null) {
                BillingFlowParams.ProductDetailsParams productParams =
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(details)
                                .build();
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(List.of(productParams))
                        .build();
                billingClient.launchBillingFlow(activity, flowParams);
            }
        }
    }

    /**
     * Initialize Mobile Ads (usually done once in your app, but
     * can be done here if needed for the support screen).
     */
    public void initMobileAds(ActivitySupportBinding binding) {
        MobileAds.initialize(context);
        binding.largeBannerAd.loadAd(new AdRequest.Builder().build());
    }

    /**
     * Callback interface for when product details are fetched.
     */
    public interface OnProductDetailsListener {
        void onProductDetailsRetrieved(List<ProductDetails> productDetailsList);
    }

}