package com.d4rk.androidtutorials.java.ui.screens.android.lessons.architecture.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {

    private final MutableLiveData<Integer> counter = new MutableLiveData<>(0);

    public LiveData<Integer> getCounter() {
        return counter;
    }

    public void incrementCounter() {
        Integer currentValue = counter.getValue();
        if (currentValue == null) {
            currentValue = 0;
        }
        counter.setValue(currentValue + 1);
    }
}
