package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Test;

public class GetDailyTipUseCaseTest {

    @Test
    public void invokeReturnsDailyTip() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.dailyTip()).thenReturn("tip");
        GetDailyTipUseCase useCase = new GetDailyTipUseCase(repository);

        String result = useCase.invoke();

        assertEquals("tip", result);
        verify(repository).dailyTip();
    }

    @Test
    public void invokeHandlesEmptyTip() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.dailyTip()).thenReturn("");
        GetDailyTipUseCase useCase = new GetDailyTipUseCase(repository);

        String result = useCase.invoke();

        assertEquals("", result);
        verify(repository).dailyTip();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        HomeRepository repository = mock(HomeRepository.class);
        when(repository.dailyTip()).thenThrow(new IllegalArgumentException("bad"));
        GetDailyTipUseCase useCase = new GetDailyTipUseCase(repository);

        assertThrows(IllegalArgumentException.class, useCase::invoke);
        verify(repository).dailyTip();
    }
}
