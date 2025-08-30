package com.d4rk.androidtutorials.java.domain.help;

import com.d4rk.androidtutorials.java.data.repository.HelpRepository;

/** Requests the Google Play review flow. */
public class RequestReviewFlowUseCase {
    private final HelpRepository repository;

    public RequestReviewFlowUseCase(HelpRepository repository) {
        this.repository = repository;
    }

    public void invoke(HelpRepository.OnReviewInfoListener listener) {
        repository.requestReviewFlow(listener);
    }
}
