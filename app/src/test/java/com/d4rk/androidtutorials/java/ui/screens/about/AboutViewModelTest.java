package com.d4rk.androidtutorials.java.ui.screens.about;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.domain.about.GetCurrentYearUseCase;
import com.d4rk.androidtutorials.java.domain.about.GetVersionStringUseCase;

import org.junit.Test;

public class AboutViewModelTest {

    @Test
    public void getVersionStringDelegatesToUseCase() {
        GetVersionStringUseCase versionUseCase = mock(GetVersionStringUseCase.class);
        GetCurrentYearUseCase currentYearUseCase = mock(GetCurrentYearUseCase.class);
        when(versionUseCase.invoke()).thenReturn("v1.2.3 (45)");
        AboutViewModel viewModel = new AboutViewModel(versionUseCase, currentYearUseCase);

        String result = viewModel.getVersionString();

        assertEquals("v1.2.3 (45)", result);
        verify(versionUseCase).invoke();
    }

    @Test
    public void getCurrentYearDelegatesToUseCase() {
        GetVersionStringUseCase versionUseCase = mock(GetVersionStringUseCase.class);
        GetCurrentYearUseCase currentYearUseCase = mock(GetCurrentYearUseCase.class);
        when(currentYearUseCase.invoke()).thenReturn("2024");
        AboutViewModel viewModel = new AboutViewModel(versionUseCase, currentYearUseCase);

        String result = viewModel.getCurrentYear();

        assertEquals("2024", result);
        verify(currentYearUseCase).invoke();
    }

    @Test
    public void getVersionString_propagatesFailure() {
        GetVersionStringUseCase versionUseCase = mock(GetVersionStringUseCase.class);
        GetCurrentYearUseCase currentYearUseCase = mock(GetCurrentYearUseCase.class);
        when(versionUseCase.invoke()).thenThrow(new IllegalStateException("error"));
        AboutViewModel viewModel = new AboutViewModel(versionUseCase, currentYearUseCase);

        assertThrows(IllegalStateException.class, viewModel::getVersionString);
    }

    @Test
    public void getCurrentYear_propagatesFailure() {
        GetVersionStringUseCase versionUseCase = mock(GetVersionStringUseCase.class);
        GetCurrentYearUseCase currentYearUseCase = mock(GetCurrentYearUseCase.class);
        when(currentYearUseCase.invoke()).thenThrow(new IllegalArgumentException("error"));
        AboutViewModel viewModel = new AboutViewModel(versionUseCase, currentYearUseCase);

        assertThrows(IllegalArgumentException.class, viewModel::getCurrentYear);
    }
}
