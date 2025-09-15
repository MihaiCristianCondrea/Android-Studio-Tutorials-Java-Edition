package com.d4rk.androidtutorials.java.domain.home;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

public class GetPromotedAppsUseCaseTest {

    @Test
    public void invokeCallsRepository() {
        HomeRepository repository = mock(HomeRepository.class);
        GetPromotedAppsUseCase useCase = new GetPromotedAppsUseCase(repository);

        useCase.invoke(apps -> {});

        verify(repository).fetchPromotedApps(any());
    }

    @Test
    public void invokeReturnsAppsThroughCallback() {
        HomeRepository repository = mock(HomeRepository.class);
        GetPromotedAppsUseCase useCase = new GetPromotedAppsUseCase(repository);
        GetPromotedAppsUseCase.Callback callback = mock(GetPromotedAppsUseCase.Callback.class);

        useCase.invoke(callback);

        ArgumentCaptor<HomeRepository.PromotedAppsCallback> captor =
                ArgumentCaptor.forClass(HomeRepository.PromotedAppsCallback.class);
        verify(repository).fetchPromotedApps(captor.capture());

        List<PromotedApp> apps = List.of(new PromotedApp("App", "pkg", "icon"));
        captor.getValue().onResult(apps);

        verify(callback).onResult(apps);
    }

    @Test
    public void invokePropagatesEmptyResults() {
        HomeRepository repository = mock(HomeRepository.class);
        GetPromotedAppsUseCase useCase = new GetPromotedAppsUseCase(repository);
        GetPromotedAppsUseCase.Callback callback = mock(GetPromotedAppsUseCase.Callback.class);

        useCase.invoke(callback);

        ArgumentCaptor<HomeRepository.PromotedAppsCallback> captor =
                ArgumentCaptor.forClass(HomeRepository.PromotedAppsCallback.class);
        verify(repository).fetchPromotedApps(captor.capture());

        List<PromotedApp> apps = Collections.emptyList();
        captor.getValue().onResult(apps);

        verify(callback).onResult(apps);
    }

    @Test
    public void invokePropagatesNullResults() {
        HomeRepository repository = mock(HomeRepository.class);
        GetPromotedAppsUseCase useCase = new GetPromotedAppsUseCase(repository);
        GetPromotedAppsUseCase.Callback callback = mock(GetPromotedAppsUseCase.Callback.class);

        useCase.invoke(callback);

        ArgumentCaptor<HomeRepository.PromotedAppsCallback> captor =
                ArgumentCaptor.forClass(HomeRepository.PromotedAppsCallback.class);
        verify(repository).fetchPromotedApps(captor.capture());

        captor.getValue().onResult(null);

        verify(callback).onResult(null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        HomeRepository repository = mock(HomeRepository.class);
        GetPromotedAppsUseCase useCase = new GetPromotedAppsUseCase(repository);
        GetPromotedAppsUseCase.Callback callback = mock(GetPromotedAppsUseCase.Callback.class);
        doThrow(new IllegalStateException("failure"))
                .when(repository).fetchPromotedApps(any());

        assertThrows(IllegalStateException.class, () -> useCase.invoke(callback));
        verify(repository).fetchPromotedApps(any());
    }
}
