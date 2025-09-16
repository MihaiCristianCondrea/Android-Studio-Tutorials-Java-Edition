package com.d4rk.androidtutorials.java.ui.screens.support;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.google.android.gms.ads.AdRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SupportViewModelTest {

    @Mock
    private InitBillingClientUseCase initBillingClientUseCase;

    @Mock
    private QueryProductDetailsUseCase queryProductDetailsUseCase;

    @Mock
    private InitiatePurchaseUseCase initiatePurchaseUseCase;

    @Mock
    private InitMobileAdsUseCase initMobileAdsUseCase;

    @Test
    public void initBillingClient_delegatesToUseCase() {
        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        Runnable onConnected = mock(Runnable.class);

        viewModel.initBillingClient(onConnected);

        verify(initBillingClientUseCase).invoke(onConnected);
    }

    @Test
    public void initBillingClient_propagatesFailure() {
        doThrow(new IllegalStateException("error"))
                .when(initBillingClientUseCase)
                .invoke(any());

        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        Runnable onConnected = mock(Runnable.class);

        assertThrows(IllegalStateException.class, () -> viewModel.initBillingClient(onConnected));
    }

    @Test
    public void queryProductDetails_delegatesToUseCase() {
        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        List<String> productIds = List.of("one", "two");
        SupportRepository.OnProductDetailsListener listener =
                mock(SupportRepository.OnProductDetailsListener.class);

        viewModel.queryProductDetails(productIds, listener);

        verify(queryProductDetailsUseCase).invoke(productIds, listener);
    }

    @Test
    public void queryProductDetails_propagatesFailure() {
        List<String> productIds = List.of("id");
        SupportRepository.OnProductDetailsListener listener =
                mock(SupportRepository.OnProductDetailsListener.class);

        doThrow(new RuntimeException("boom"))
                .when(queryProductDetailsUseCase)
                .invoke(eq(productIds), eq(listener));

        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        assertThrows(RuntimeException.class,
                () -> viewModel.queryProductDetails(productIds, listener));
    }

    @Test
    public void initiatePurchase_delegatesToUseCase() {
        SupportRepository.BillingFlowLauncher launcher = mock(SupportRepository.BillingFlowLauncher.class);
        when(initiatePurchaseUseCase.invoke("product")).thenReturn(launcher);

        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        SupportRepository.BillingFlowLauncher result = viewModel.initiatePurchase("product");

        assertSame(launcher, result);
        verify(initiatePurchaseUseCase).invoke("product");
    }

    @Test
    public void initiatePurchase_propagatesFailure() {
        doThrow(new IllegalArgumentException("bad"))
                .when(initiatePurchaseUseCase)
                .invoke("missing");

        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        assertThrows(IllegalArgumentException.class,
                () -> viewModel.initiatePurchase("missing"));
    }

    @Test
    public void initMobileAds_returnsResult() {
        AdRequest adRequest = mock(AdRequest.class);
        when(initMobileAdsUseCase.invoke()).thenReturn(adRequest);

        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        AdRequest result = viewModel.initMobileAds();

        assertSame(adRequest, result);
        verify(initMobileAdsUseCase).invoke();
    }

    @Test
    public void initMobileAds_propagatesFailure() {
        doThrow(new IllegalStateException("ads"))
                .when(initMobileAdsUseCase)
                .invoke();

        SupportViewModel viewModel = new SupportViewModel(
                initBillingClientUseCase,
                queryProductDetailsUseCase,
                initiatePurchaseUseCase,
                initMobileAdsUseCase
        );

        assertThrows(IllegalStateException.class, viewModel::initMobileAds);
    }
}

