package com.d4rk.androidtutorials.java.data.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AndroidVersionTest {

    @Test
    public void androidVersionRecordStoresProvidedValues() {
        AndroidVersion androidVersion = new AndroidVersion(
                "13",
                "API 33",
                "Tiramisu",
                "android.os.Build.VERSION_CODES.TIRAMISU",
                "2022"
        );

        assertEquals("13", androidVersion.version());
        assertEquals("API 33", androidVersion.api());
        assertEquals("Tiramisu", androidVersion.codeName());
        assertEquals("android.os.Build.VERSION_CODES.TIRAMISU", androidVersion.codenameLiteral());
        assertEquals("2022", androidVersion.year());
    }

    @Test
    public void androidVersionRecordAllowsNullValues() {
        AndroidVersion androidVersion = new AndroidVersion(null, null, null, null, null);

        assertNull(androidVersion.version());
        assertNull(androidVersion.api());
        assertNull(androidVersion.codeName());
        assertNull(androidVersion.codenameLiteral());
        assertNull(androidVersion.year());
    }
}
