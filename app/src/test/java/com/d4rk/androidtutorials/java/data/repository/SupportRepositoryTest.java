package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.google.android.gms.ads.AdRequest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.List;

public class SupportRepositoryTest {

    private DefaultSupportRepository createRepository(Context context,
                                                      BillingClient billingClient,
                                                      BillingClient.Builder builder,
                                                      PendingPurchasesParams.Builder pendingBuilder,
                                                      PendingPurchasesParams pendingParams,
                                                      MockedStatic<BillingClient> billingClientMockedStatic,
                                                      MockedStatic<PendingPurchasesParams> pendingPurchasesMockedStatic) {
        when(context.getApplicationContext()).thenReturn(context);
        billingClientMockedStatic.when(() -> BillingClient.newBuilder(context)).thenReturn(builder);
        when(builder.setListener(any())).thenReturn(builder);
        when(builder.enablePendingPurchases(any(PendingPurchasesParams.class))).thenReturn(builder);
        when(builder.enableAutoServiceReconnection()).thenReturn(builder);
        when(builder.build()).thenReturn(billingClient);
        pendingPurchasesMockedStatic.when(PendingPurchasesParams::newBuilder).thenReturn(pendingBuilder);
        when(pendingBuilder.enableOneTimeProducts()).thenReturn(pendingBuilder);
        when(pendingBuilder.build()).thenReturn(pendingParams);
        doNothing().when(billingClient).startConnection(any());
        return new DefaultSupportRepository(context);
    }

    @Test
    public void initBillingClient_invokesCallbackWhenConnected() {
        Context context = mock(Context.class);
        BillingClient billingClient = mock(BillingClient.class);
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);
        Runnable onConnected = mock(Runnable.class);
        BillingResult billingResult = mock(BillingResult.class);
        when(billingResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);

