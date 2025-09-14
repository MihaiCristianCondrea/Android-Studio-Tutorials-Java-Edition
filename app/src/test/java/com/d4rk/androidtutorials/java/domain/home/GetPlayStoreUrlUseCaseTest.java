package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
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
}
