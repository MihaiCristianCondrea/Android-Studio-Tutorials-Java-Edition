package com.d4rk.androidtutorials.java.domain.support;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

import org.junit.Test;

public class InitiatePurchaseUseCaseTest {

    @Test
    public void invokeReturnsLauncher() {
        SupportRepository repository = mock(SupportRepository.class);
        SupportRepository.BillingFlowLauncher launcher = mock(SupportRepository.BillingFlowLauncher.class);
        when(repository.initiatePurchase("sku")).thenReturn(launcher);
        InitiatePurchaseUseCase useCase = new InitiatePurchaseUseCase(repository);

        SupportRepository.BillingFlowLauncher result = useCase.invoke("sku");

        assertSame(launcher, result);
        verify(repository).initiatePurchase("sku");
    }

    @Test
    public void invokeAllowsNullProductId() {
        SupportRepository repository = mock(SupportRepository.class);
        SupportRepository.BillingFlowLauncher launcher = mock(SupportRepository.BillingFlowLauncher.class);
        when(repository.initiatePurchase(null)).thenReturn(launcher);
        InitiatePurchaseUseCase useCase = new InitiatePurchaseUseCase(repository);

        SupportRepository.BillingFlowLauncher result = useCase.invoke(null);

        assertSame(launcher, result);
        verify(repository).initiatePurchase(null);
    }

    @Test
    public void invokeAllowsNullLauncher() {
        SupportRepository repository = mock(SupportRepository.class);
        when(repository.initiatePurchase("sku")).thenReturn(null);
        InitiatePurchaseUseCase useCase = new InitiatePurchaseUseCase(repository);

        SupportRepository.BillingFlowLauncher result = useCase.invoke("sku");

        assertNull(result);
        verify(repository).initiatePurchase("sku");
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SupportRepository repository = mock(SupportRepository.class);
        when(repository.initiatePurchase("sku")).thenThrow(new IllegalStateException("error"));
        InitiatePurchaseUseCase useCase = new InitiatePurchaseUseCase(repository);

        assertThrows(IllegalStateException.class, () -> useCase.invoke("sku"));
        verify(repository).initiatePurchase("sku");
    }
}
