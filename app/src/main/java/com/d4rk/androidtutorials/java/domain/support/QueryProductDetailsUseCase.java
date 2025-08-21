package com.d4rk.androidtutorials.java.domain.support;

import java.util.List;
import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.ui.screens.support.repository.SupportRepository;

/** Queries in-app product details. */
public class QueryProductDetailsUseCase {
    private final SupportRepository repository;

    public interface Listener extends SupportRepository.OnProductDetailsListener {}

    public QueryProductDetailsUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke(List<String> productIds, Listener listener) {
        repository.queryProductDetails(productIds, listener);
    }
}
