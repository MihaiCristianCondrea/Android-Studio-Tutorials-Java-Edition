package com.d4rk.androidtutorials.java.domain.support;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

import org.junit.Test;

public class RefreshPurchasesUseCaseTest {

    @Test
    public void invokeRequestsRepositoryRefresh() {
        SupportRepository repository = mock(SupportRepository.class);
        RefreshPurchasesUseCase useCase = new RefreshPurchasesUseCase(repository);

        useCase.invoke();

        verify(repository).refreshPurchases();
    }
}
