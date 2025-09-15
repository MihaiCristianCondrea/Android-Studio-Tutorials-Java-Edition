package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;
import com.google.android.play.core.appupdate.AppUpdateManager;

import org.junit.Test;

public class GetAppUpdateManagerUseCaseTest {

    @Test
    public void invokeReturnsManager() {
        MainRepository repository = mock(MainRepository.class);
        AppUpdateManager manager = mock(AppUpdateManager.class);
        when(repository.getAppUpdateManager()).thenReturn(manager);
        GetAppUpdateManagerUseCase useCase = new GetAppUpdateManagerUseCase(repository);

        AppUpdateManager result = useCase.invoke();

        assertSame(manager, result);
        verify(repository).getAppUpdateManager();
    }

    @Test
    public void invokeAllowsNullManager() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getAppUpdateManager()).thenReturn(null);
        GetAppUpdateManagerUseCase useCase = new GetAppUpdateManagerUseCase(repository);

        AppUpdateManager result = useCase.invoke();

        assertNull(result);
        verify(repository).getAppUpdateManager();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getAppUpdateManager()).thenThrow(new IllegalStateException("error"));
        GetAppUpdateManagerUseCase useCase = new GetAppUpdateManagerUseCase(repository);

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).getAppUpdateManager();
    }
}