        try (MockedStatic<BillingClient> billingClientMockedStatic = Mockito.mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingPurchasesMockedStatic = Mockito.mockStatic(PendingPurchasesParams.class)) {
            DefaultSupportRepository repository = createRepository(context, billingClient, builder, pendingBuilder, pendingParams,
                    billingClientMockedStatic, pendingPurchasesMockedStatic);

            doAnswer(invocation -> {
                BillingClientStateListener listener = invocation.getArgument(0);
                listener.onBillingSetupFinished(billingResult);
                return null;
            }).when(billingClient).startConnection(any());

            repository.initBillingClient(onConnected);

            verify(billingClient).startConnection(any());
            verify(onConnected).run();
        }
    }

    @Test
    public void queryProductDetails_notifiesListenerAndCachesProducts() throws Exception {
        Context context = mock(Context.class);
        BillingClient billingClient = mock(BillingClient.class);
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);
        BillingResult billingResult = mock(BillingResult.class);
        when(billingResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
        ProductDetails productDetails = mock(ProductDetails.class);
        when(productDetails.getProductId()).thenReturn("supporter");
        ProductDetails.OneTimePurchaseOfferDetails offerDetails = mock(ProductDetails.OneTimePurchaseOfferDetails.class);
        when(offerDetails.getOfferToken()).thenReturn("token");
        when(productDetails.getOneTimePurchaseOfferDetails()).thenReturn(offerDetails);
        List<ProductDetails> productDetailsList = List.of(productDetails);
        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);

        try (MockedStatic<BillingClient> billingClientMockedStatic = Mockito.mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingPurchasesMockedStatic = Mockito.mockStatic(PendingPurchasesParams.class)) {
            DefaultSupportRepository repository = createRepository(context, billingClient, builder, pendingBuilder, pendingParams,
                    billingClientMockedStatic, pendingPurchasesMockedStatic);
            repository.initBillingClient(null);
            when(billingClient.isReady()).thenReturn(true);

            doAnswer(invocation -> {
                Object callback = invocation.getArgument(1);
                Class<?> listenerType = callback.getClass().getInterfaces()[0];
                Method method = listenerType.getDeclaredMethods()[0];
                method.setAccessible(true);
                Class<?> resultType = method.getParameterTypes()[1];
                Object secondParameter;
                if (List.class.isAssignableFrom(resultType)) {
                    secondParameter = productDetailsList;
                } else {
                    secondParameter = Mockito.mock(resultType, invocationOnMock -> {
                        if ("getProductDetailsList".equals(invocationOnMock.getMethod().getName())) {
                            return productDetailsList;
                        }
                        return Mockito.RETURNS_DEFAULTS.answer(invocationOnMock);
                    });
                }
                method.invoke(callback, billingResult, secondParameter);
                return null;
            }).when(billingClient).queryProductDetailsAsync(any(QueryProductDetailsParams.class), any());

            repository.queryProductDetails(List.of("supporter"), listener);

            verify(listener).onProductDetailsRetrieved(productDetailsList);
            SupportRepository.BillingFlowLauncher launcher = repository.initiatePurchase("supporter");
            assertNotNull(launcher);
        }
    }

    @Test
    public void queryProductDetails_whenClientNotReadyDoesNothing() {
        Context context = mock(Context.class);
        BillingClient billingClient = mock(BillingClient.class);
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);
        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);

        try (MockedStatic<BillingClient> billingClientMockedStatic = Mockito.mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingPurchasesMockedStatic = Mockito.mockStatic(PendingPurchasesParams.class)) {
            DefaultSupportRepository repository = createRepository(context, billingClient, builder, pendingBuilder, pendingParams,
                    billingClientMockedStatic, pendingPurchasesMockedStatic);
            repository.initBillingClient(null);
            when(billingClient.isReady()).thenReturn(false);

            repository.queryProductDetails(List.of("supporter"), listener);

            verify(billingClient, never()).queryProductDetailsAsync(any(QueryProductDetailsParams.class), any());
            verify(listener, never()).onProductDetailsRetrieved(anyList());
        }
    }

    @Test
    public void initiatePurchase_launchesBillingFlowWithCachedProduct() {
        Context context = mock(Context.class);
        BillingClient billingClient = mock(BillingClient.class);
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);
        BillingResult billingResult = mock(BillingResult.class);
        when(billingResult.getResponseCode()).thenReturn(BillingClient.BillingResponseCode.OK);
        ProductDetails productDetails = mock(ProductDetails.class);
        when(productDetails.getProductId()).thenReturn("supporter");
        ProductDetails.OneTimePurchaseOfferDetails offerDetails = mock(ProductDetails.OneTimePurchaseOfferDetails.class);
        when(offerDetails.getOfferToken()).thenReturn("token");
        when(productDetails.getOneTimePurchaseOfferDetails()).thenReturn(offerDetails);
        List<ProductDetails> productDetailsList = List.of(productDetails);

        try (MockedStatic<BillingClient> billingClientMockedStatic = Mockito.mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingPurchasesMockedStatic = Mockito.mockStatic(PendingPurchasesParams.class)) {
            DefaultSupportRepository repository = createRepository(context, billingClient, builder, pendingBuilder, pendingParams,
                    billingClientMockedStatic, pendingPurchasesMockedStatic);
            repository.initBillingClient(null);
            when(billingClient.isReady()).thenReturn(true);

            doAnswer(invocation -> {
                Object callback = invocation.getArgument(1);
                Class<?> listenerType = callback.getClass().getInterfaces()[0];
                Method method = listenerType.getDeclaredMethods()[0];
                method.setAccessible(true);
                Class<?> resultType = method.getParameterTypes()[1];
                Object secondParameter;
                if (List.class.isAssignableFrom(resultType)) {
                    secondParameter = productDetailsList;
                } else {
                    secondParameter = Mockito.mock(resultType, invocationOnMock -> {
                        if ("getProductDetailsList".equals(invocationOnMock.getMethod().getName())) {
                            return productDetailsList;
                        }
                        return Mockito.RETURNS_DEFAULTS.answer(invocationOnMock);
                    });
                }
                method.invoke(callback, billingResult, secondParameter);
                return null;
            }).when(billingClient).queryProductDetailsAsync(any(QueryProductDetailsParams.class), any());

            repository.queryProductDetails(List.of("supporter"), null);

            SupportRepository.BillingFlowLauncher launcher = repository.initiatePurchase("supporter");
            assertNotNull(launcher);

            Activity activity = mock(Activity.class);
            BillingResult launchResult = mock(BillingResult.class);
            when(billingClient.launchBillingFlow(eq(activity), any(BillingFlowParams.class))).thenReturn(launchResult);

            launcher.launch(activity);

            ArgumentCaptor<BillingFlowParams> paramsCaptor = ArgumentCaptor.forClass(BillingFlowParams.class);
            verify(billingClient).launchBillingFlow(eq(activity), paramsCaptor.capture());
            assertNotNull(paramsCaptor.getValue());
        }
    }

    @Test
    public void initiatePurchase_returnsNullWhenProductMissing() {
        Context context = mock(Context.class);
        BillingClient billingClient = mock(BillingClient.class);
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        PendingPurchasesParams.Builder pendingBuilder = mock(PendingPurchasesParams.Builder.class);
        PendingPurchasesParams pendingParams = mock(PendingPurchasesParams.class);

        try (MockedStatic<BillingClient> billingClientMockedStatic = Mockito.mockStatic(BillingClient.class);
             MockedStatic<PendingPurchasesParams> pendingPurchasesMockedStatic = Mockito.mockStatic(PendingPurchasesParams.class)) {
            DefaultSupportRepository repository = createRepository(context, billingClient, builder, pendingBuilder, pendingParams,
                    billingClientMockedStatic, pendingPurchasesMockedStatic);

            assertNull(repository.initiatePurchase("unknown"));
        }
    }

    @Test
    public void initMobileAds_initializesAndReturnsRequest() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);

        try (MockedStatic<AdUtils> adUtilsMockedStatic = Mockito.mockStatic(AdUtils.class)) {
            DefaultSupportRepository repository = new DefaultSupportRepository(context);

            AdRequest adRequest = repository.initMobileAds();

            adUtilsMockedStatic.verify(() -> AdUtils.initialize(context));
            assertNotNull(adRequest);
        }
    }
}
