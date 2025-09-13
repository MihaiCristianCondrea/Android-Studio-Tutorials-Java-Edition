package com.d4rk.androidtutorials.java.ui.components.navigation;

public abstract class UpNavigationActivity extends BaseActivity {
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
