package com.d4rk.androidtutorials.java.domain.support;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

import org.junit.Test;

public class SetPurchaseStatusListenerUseCaseTest {

    @Test
    public void invokeRegistersListener() {
        SupportRepository repository = mock(SupportRepository.class);
        SupportRepository.PurchaseStatusListener listener = mock(SupportRepository.PurchaseStatusListener.class);
        SetPurchaseStatusListenerUseCase useCase = new SetPurchaseStatusListenerUseCase(repository);

        useCase.invoke(listener);

        verify(repository).setPurchaseStatusListener(listener);
    }
}
