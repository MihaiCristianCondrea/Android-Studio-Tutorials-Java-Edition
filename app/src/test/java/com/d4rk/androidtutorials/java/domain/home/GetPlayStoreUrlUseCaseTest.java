package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Test;

public class GetPlayStoreUrlUseCaseTest {

    @Test
    public void invokeReturnsUrl() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getPlayStoreUrl()).thenReturn("url");
        GetPlayStoreUrlUseCase useCase = new GetPlayStoreUrlUseCase(repository);

        String result = useCase.invoke();

        assertEquals("url", result);
        verify(repository).getPlayStoreUrl();
    }

    @Test
    public void invokeReturnsNullWhenRepositoryReturnsNull() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getPlayStoreUrl()).thenReturn(null);
        GetPlayStoreUrlUseCase useCase = new GetPlayStoreUrlUseCase(repository);

        String result = useCase.invoke();

        assertNull(result);
        verify(repository).getPlayStoreUrl();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.getPlayStoreUrl()).thenThrow(new RuntimeException("fail"));
        GetPlayStoreUrlUseCase useCase = new GetPlayStoreUrlUseCase(repository);

        assertThrows(RuntimeException.class, useCase::invoke);
        verify(repository).getPlayStoreUrl();
    }
}
