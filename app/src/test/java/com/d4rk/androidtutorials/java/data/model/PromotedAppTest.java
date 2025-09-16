package com.d4rk.androidtutorials.java.data.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PromotedAppTest {

    @Test
    public void promotedAppRecordStoresProvidedValues() {
        PromotedApp promotedApp = new PromotedApp(
                "Android Tutorials",
                "com.example.tutorials",
                "https://example.com/icon.png"
        );

        assertEquals("Android Tutorials", promotedApp.name());
        assertEquals("com.example.tutorials", promotedApp.packageName());
        assertEquals("https://example.com/icon.png", promotedApp.iconUrl());
    }

    @Test
    public void promotedAppRecordAllowsNullValues() {
        PromotedApp promotedApp = new PromotedApp(null, null, null);

        assertNull(promotedApp.name());
        assertNull(promotedApp.packageName());
        assertNull(promotedApp.iconUrl());
    }
}
