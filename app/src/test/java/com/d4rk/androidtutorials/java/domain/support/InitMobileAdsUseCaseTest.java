package com.d4rk.androidtutorials.java.domain.support;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.google.android.gms.ads.AdRequest;

import org.junit.Test;

public class InitMobileAdsUseCaseTest {

    @Test
    public void invokeReturnsAdRequest() {
        SupportRepository repository = mock(SupportRepository.class);
        AdRequest adRequest = mock(AdRequest.class);
        when(repository.initMobileAds()).thenReturn(adRequest);
        InitMobileAdsUseCase useCase = new InitMobileAdsUseCase(repository);

        AdRequest result = useCase.invoke();

        assertSame(adRequest, result);
        verify(repository).initMobileAds();
    }

    @Test
    public void invokeAllowsNullResult() {
        SupportRepository repository = mock(SupportRepository.class);
        when(repository.initMobileAds()).thenReturn(null);
        InitMobileAdsUseCase useCase = new InitMobileAdsUseCase(repository);

        AdRequest result = useCase.invoke();

        assertNull(result);
        verify(repository).initMobileAds();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SupportRepository repository = mock(SupportRepository.class);
        when(repository.initMobileAds()).thenThrow(new IllegalStateException("boom"));
        InitMobileAdsUseCase useCase = new InitMobileAdsUseCase(repository);

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).initMobileAds();
    }
}
