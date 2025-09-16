package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Before;
import org.junit.Test;

public class GetPlayStoreUrlUseCaseTest {

    private HomeRepository repository;
    private GetPlayStoreUrlUseCase useCase;

    @Before
    public void setUp() {
        repository = mock(HomeRepository.class);
        useCase = new GetPlayStoreUrlUseCase(repository);
    }

    @Test
    public void invokeReturnsRepositoryUrl() {
        when(repository.getPlayStoreUrl()).thenReturn("https://play.store/app");

        String result = useCase.invoke();

        assertEquals("https://play.store/app", result);
        verify(repository).getPlayStoreUrl();
    }

    @Test
    public void invokeReturnsEmptyUrlWhenRepositoryReturnsEmpty() {
        when(repository.getPlayStoreUrl()).thenReturn("");

        String result = useCase.invoke();

        assertEquals("", result);
        verify(repository).getPlayStoreUrl();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        when(repository.getPlayStoreUrl()).thenThrow(new IllegalStateException("missing"));

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).getPlayStoreUrl();
    }
}
