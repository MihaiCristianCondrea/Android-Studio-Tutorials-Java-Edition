package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository.BillingFlowLauncher;
import com.google.android.gms.ads.AdRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultSupportRepositoryTest {

    private Context context;
    private BillingClient billingClient;
    private DefaultSupportRepository repository;

    @Before
    public void setUp() throws Exception {
        context = Mockito.mock(Context.class);
        Mockito.when(context.getApplicationContext()).thenReturn(context);

        repository = new DefaultSupportRepository(context);

        billingClient = Mockito.mock(BillingClient.class);
        setBillingClient(repository, billingClient);
    }

    @Test
    public void queryProductDetailsPopulatesCacheAndNotifiesListener() throws Exception {
        Mockito.when(billingClient.isReady()).thenReturn(true);

        ProductDetails productDetails = Mockito.mock(ProductDetails.class);
        Mockito.when(productDetails.getProductId()).thenReturn("donation");
        List<ProductDetails> detailsList = List.of(productDetails);

        Mockito.doAnswer(invocation -> {
            Object listener = invocation.getArgument(1);
            try {
                invokeProductDetailsResponse(listener, BillingClient.BillingResponseCode.OK, detailsList);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return null;
        }).when(billingClient).queryProductDetailsAsync(Mockito.any(), Mockito.any());

        AtomicReference<List<ProductDetails>> captured = new AtomicReference<>();
        repository.queryProductDetails(List.of("donation"), captured::set);

        assertEquals(detailsList, captured.get());

        Map<String, ProductDetails> productDetailsMap = getProductDetailsMap(repository);
        assertSame(productDetails, productDetailsMap.get("donation"));
    }

    @Test
    public void initiatePurchaseLaunchesFlowWithOfferToken() throws Exception {
        Map<String, ProductDetails> productDetailsMap = getProductDetailsMap(repository);

        ProductDetails productDetails = Mockito.mock(ProductDetails.class);
        ProductDetails.OneTimePurchaseOfferDetails offerDetails = Mockito.mock(ProductDetails.OneTimePurchaseOfferDetails.class);
        Mockito.when(offerDetails.getOfferToken()).thenReturn("token-123");
        Mockito.when(productDetails.getOneTimePurchaseOfferDetails()).thenReturn(offerDetails);
        Mockito.when(productDetails.getProductId()).thenReturn("donation");
        productDetailsMap.put("donation", productDetails);

        BillingResult launchResult = Mockito.mock(BillingResult.class);
        Mockito.when(billingClient.launchBillingFlow(Mockito.any(Activity.class), Mockito.any(BillingFlowParams.class)))
                .thenReturn(launchResult);

        BillingFlowLauncher launcher = repository.initiatePurchase("donation");
        assertNotNull(launcher);

        Activity activity = Mockito.mock(Activity.class);
        launcher.launch(activity);

        ArgumentCaptor<BillingFlowParams> paramsCaptor = ArgumentCaptor.forClass(BillingFlowParams.class);
        Mockito.verify(billingClient).launchBillingFlow(Mockito.eq(activity), paramsCaptor.capture());

        BillingFlowParams flowParams = paramsCaptor.getValue();
        List<?> paramsList = getProductDetailsParamsList(flowParams);
        assertEquals(1, paramsList.size());
        Object params = paramsList.get(0);
        assertSame(productDetails, getProductDetailsFromParams(params));
        assertEquals("token-123", getOfferTokenFromParams(params));
    }

    @Test
    public void initiatePurchaseReturnsNullWhenDetailsMissing() {
        BillingFlowLauncher launcher = repository.initiatePurchase("missing");
        assertNull(launcher);
        Mockito.verifyNoInteractions(billingClient);
    }

    @Test
    public void initMobileAdsDelegatesToAdUtils() {
        try (MockedStatic<AdUtils> mockedAdUtils = Mockito.mockStatic(AdUtils.class)) {
            AdRequest request = repository.initMobileAds();

            mockedAdUtils.verify(() -> AdUtils.initialize(context));
            assertNotNull(request);
        }
    }

    private void invokeProductDetailsResponse(Object listener, int responseCode, List<ProductDetails> details)
            throws Exception {
        BillingResult billingResult = Mockito.mock(BillingResult.class);
        Mockito.when(billingResult.getResponseCode()).thenReturn(responseCode);

        Method callbackMethod = findListenerMethod(listener);
        Class<?> resultType = callbackMethod.getParameterTypes()[1];
        Object resultArgument;
        if (List.class.isAssignableFrom(resultType)) {
            resultArgument = details;
        } else {
            resultArgument = Mockito.mock(resultType, invocation -> {
                if ("getProductDetailsList".equals(invocation.getMethod().getName())) {
                    return details;
                }
                return null;
            });
        }
        callbackMethod.invoke(listener, billingResult, resultArgument);
    }

    private Method findListenerMethod(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isSynthetic()) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new IllegalStateException("Unable to locate listener method");
    }

    private Map<String, ProductDetails> getProductDetailsMap(DefaultSupportRepository target) throws Exception {
        Field field = DefaultSupportRepository.class.getDeclaredField("productDetailsMap");
        field.setAccessible(true);
        //noinspection unchecked
        return (Map<String, ProductDetails>) field.get(target);
    }

    private void setBillingClient(DefaultSupportRepository target, BillingClient client) throws Exception {
        Field field = DefaultSupportRepository.class.getDeclaredField("billingClient");
        field.setAccessible(true);
        field.set(target, client);
    }

    private List<?> getProductDetailsParamsList(BillingFlowParams params) throws Exception {
        Method method = BillingFlowParams.class.getDeclaredMethod("getProductDetailsParamsList");
        method.setAccessible(true);
        return (List<?>) method.invoke(params);
    }

    private Object getProductDetailsFromParams(Object params) throws Exception {
        Method method = params.getClass().getDeclaredMethod("getProductDetails");
        method.setAccessible(true);
        return method.invoke(params);
    }

    private String getOfferTokenFromParams(Object params) throws Exception {
        Method method = params.getClass().getDeclaredMethod("getOfferToken");
        method.setAccessible(true);
        Object token = method.invoke(params);
        return token != null ? token.toString() : null;
    }
}
