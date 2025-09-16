package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

public class GetPromotedAppsUseCaseTest {

    private HomeRepository repository;
    private GetPromotedAppsUseCase useCase;
    private GetPromotedAppsUseCase.Callback callback;

    @Before
    public void setUp() {
        repository = mock(HomeRepository.class);
        useCase = new GetPromotedAppsUseCase(repository);
        callback = mock(GetPromotedAppsUseCase.Callback.class);
    }

    @Test
    public void invokeRequestsPromotedAppsFromRepository() {
        useCase.invoke(callback);

        verify(repository).fetchPromotedApps(any());
    }

    @Test
    public void invokeForwardsRepositoryResultsToCallback() {
        ArgumentCaptor<HomeRepository.PromotedAppsCallback> captor =
                ArgumentCaptor.forClass(HomeRepository.PromotedAppsCallback.class);

        useCase.invoke(callback);

        verify(repository).fetchPromotedApps(captor.capture());
        List<PromotedApp> promotedApps = Collections.singletonList(
                new PromotedApp("App", "package", "icon")
        );

        captor.getValue().onResult(promotedApps);

        verify(callback).onResult(promotedApps);
    }

    @Test
    public void invokeForwardsEmptyList() {
        ArgumentCaptor<HomeRepository.PromotedAppsCallback> captor =
                ArgumentCaptor.forClass(HomeRepository.PromotedAppsCallback.class);

        useCase.invoke(callback);

        verify(repository).fetchPromotedApps(captor.capture());
        List<PromotedApp> promotedApps = Collections.emptyList();

        captor.getValue().onResult(promotedApps);

        verify(callback).onResult(promotedApps);
    }

    @Test
    public void invokeForwardsNullList() {
        ArgumentCaptor<HomeRepository.PromotedAppsCallback> captor =
                ArgumentCaptor.forClass(HomeRepository.PromotedAppsCallback.class);

        useCase.invoke(callback);

        verify(repository).fetchPromotedApps(captor.capture());

        captor.getValue().onResult(null);

        verify(callback).onResult(null);
    }

    @Test
    public void invokePropagatesRepositoryExceptions() {
        doThrow(new IllegalStateException("failed"))
                .when(repository)
                .fetchPromotedApps(any());

        assertThrows(IllegalStateException.class, () -> useCase.invoke(callback));
        verify(repository).fetchPromotedApps(any());
    }
}
