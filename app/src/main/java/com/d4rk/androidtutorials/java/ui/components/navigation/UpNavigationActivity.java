package com.d4rk.androidtutorials.java.ui.components.navigation;

import androidx.core.app.NavUtils;

public abstract class UpNavigationActivity extends BaseActivity {
    @Override
    public boolean onSupportNavigateUp() {
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
}
