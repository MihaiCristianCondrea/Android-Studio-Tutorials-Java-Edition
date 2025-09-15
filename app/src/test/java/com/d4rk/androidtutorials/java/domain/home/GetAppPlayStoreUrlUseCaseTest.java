package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Test;

public class GetAppPlayStoreUrlUseCaseTest {

    @Test
    public void invokeReturnsAppUrl() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getAppPlayStoreUrl("pkg")).thenReturn("url");
        GetAppPlayStoreUrlUseCase useCase = new GetAppPlayStoreUrlUseCase(repository);

        String result = useCase.invoke("pkg");

        assertEquals("url", result);
        verify(repository).getAppPlayStoreUrl("pkg");
    }

    @Test
    public void invokeDelegatesNullPackageName() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getAppPlayStoreUrl(null)).thenReturn("fallback");
        GetAppPlayStoreUrlUseCase useCase = new GetAppPlayStoreUrlUseCase(repository);

        String result = useCase.invoke(null);

        assertEquals("fallback", result);
        verify(repository).getAppPlayStoreUrl(null);
    }

    @Test
    public void invokeReturnsNullWhenRepositoryReturnsNull() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getAppPlayStoreUrl("pkg")).thenReturn(null);
        GetAppPlayStoreUrlUseCase useCase = new GetAppPlayStoreUrlUseCase(repository);

        String result = useCase.invoke("pkg");

        assertNull(result);
        verify(repository).getAppPlayStoreUrl("pkg");
    }

    @Test
    public void invokePropagatesRepositoryException() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getAppPlayStoreUrl("pkg")).thenThrow(new IllegalStateException("err"));
        GetAppPlayStoreUrlUseCase useCase = new GetAppPlayStoreUrlUseCase(repository);

        assertThrows(IllegalStateException.class, () -> useCase.invoke("pkg"));
        verify(repository).getAppPlayStoreUrl("pkg");
    }
}
