package com.d4rk.androidtutorials.java.domain.support;

import java.util.List;
import com.d4rk.androidtutorials.java.ui.screens.support.repository.SupportRepository;

/** Queries in-app product details. */
public class QueryProductDetailsUseCase {
    private final SupportRepository repository;

    public QueryProductDetailsUseCase(SupportRepository repository) {
        this.repository = repository;
    }

    public void invoke(List<String> productIds,
                       SupportRepository.OnProductDetailsListener listener) {
        repository.queryProductDetails(productIds, listener);
    }
}
