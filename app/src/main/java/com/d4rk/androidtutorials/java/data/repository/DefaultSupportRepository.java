package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.google.android.gms.ads.AdRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultSupportRepository implements SupportRepository {

    private static final String PREFS_NAME = "support_billing";
    private static final String KEY_GRANTED_PRODUCTS = "granted_products";

    private final Context context;
    private final Map<String, ProductDetails> productDetailsMap = new HashMap<>();
    private final SharedPreferences billingPrefs;
    private final Set<String> grantedEntitlements;
    private BillingClient billingClient;
    private PurchaseStatusListener purchaseStatusListener;

    public DefaultSupportRepository(Context context) {
        this.context = context.getApplicationContext();
        this.billingPrefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> granted = billingPrefs.getStringSet(KEY_GRANTED_PRODUCTS, Collections.emptySet());
        this.grantedEntitlements = (granted != null) ? new HashSet<>(granted) : new HashSet<>();
    }

    /**
     * Initialize the billing client and start the connection.
     *
     * @param onConnected Callback once the billing service is connected.
     */
    @Override
    public void initBillingClient(Runnable onConnected) {
        billingClient = BillingClient.newBuilder(context)
                .setListener(this::handlePurchaseUpdates)
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
                    refreshPurchases();
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
    @Override
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
    @Override
    public BillingFlowLauncher initiatePurchase(String productId) {
        ProductDetails details = productDetailsMap.get(productId);
        if (details != null && billingClient != null) {
            String offerToken = "";
            if (details.getOneTimePurchaseOfferDetails() != null) {
                offerToken = details.getOneTimePurchaseOfferDetails().getOfferToken();
            }

            String finalOfferToken = offerToken;
            BillingFlowParams.ProductDetailsParams.Builder paramsBuilder =
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(details);
            if (finalOfferToken != null && !finalOfferToken.isEmpty()) {
                paramsBuilder.setOfferToken(finalOfferToken);
            }

            List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                    Collections.singletonList(paramsBuilder.build());

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
    @Override
    public AdRequest initMobileAds() {
        AdUtils.initialize(context);
        return new AdRequest.Builder().build();
    }

    @Override
    public void setPurchaseStatusListener(PurchaseStatusListener listener) {
        this.purchaseStatusListener = listener;
        if (listener != null) {
            for (String productId : grantedEntitlements) {
                listener.onPurchaseAcknowledged(productId, false);
            }
        }
    }

    @Override
    public void refreshPurchases() {
        if (billingClient == null || !billingClient.isReady()) {
            return;
        }

        QueryPurchasesParams params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build();

        billingClient.queryPurchasesAsync(params, (billingResult, purchasesList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                handlePurchaseUpdates(billingResult, purchasesList);
            }
        });
    }

    private void handlePurchaseUpdates(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK || purchases == null) {
            return;
        }

        Set<String> activeEntitlements = new HashSet<>();
        boolean shouldPersist = false;

        for (Purchase purchase : purchases) {
            List<String> productIds = purchase.getProducts();
            if (productIds.isEmpty()) {
                continue;
            }

            int rawState = getRawPurchaseState(purchase);
            if (rawState == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged()) {
                    acknowledgePurchase(purchase);
                }
                for (String productId : productIds) {
                    activeEntitlements.add(productId);
                    if (grantedEntitlements.add(productId)) {
                        shouldPersist = true;
                        notifyPurchaseAcknowledged(productId, true);
                    }
                }
            } else {
                for (String productId : productIds) {
                    if (grantedEntitlements.remove(productId)) {
                        shouldPersist = true;
                        notifyPurchaseRevoked(productId);
                    }
                }
            }
        }

        Set<String> revokedProducts = new HashSet<>(grantedEntitlements);
        revokedProducts.removeAll(activeEntitlements);
        if (!revokedProducts.isEmpty()) {
            shouldPersist = true;
        }
        for (String productId : revokedProducts) {
            if (grantedEntitlements.remove(productId)) {
                notifyPurchaseRevoked(productId);
            }
        }

        if (shouldPersist) {
            persistGrantedEntitlements();
        }
    }

    private void acknowledgePurchase(Purchase purchase) {
        if (billingClient == null) {
            return;
        }
        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.acknowledgePurchase(params, billingResult -> {
            // No-op: handled by entitlement updates once Google confirms the acknowledgement.
        });
    }

    private void notifyPurchaseAcknowledged(String productId, boolean isNewPurchase) {
        if (purchaseStatusListener != null) {
            purchaseStatusListener.onPurchaseAcknowledged(productId, isNewPurchase);
        }
    }

    private void notifyPurchaseRevoked(String productId) {
        if (purchaseStatusListener != null) {
            purchaseStatusListener.onPurchaseRevoked(productId);
        }
    }

    private int getRawPurchaseState(Purchase purchase) {
        try {
            JSONObject jsonObject = new JSONObject(purchase.getOriginalJson());
            return jsonObject.optInt("purchaseState", Purchase.PurchaseState.UNSPECIFIED_STATE);
        } catch (JSONException exception) {
            return purchase.getPurchaseState();
        }
    }

    private void persistGrantedEntitlements() {
        billingPrefs.edit()
                .putStringSet(KEY_GRANTED_PRODUCTS, new HashSet<>(grantedEntitlements))
                .apply();
    }
}
