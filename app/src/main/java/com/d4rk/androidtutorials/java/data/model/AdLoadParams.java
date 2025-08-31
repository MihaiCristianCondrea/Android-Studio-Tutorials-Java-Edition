package com.d4rk.androidtutorials.java.data.model;

import com.google.android.gms.ads.AdRequest;

/** Parameters used to load an ad without exposing view bindings. */
public class AdLoadParams {

    /** Callback to handle ad loading without exposing view bindings. */
    @FunctionalInterface
    public interface AdLoader {
        void load(AdRequest request);
    }

    private final AdLoader adLoader;

    public AdLoadParams(AdLoader adLoader) {
        this.adLoader = adLoader;
    }

    public AdLoader getAdLoader() {
        return adLoader;
    }
}

