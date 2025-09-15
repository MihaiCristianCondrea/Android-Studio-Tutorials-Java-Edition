package com.d4rk.androidtutorials.java.domain.about;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.AboutRepository;

import org.junit.Test;

public class GetCurrentYearUseCaseTest {

    @Test
    public void invokeReturnsRepositoryCurrentYear() {
        AboutRepository repository = mock(AboutRepository.class);
        when(repository.getCurrentYear()).thenReturn("2030");
        GetCurrentYearUseCase useCase = new GetCurrentYearUseCase(repository);

        String result = useCase.invoke();

        assertEquals("2030", result);
        verify(repository).getCurrentYear();
    }
}
