package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Before;
import org.junit.Test;

public class GetAppPlayStoreUrlUseCaseTest {

    private static final String PACKAGE_NAME = "com.example.pkg";

    private HomeRepository repository;
    private GetAppPlayStoreUrlUseCase useCase;

    @Before
    public void setUp() {
        repository = mock(HomeRepository.class);
        useCase = new GetAppPlayStoreUrlUseCase(repository);
    }

    @Test
    public void invokeReturnsUrlForPackage() {
        when(repository.getAppPlayStoreUrl(PACKAGE_NAME)).thenReturn("https://play.store/app/com.example.pkg");

        String result = useCase.invoke(PACKAGE_NAME);

        assertEquals("https://play.store/app/com.example.pkg", result);
        verify(repository).getAppPlayStoreUrl(PACKAGE_NAME);
    }

    @Test
    public void invokeReturnsEmptyUrlWhenRepositoryReturnsEmpty() {
        when(repository.getAppPlayStoreUrl(PACKAGE_NAME)).thenReturn("");

        String result = useCase.invoke(PACKAGE_NAME);

        assertEquals("", result);
        verify(repository).getAppPlayStoreUrl(PACKAGE_NAME);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        when(repository.getAppPlayStoreUrl(PACKAGE_NAME))
                .thenThrow(new IllegalArgumentException("invalid package"));

        assertThrows(IllegalArgumentException.class, () -> useCase.invoke(PACKAGE_NAME));
        verify(repository).getAppPlayStoreUrl(PACKAGE_NAME);
    }
}
