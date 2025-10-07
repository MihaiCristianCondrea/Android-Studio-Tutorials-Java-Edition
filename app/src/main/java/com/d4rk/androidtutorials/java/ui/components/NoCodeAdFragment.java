package com.d4rk.androidtutorials.java.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.d4rk.androidtutorials.java.ads.AdUtils;

/**
 * Generic fragment that inflates a provided ViewBinding and loads a banner ad.
 *
 * @param <T> The type of ViewBinding associated with this fragment.
 */
public abstract class NoCodeAdFragment<T extends ViewBinding> extends Fragment {
    protected T binding;

    /**
     * Inflate the ViewBinding for this fragment.
     *
     * @param inflater  LayoutInflater to use.
     * @param container Optional container.
     * @return The inflated binding.
     */
    @NonNull
    protected abstract T inflateBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * Called after the binding has been created and the banner ad loaded.
     * Subclasses can override to perform additional setup.
     *
     * @param binding            The binding instance.
     * @param savedInstanceState Saved instance state.
     */
    protected void onBindingCreated(@NonNull T binding, @Nullable Bundle savedInstanceState) {
        // default no-op
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = inflateBinding(inflater, container);
        AdUtils.loadBanner(getAdView(binding));
        onBindingCreated(binding, savedInstanceState);
        return binding.getRoot();
    }

    @NonNull
    protected abstract View getAdView(@NonNull T binding);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
