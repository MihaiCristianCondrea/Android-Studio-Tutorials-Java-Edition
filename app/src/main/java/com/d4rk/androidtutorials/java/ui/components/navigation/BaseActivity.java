package com.d4rk.androidtutorials.java.ui.components.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu instanceof MenuBuilder menuBuilder) {
            menuBuilder.setOptionalIconsVisible(true);
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
