package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.google.android.gms.ads.AdRequest;
import com.d4rk.androidtutorials.java.ads.AdUtils;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultSupportRepositoryTest {

    @Test
    public void initBillingClient_connectsAndTriggersOnConnected() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        DefaultSupportRepository repository = new DefaultSupportRepository(context);

        Runnable onConnected = mock(Runnable.class);

        BillingClient billingClient = mock(BillingClient.class);
        BillingClient.Builder builder = mock(BillingClient.Builder.class);
        ArgumentCaptor<BillingClientStateListener> listenerCaptor = ArgumentCaptor.forClass(BillingClientStateListener.class);

        try (MockedStatic<BillingClient> billingStatic = org.mockito.Mockito.mockStatic(BillingClient.class)) {
            billingStatic.when(() -> BillingClient.newBuilder(context)).thenReturn(builder);
            when(builder.setListener(any())).thenReturn(builder);
            when(builder.enablePendingPurchases(any())).thenReturn(builder);
            when(builder.enableAutoServiceReconnection()).thenReturn(builder);
            when(builder.build()).thenReturn(billingClient);

            repository.initBillingClient(onConnected);

            verify(billingClient).startConnection(listenerCaptor.capture());

            BillingClientStateListener stateListener = listenerCaptor.getValue();
            BillingResult result = BillingResult.newBuilder()
                    .setResponseCode(BillingClient.BillingResponseCode.OK)
                    .build();
            stateListener.onBillingSetupFinished(result);

            verify(onConnected).run();
        }
    }

    @Test
    public void queryProductDetails_populatesMapAndInvokesListener() throws Exception {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        DefaultSupportRepository repository = new DefaultSupportRepository(context);

        BillingClient billingClient = mock(BillingClient.class);
        when(billingClient.isReady()).thenReturn(true);

        Field billingField = DefaultSupportRepository.class.getDeclaredField("billingClient");
        billingField.setAccessible(true);
        billingField.set(repository, billingClient);

        SupportRepository.OnProductDetailsListener listener = mock(SupportRepository.OnProductDetailsListener.class);
        ProductDetails details = mock(ProductDetails.class);
        when(details.getProductId()).thenReturn("id1");

        QueryProductDetailsResult queryResult = mock(QueryProductDetailsResult.class);
        when(queryResult.getProductDetailsList()).thenReturn(Collections.singletonList(details));

        org.mockito.Mockito.doAnswer(invocation -> {
            ProductDetailsResponseListener responseListener = invocation.getArgument(1);
            BillingResult billingResult = BillingResult.newBuilder()
                    .setResponseCode(BillingClient.BillingResponseCode.OK)
                    .build();
            responseListener.onProductDetailsResponse(billingResult, queryResult);
            return null;
        }).when(billingClient).queryProductDetailsAsync(any(QueryProductDetailsParams.class), any(ProductDetailsResponseListener.class));

        repository.queryProductDetails(Collections.singletonList("id1"), listener);

        ArgumentCaptor<List<ProductDetails>> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(listener).onProductDetailsRetrieved(listCaptor.capture());
        assertEquals(1, listCaptor.getValue().size());
        assertSame(details, listCaptor.getValue().get(0));

        Field mapField = DefaultSupportRepository.class.getDeclaredField("productDetailsMap");
        mapField.setAccessible(true);
        Map<String, ProductDetails> map = (Map<String, ProductDetails>) mapField.get(repository);
        assertSame(details, map.get("id1"));
    }

    @Test
    public void initiatePurchase_buildsFlowParamsAndCallsLaunchBillingFlow() throws Exception {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        DefaultSupportRepository repository = new DefaultSupportRepository(context);

        BillingClient billingClient = mock(BillingClient.class);
        Field billingField = DefaultSupportRepository.class.getDeclaredField("billingClient");
        billingField.setAccessible(true);
        billingField.set(repository, billingClient);

        ProductDetails details = mock(ProductDetails.class);
        when(details.getProductId()).thenReturn("id1");
        ProductDetails.OneTimePurchaseOfferDetails offerDetails = mock(ProductDetails.OneTimePurchaseOfferDetails.class);
        when(offerDetails.getOfferToken()).thenReturn("token");
        when(details.getOneTimePurchaseOfferDetails()).thenReturn(offerDetails);

        Field mapField = DefaultSupportRepository.class.getDeclaredField("productDetailsMap");
        mapField.setAccessible(true);
        Map<String, ProductDetails> map = (Map<String, ProductDetails>) mapField.get(repository);
        map.put("id1", details);

        SupportRepository.BillingFlowLauncher launcher = repository.initiatePurchase("id1");
        Activity activity = mock(Activity.class);
        assertNotNull(launcher);
        launcher.launch(activity);

        ArgumentCaptor<BillingFlowParams> paramsCaptor = ArgumentCaptor.forClass(BillingFlowParams.class);
        verify(billingClient).launchBillingFlow(eq(activity), paramsCaptor.capture());
        BillingFlowParams params = paramsCaptor.getValue();
        List<BillingFlowParams.ProductDetailsParams> list = params.getProductDetailsParamsList();
        assertEquals(1, list.size());
        BillingFlowParams.ProductDetailsParams pdp = list.get(0);
        assertSame(details, pdp.getProductDetails());
        assertEquals("token", pdp.getOfferToken());
    }

    @Test
    public void initMobileAds_returnsAdRequestAndCallsInitialize() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        DefaultSupportRepository repository = new DefaultSupportRepository(context);

        try (MockedStatic<AdUtils> adUtils = org.mockito.Mockito.mockStatic(AdUtils.class)) {
            AdRequest request = repository.initMobileAds();
            assertNotNull(request);
            adUtils.verify(() -> AdUtils.initialize(context), times(1));
        }
    }
}
