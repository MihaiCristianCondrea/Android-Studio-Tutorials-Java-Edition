package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

import org.junit.Test;

public class GetBottomNavLabelVisibilityUseCaseTest {

    @Test
    public void invokeReturnsVisibilityValue() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getBottomNavLabelVisibility()).thenReturn("auto");
        GetBottomNavLabelVisibilityUseCase useCase =
                new GetBottomNavLabelVisibilityUseCase(repository);

        String result = useCase.invoke();

        assertEquals("auto", result);
        verify(repository).getBottomNavLabelVisibility();
    }

    @Test
    public void invokeReturnsNullWhenRepositoryReturnsNull() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getBottomNavLabelVisibility()).thenReturn(null);
        GetBottomNavLabelVisibilityUseCase useCase =
                new GetBottomNavLabelVisibilityUseCase(repository);

        String result = useCase.invoke();

        assertNull(result);
        verify(repository).getBottomNavLabelVisibility();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getBottomNavLabelVisibility())
                .thenThrow(new IllegalStateException("fail"));
        GetBottomNavLabelVisibilityUseCase useCase =
                new GetBottomNavLabelVisibilityUseCase(repository);

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).getBottomNavLabelVisibility();
    }
}
