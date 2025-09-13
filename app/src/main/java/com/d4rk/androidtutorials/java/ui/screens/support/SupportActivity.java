package com.d4rk.androidtutorials.java.ui.screens.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.d4rk.androidtutorials.java.ads.AdUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SupportActivity extends BaseActivity {

    private ActivitySupportBinding binding;
    private SupportViewModel supportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);

        AdUtils.initialize(this);
        binding.supportNativeAd.loadAd(new AdRequest.Builder().build());
        AdUtils.loadBanner(binding.bannerAdView);

        binding.buttonWebAd.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3p8bpjj"))));

        supportViewModel.initBillingClient(this::queryProductDetails);

        binding.buttonLowDonation.setOnClickListener(v -> initiatePurchase("low_donation"));
        binding.buttonNormalDonation.setOnClickListener(v -> initiatePurchase("normal_donation"));
        binding.buttonHighDonation.setOnClickListener(v -> initiatePurchase("high_donation"));
        binding.buttonExtremeDonation.setOnClickListener(v -> initiatePurchase("extreme_donation"));
    }

    private void queryProductDetails() {
        List<String> productIds = List.of("low_donation", "normal_donation", "high_donation", "extreme_donation");
        supportViewModel.queryProductDetails(productIds, productDetailsList -> {
            for (ProductDetails productDetails : productDetailsList) {
                String price = productDetails.getOneTimePurchaseOfferDetails() != null
                        ? productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice()
                        : "";
                switch (productDetails.getProductId()) {
                    case "low_donation" -> binding.buttonLowDonation.setText(price);
                    case "normal_donation" -> binding.buttonNormalDonation.setText(price);
                    case "high_donation" -> binding.buttonHighDonation.setText(price);
                    case "extreme_donation" -> binding.buttonExtremeDonation.setText(price);
                }
            }
        });
    }

    private void initiatePurchase(String productId) {
        SupportRepository.BillingFlowLauncher launcher = supportViewModel.initiatePurchase(productId);
        if (launcher != null) {
            launcher.launch(this);
        }
    }

    // Up navigation handled by BaseActivity
}