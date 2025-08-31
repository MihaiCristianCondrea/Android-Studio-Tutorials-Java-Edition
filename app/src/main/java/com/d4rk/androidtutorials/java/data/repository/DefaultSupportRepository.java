package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository.BillingFlowLauncher;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository.OnProductDetailsListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultSupportRepository implements SupportRepository {

    private final Context context;
    private final Map<String, ProductDetails> productDetailsMap = new HashMap<>();
    private BillingClient billingClient;

    public DefaultSupportRepository(Context context) {
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
                    // To be implemented in a later release
                })
                .enablePendingPurchases(
                        PendingPurchasesParams.newBuilder()
                                .enableOneTimeProducts()
                                .build())
                // Added as a best practice from the official documentation for v8+
                .enableAutoServiceReconnection()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    if (onConnected != null) {
                        onConnected.run();
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                // With enableAutoServiceReconnection(), this is handled automatically.
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

        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
        for (String id : productIds) {
            productList.add(
                    QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(id)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
            );
        }

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();

        // **FIXED**: The lambda now correctly accepts a single QueryProductDetailsResult
        // object as the second parameter, directly matching the official documentation.
        billingClient.queryProductDetailsAsync(params, (billingResult, queryProductDetailsResult) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                // The list of products is retrieved from the QueryProductDetailsResult object.
                List<ProductDetails> productDetailsList = queryProductDetailsResult.getProductDetailsList();

                if (!productDetailsList.isEmpty()) {
                    for (ProductDetails productDetails : productDetailsList) {
                        productDetailsMap.put(productDetails.getProductId(), productDetails);
                    }
                    if (listener != null) {
                        listener.onProductDetailsRetrieved(productDetailsList);
                    }
                }
                // Optionally handle unfetched products if needed:
                // List<UnfetchedProduct> unfetched = queryProductDetailsResult.getUnfetchedProductList();
            }
            // Handle other billingResult response codes here if necessary.
        });
    }


    /**
     * Launch the billing flow for a particular product.
     */
    public BillingFlowLauncher initiatePurchase(String productId) {
        ProductDetails details = productDetailsMap.get(productId);
        if (details != null && billingClient != null) {
            String offerToken = "";
            if (details.getOneTimePurchaseOfferDetails() != null) {
                offerToken = details.getOneTimePurchaseOfferDetails().getOfferToken();
            }

            String finalOfferToken = offerToken;
            List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                    Collections.singletonList(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(details)
                                    .setOfferToken(finalOfferToken)
                                    .build()
                    );

            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build();

            return activity -> billingClient.launchBillingFlow(activity, flowParams);
        }
        return null;
    }


    /**
     * Initialize Mobile Ads (usually done once in your app, but
     * can be done here if needed for the support screen).
     */
    public AdRequest initMobileAds() {
        MobileAds.initialize(context);
        return new AdRequest.Builder().build();
    }

}