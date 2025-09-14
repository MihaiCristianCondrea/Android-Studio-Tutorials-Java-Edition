package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
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
}
