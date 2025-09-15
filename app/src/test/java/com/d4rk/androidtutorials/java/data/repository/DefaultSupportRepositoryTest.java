package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.google.android.gms.ads.AdRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultSupportRepositoryTest {

    private Context context;
    private DefaultSupportRepository repository;

    @Before
    public void setUp() {
        context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        repository = new DefaultSupportRepository(context);
    }

    @Test
    public void initBillingClient_buildsClientAndRunsCallbackWhenReady() throws ReflectiveOperationException {
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        BillingClient billingClient = mock(BillingClient.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);

        when(builder.setListener(any())).thenReturn(builder);
        when(builder.enablePendingPurchases(any())).thenReturn(builder);
        when(builder.enableAutoServiceReconnection()).thenReturn(builder);
        when(builder.build()).thenReturn(billingClient);
        when(pendingBuilder.enableOneTimeProducts()).thenReturn(pendingBuilder);
        when(pendingBuilder.build()).thenReturn(pendingParams);

        try (MockedStatic<BillingClient> billingClientStatic = mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingStatic = mockStatic(PendingPurchasesParams.class)) {
            billingClientStatic.when(() -> BillingClient.newBuilder(context)).thenReturn(builder);
            pendingStatic.when(PendingPurchasesParams::newBuilder).thenReturn(pendingBuilder);

            AtomicReference<BillingClientStateListener> listenerRef = new AtomicReference<>();
            doAnswer(invocation -> {
                BillingClientStateListener listener = invocation.getArgument(0);
                listenerRef.set(listener);
                return null;
            }).when(billingClient).startConnection(any());

            Runnable onConnected = mock(Runnable.class);
            repository.initBillingClient(onConnected);

            billingClientStatic.verify(() -> BillingClient.newBuilder(context));
            pendingStatic.verify(PendingPurchasesParams::newBuilder);
            verify(builder).setListener(any());
            verify(builder).enablePendingPurchases(pendingParams);
            verify(builder).enableAutoServiceReconnection();
            verify(builder).build();
            verify(billingClient).startConnection(any());

            BillingResult okResult = mock(BillingResult.class);
            when(okResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
            listenerRef.get().onBillingSetupFinished(okResult);
            verify(onConnected).run();

            BillingResult errorResult = mock(BillingResult.class);
            when(errorResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.ERROR);
            listenerRef.get().onBillingSetupFinished(errorResult);
            verify(onConnected, times(1)).run();

            setBillingClient(repository, billingClient);
        }
    }

    @Test
    public void initBillingClient_handlesNullCallbackGracefully() {
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        BillingClient billingClient = mock(BillingClient.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);

        when(builder.setListener(any())).thenReturn(builder);
        when(builder.enablePendingPurchases(any())).thenReturn(builder);
        when(builder.enableAutoServiceReconnection()).thenReturn(builder);
        when(builder.build()).thenReturn(billingClient);
        when(pendingBuilder.enableOneTimeProducts()).thenReturn(pendingBuilder);
        when(pendingBuilder.build()).thenReturn(pendingParams);

        try (MockedStatic<BillingClient> billingClientStatic = mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingStatic = mockStatic(PendingPurchasesParams.class)) {
            billingClientStatic.when(() -> BillingClient.newBuilder(context)).thenReturn(builder);
            pendingStatic.when(PendingPurchasesParams::newBuilder).thenReturn(pendingBuilder);

            AtomicReference<BillingClientStateListener> listenerRef = new AtomicReference<>();
            doAnswer(invocation -> {
                listenerRef.set(invocation.getArgument(0));
                return null;
            }).when(billingClient).startConnection(any());

            repository.initBillingClient(null);
            BillingResult okResult = mock(BillingResult.class);
            when(okResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
            listenerRef.get().onBillingSetupFinished(okResult);
        }
    }

    @Test
    public void queryProductDetails_returnsEarlyWhenClientNull() {
        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);
        repository.queryProductDetails(List.of("gold"), listener);
        verifyNoInteractions(listener);
    }

    @Test
    public void queryProductDetails_returnsEarlyWhenClientNotReady() throws ReflectiveOperationException {
        BillingClient billingClient = mock(BillingClient.class);
        when(billingClient.isReady()).thenReturn(false);
        setBillingClient(repository, billingClient);

        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);
        repository.queryProductDetails(List.of("gold"), listener);
        verify(billingClient, never()).queryProductDetailsAsync(any(), any());
        verifyNoInteractions(listener);
    }

    @Test
    public void queryProductDetails_populatesCacheAndNotifiesListener() throws ReflectiveOperationException {
        BillingClient billingClient = mock(BillingClient.class);
        when(billingClient.isReady()).thenReturn(true);
        setBillingClient(repository, billingClient);

        AtomicReference<ProductDetailsResponseListener> responseListenerRef = new AtomicReference<>();
        doAnswer(invocation -> {
            responseListenerRef.set(invocation.getArgument(1));
            return null;
        }).when(billingClient).queryProductDetailsAsync(any(), any());

        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);
        repository.queryProductDetails(List.of("gold"), listener);

        ProductDetailsResponseListener responseListener = responseListenerRef.get();
        BillingResult okResult = mock(BillingResult.class);
        when(okResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
        ProductDetails productDetails = mock(ProductDetails.class);
        when(productDetails.getProductId()).thenReturn("gold");
        QueryProductDetailsResult queryResult = mock(QueryProductDetailsResult.class);
        when(queryResult.getProductDetailsList()).thenReturn(List.of(productDetails));

        responseListener.onProductDetailsResponse(okResult, queryResult);
        verify(listener).onProductDetailsRetrieved(List.of(productDetails));

        SupportRepository.BillingFlowLauncher launcher = repository.initiatePurchase("gold");
        assertNotNull(launcher);
    }

    @Test
    public void queryProductDetails_doesNothingWhenResultEmpty() throws ReflectiveOperationException {
        BillingClient billingClient = mock(BillingClient.class);
        when(billingClient.isReady()).thenReturn(true);
        setBillingClient(repository, billingClient);

        AtomicReference<ProductDetailsResponseListener> responseListenerRef = new AtomicReference<>();
        doAnswer(invocation -> {
            responseListenerRef.set(invocation.getArgument(1));
            return null;
        }).when(billingClient).queryProductDetailsAsync(any(), any());

        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);
        repository.queryProductDetails(List.of("gold"), listener);

        ProductDetailsResponseListener responseListener = responseListenerRef.get();
        BillingResult okResult = mock(BillingResult.class);
        when(okResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
        QueryProductDetailsResult queryResult = mock(QueryProductDetailsResult.class);
        when(queryResult.getProductDetailsList()).thenReturn(Collections.emptyList());
        responseListener.onProductDetailsResponse(okResult, queryResult);

        verifyNoInteractions(listener);
        assertNull(repository.initiatePurchase("gold"));
    }

    @Test
    public void initiatePurchase_returnsNullWhenNoDetails() throws ReflectiveOperationException {
        assertNull(repository.initiatePurchase("gold"));

        BillingClient billingClient = mock(BillingClient.class);
        setBillingClient(repository, billingClient);
        assertNull(repository.initiatePurchase("gold"));
    }

    @Test
    public void initiatePurchase_launchesBillingFlowWithStoredDetails() throws ReflectiveOperationException {
        BillingClient billingClient = mock(BillingClient.class);
        when(billingClient.isReady()).thenReturn(true);
        setBillingClient(repository, billingClient);

        AtomicReference<ProductDetailsResponseListener> responseListenerRef = new AtomicReference<>();
        doAnswer(invocation -> {
            responseListenerRef.set(invocation.getArgument(1));
            return null;
        }).when(billingClient).queryProductDetailsAsync(any(), any());

        ProductDetails productDetails = mock(ProductDetails.class);
        when(productDetails.getProductId()).thenReturn("gold");
        ProductDetails.OneTimePurchaseOfferDetails offerDetails = mock(ProductDetails.OneTimePurchaseOfferDetails.class);
        when(offerDetails.getOfferToken()).thenReturn("offer");
        when(productDetails.getOneTimePurchaseOfferDetails()).thenReturn(offerDetails);

        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);
        repository.queryProductDetails(List.of("gold"), listener);

        ProductDetailsResponseListener responseListener = responseListenerRef.get();
        BillingResult okResult = mock(BillingResult.class);
        when(okResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
        QueryProductDetailsResult queryResult = mock(QueryProductDetailsResult.class);
        when(queryResult.getProductDetailsList()).thenReturn(List.of(productDetails));
        responseListener.onProductDetailsResponse(okResult, queryResult);

        SupportRepository.BillingFlowLauncher launcher = repository.initiatePurchase("gold");
        assertNotNull(launcher);

        when(billingClient.launchBillingFlow(any(Activity.class), any())).thenReturn(mock(BillingResult.class));
        Activity activity = mock(Activity.class);
        launcher.launch(activity);

        ArgumentCaptor<BillingFlowParams> paramsCaptor = ArgumentCaptor.forClass(BillingFlowParams.class);
        verify(billingClient).launchBillingFlow(eq(activity), paramsCaptor.capture());
        List<BillingFlowParams.ProductDetailsParams> paramsList = paramsCaptor.getValue().getProductDetailsParamsList();
        assertEquals(1, paramsList.size());
        assertSame(productDetails, paramsList.get(0).getProductDetails());
    }

    @Test
    public void initMobileAds_initializesSdkAndReturnsRequest() {
        try (MockedStatic<AdUtils> adUtils = mockStatic(AdUtils.class)) {
            AdRequest request = repository.initMobileAds();
            adUtils.verify(() -> AdUtils.initialize(context));
            assertNotNull(request);
        }
    }

    private static void setBillingClient(DefaultSupportRepository repository, BillingClient billingClient)
            throws ReflectiveOperationException {
        Field field = DefaultSupportRepository.class.getDeclaredField("billingClient");
        field.setAccessible(true);
        field.set(repository, billingClient);
    }
}
