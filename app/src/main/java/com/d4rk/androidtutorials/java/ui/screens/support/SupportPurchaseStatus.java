package com.d4rk.androidtutorials.java.ui.screens.support;

/**
 * UI-facing model that describes entitlement updates for support purchases.
 */
public record SupportPurchaseStatus(String productId,
                                    com.d4rk.androidtutorials.java.ui.screens.support.SupportPurchaseStatus.State state,
                                    boolean newPurchase) {

    public enum State {
        GRANTED,
        REVOKED
    }

}
