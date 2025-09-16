package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ui.screens.about.repository.AboutRepository;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AboutRepositoryTest {

    @Test
    public void getVersionString_formatsFromContextTemplate() {
        Context context = org.mockito.Mockito.mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getString(R.string.app_version)).thenReturn("v%s (%d)");

        AboutRepository repository = new AboutRepository(context);

        String expected = String.format("v%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        assertEquals(expected, repository.getVersionString());

        verify(context).getString(R.string.app_version);
    }

    @Test
    public void getCurrentYear_returnsFormattedYear() {
        Context context = org.mockito.Mockito.mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);

        AboutRepository repository = new AboutRepository(context);

        String expected = new SimpleDateFormat("yyyy", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        assertEquals(expected, repository.getCurrentYear());
    }
}
