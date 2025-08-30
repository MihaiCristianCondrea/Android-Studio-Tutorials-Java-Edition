package com.d4rk.androidtutorials.java.domain.about;

import com.d4rk.androidtutorials.java.data.repository.AboutRepository;

/** Returns the formatted app version string. */
public class GetVersionStringUseCase {
    private final AboutRepository repository;

    public GetVersionStringUseCase(AboutRepository repository) {
        this.repository = repository;
    }

    public String invoke() {
        return repository.getVersionString();
    }
}
