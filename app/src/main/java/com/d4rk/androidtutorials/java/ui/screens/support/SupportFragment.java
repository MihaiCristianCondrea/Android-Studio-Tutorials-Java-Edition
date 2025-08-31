package com.d4rk.androidtutorials.java.ui.screens.support;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.billingclient.api.ProductDetails;
import com.d4rk.androidtutorials.java.data.repository.SupportRepository;
import com.d4rk.androidtutorials.java.databinding.FragmentSupportBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SupportFragment extends Fragment {

    private FragmentSupportBinding binding;
    private SupportViewModel supportViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSupportBinding.inflate(inflater, container, false);

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(requireActivity());
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        supportViewModel = new ViewModelProvider(this).get(SupportViewModel.class);

        AdRequest adRequest = supportViewModel.initMobileAds();
        binding.largeBannerAd.loadAd(adRequest);

        binding.buttonWebAd.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://bit.ly/3p8bpjj"))));

        supportViewModel.initBillingClient(this::queryProductDetails);

        binding.buttonLowDonation.setOnClickListener(v -> initiatePurchase("low_donation"));
        binding.buttonNormalDonation.setOnClickListener(v -> initiatePurchase("normal_donation"));
        binding.buttonHighDonation.setOnClickListener(v -> initiatePurchase("high_donation"));
        binding.buttonExtremeDonation.setOnClickListener(v -> initiatePurchase("extreme_donation"));

        return binding.getRoot();
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
            launcher.launch(requireActivity());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
