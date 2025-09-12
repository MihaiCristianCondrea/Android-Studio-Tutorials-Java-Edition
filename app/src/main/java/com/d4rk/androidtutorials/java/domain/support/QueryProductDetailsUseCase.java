package com.d4rk.androidtutorials.java.domain.support;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

import java.util.List;

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
