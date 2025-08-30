package com.d4rk.androidtutorials.java.domain.about;

import com.d4rk.androidtutorials.java.data.repository.AboutRepository;

/** Provides current year as a string. */
public class GetCurrentYearUseCase {
    private final AboutRepository repository;

    public GetCurrentYearUseCase(AboutRepository repository) {
        this.repository = repository;
    }

    public String invoke() {
        return repository.getCurrentYear();
    }
}
