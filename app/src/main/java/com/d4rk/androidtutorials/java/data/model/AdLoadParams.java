package com.d4rk.androidtutorials.java.data.model;

import com.google.android.gms.ads.AdRequest;
import java.util.function.Consumer;

/** Parameters used to load an ad without exposing view bindings. */
public class AdLoadParams {
    private final Consumer<AdRequest> adLoader;

    public AdLoadParams(Consumer<AdRequest> adLoader) {
        this.adLoader = adLoader;
    }

    public Consumer<AdRequest> getAdLoader() {
        return adLoader;
    }
}

