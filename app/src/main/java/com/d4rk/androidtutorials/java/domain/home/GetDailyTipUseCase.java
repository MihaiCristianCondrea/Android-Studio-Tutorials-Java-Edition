package com.d4rk.androidtutorials.java.domain.home;

import com.d4rk.androidtutorials.java.ui.screens.home.repository.HomeRepository;

/** Use case that provides the daily tip text. */
public class GetDailyTipUseCase {
    private final HomeRepository repository;

    public GetDailyTipUseCase(HomeRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns today's tip string.
     */
    public String invoke() {
        return repository.getDailyTip();
    }
}
