package com.d4rk.androidtutorials.java.ui.screens.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.domain.support.InitBillingClientUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitMobileAdsUseCase;
import com.d4rk.androidtutorials.java.domain.support.InitiatePurchaseUseCase;
import com.d4rk.androidtutorials.java.domain.support.QueryProductDetailsUseCase;
import com.google.android.gms.ads.AdRequest;

import org.junit.Test;

import java.util.List;

public class SupportViewModelTest {

    @Test
    public void initBillingClientDelegatesToRepository() {
        FakeSupportRepository repository = new FakeSupportRepository();
        SupportViewModel viewModel = createViewModel(repository);
        Runnable onConnected = () -> { };

        viewModel.initBillingClient(onConnected);

        assertSame(onConnected, repository.lastOnConnected);
    }

    @Test
    public void queryProductDetailsDelegatesToRepository() {
        FakeSupportRepository repository = new FakeSupportRepository();
        SupportViewModel viewModel = createViewModel(repository);
        List<String> productIds = List.of("id1", "id2");
        SupportRepository.OnProductDetailsListener listener = productDetails -> { };

        viewModel.queryProductDetails(productIds, listener);

        assertEquals(productIds, repository.lastProductIds);
        assertSame(listener, repository.lastProductDetailsListener);
    }

    @Test
    public void initiatePurchaseReturnsLauncherFromRepository() {
        FakeSupportRepository repository = new FakeSupportRepository();
        SupportViewModel viewModel = createViewModel(repository);
        repository.setBillingFlowLauncher(activity -> { });

        SupportRepository.BillingFlowLauncher launcher = viewModel.initiatePurchase("donation");

        assertEquals("donation", repository.lastProductId);
        assertSame(repository.billingFlowLauncher, launcher);
    }

    @Test
    public void initMobileAdsReturnsRepositoryRequest() {
        FakeSupportRepository repository = new FakeSupportRepository();
        SupportViewModel viewModel = createViewModel(repository);
        AdRequest adRequest = new AdRequest.Builder().build();
        repository.setAdRequest(adRequest);

        AdRequest result = viewModel.initMobileAds();

        assertTrue(repository.initMobileAdsCalled);
        assertSame(adRequest, result);
    }

    private SupportViewModel createViewModel(FakeSupportRepository repository) {
        return new SupportViewModel(
                new InitBillingClientUseCase(repository),
                new QueryProductDetailsUseCase(repository),
                new InitiatePurchaseUseCase(repository),
                new InitMobileAdsUseCase(repository)
        );
    }

    private static final class FakeSupportRepository implements SupportRepository {
        private Runnable lastOnConnected;
        private List<String> lastProductIds;
        private OnProductDetailsListener lastProductDetailsListener;
        private String lastProductId;
        private BillingFlowLauncher billingFlowLauncher = activity -> { };
        private AdRequest adRequest = new AdRequest.Builder().build();
        private boolean initMobileAdsCalled;

        void setBillingFlowLauncher(BillingFlowLauncher billingFlowLauncher) {
            this.billingFlowLauncher = billingFlowLauncher;
        }

        void setAdRequest(AdRequest adRequest) {
            this.adRequest = adRequest;
        }

        @Override
        public void initBillingClient(Runnable onConnected) {
            lastOnConnected = onConnected;
        }

        @Override
        public void queryProductDetails(List<String> productIds, OnProductDetailsListener listener) {
            lastProductIds = productIds;
            lastProductDetailsListener = listener;
        }

        @Override
        public BillingFlowLauncher initiatePurchase(String productId) {
            lastProductId = productId;
            return billingFlowLauncher;
        }

        @Override
        public AdRequest initMobileAds() {
            initMobileAdsCalled = true;
            return adRequest;
        }
    }
}
