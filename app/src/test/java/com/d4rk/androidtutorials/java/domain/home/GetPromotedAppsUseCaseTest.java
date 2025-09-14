package com.d4rk.androidtutorials.java.domain.home;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
}
