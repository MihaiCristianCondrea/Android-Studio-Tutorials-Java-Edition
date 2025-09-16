package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Before;
import org.junit.Test;

public class GetDailyTipUseCaseTest {

    private HomeRepository repository;
    private GetDailyTipUseCase useCase;

    @Before
    public void setUp() {
        repository = mock(HomeRepository.class);
        useCase = new GetDailyTipUseCase(repository);
    }

    @Test
    public void invokeReturnsRepositoryTip() {
        when(repository.dailyTip()).thenReturn("Tip of the day");

        String result = useCase.invoke();

        assertEquals("Tip of the day", result);
        verify(repository).dailyTip();
    }

    @Test
    public void invokeReturnsEmptyTip() {
        when(repository.dailyTip()).thenReturn("");

        String result = useCase.invoke();

        assertEquals("", result);
        verify(repository).dailyTip();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        when(repository.dailyTip()).thenThrow(new IllegalStateException("not available"));

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).dailyTip();
    }
}
