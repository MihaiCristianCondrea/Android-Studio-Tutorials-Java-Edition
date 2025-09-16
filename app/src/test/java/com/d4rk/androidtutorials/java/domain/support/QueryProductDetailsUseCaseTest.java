package com.d4rk.androidtutorials.java.domain.support;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class QueryProductDetailsUseCaseTest {

    @Test
    public void invokeDelegatesToRepository() {
        SupportRepository repository = mock(SupportRepository.class);
        List<String> productIds = List.of("one", "two");
        SupportRepository.OnProductDetailsListener listener =
                mock(SupportRepository.OnProductDetailsListener.class);
        QueryProductDetailsUseCase useCase = new QueryProductDetailsUseCase(repository);

        useCase.invoke(productIds, listener);

        verify(repository).queryProductDetails(productIds, listener);
    }

    @Test
    public void invokeSupportsEmptyProductIds() {
        SupportRepository repository = mock(SupportRepository.class);
        SupportRepository.OnProductDetailsListener listener =
                mock(SupportRepository.OnProductDetailsListener.class);
        QueryProductDetailsUseCase useCase = new QueryProductDetailsUseCase(repository);
        List<String> ids = Collections.emptyList();

        useCase.invoke(ids, listener);

        verify(repository).queryProductDetails(ids, listener);
    }

    @Test
    public void invokePropagatesListenerResults() {
        SupportRepository repository = mock(SupportRepository.class);
        List<String> productIds = List.of("product");
        SupportRepository.OnProductDetailsListener listener =
                mock(SupportRepository.OnProductDetailsListener.class);
        QueryProductDetailsUseCase useCase = new QueryProductDetailsUseCase(repository);
        ProductDetails detail = mock(ProductDetails.class);
        List<ProductDetails> details = List.of(detail);
        doAnswer(invocation -> {
            SupportRepository.OnProductDetailsListener callback = invocation.getArgument(1);
            callback.onProductDetailsRetrieved(details);
            return null;
        }).when(repository).queryProductDetails(productIds, listener);

        useCase.invoke(productIds, listener);

        verify(repository).queryProductDetails(productIds, listener);
        verify(listener).onProductDetailsRetrieved(details);
    }

    @Test
    public void invokeSupportsNullArguments() {
        SupportRepository repository = mock(SupportRepository.class);
        QueryProductDetailsUseCase useCase = new QueryProductDetailsUseCase(repository);

        useCase.invoke(null, null);

        verify(repository).queryProductDetails(null, null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SupportRepository repository = mock(SupportRepository.class);
        List<String> productIds = List.of("id");
        SupportRepository.OnProductDetailsListener listener =
                mock(SupportRepository.OnProductDetailsListener.class);
        QueryProductDetailsUseCase useCase = new QueryProductDetailsUseCase(repository);
        doThrow(new IllegalStateException("failure"))
                .when(repository).queryProductDetails(productIds, listener);

        assertThrows(IllegalStateException.class,
                () -> useCase.invoke(productIds, listener));
        verify(repository).queryProductDetails(productIds, listener);
    }
}
