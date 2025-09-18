package com.d4rk.androidtutorials.java.ui.screens.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.databinding.ActivitySupportBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.BaseActivity;
import com.google.android.gms.ads.AdRequest;

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

        AdRequest adRequest = supportViewModel.initMobileAds();
        binding.supportNativeAd.loadAd(adRequest);
        binding.bannerAdView.loadAd(adRequest);

        binding.buttonWebAd.setOnClickListener(v -> openSupportLink());

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

    private void openSupportLink() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3p8bpjj"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.support_link_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    // Up navigation handled by BaseActivity
}