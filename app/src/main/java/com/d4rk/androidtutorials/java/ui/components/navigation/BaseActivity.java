package com.d4rk.androidtutorials.java.ui.components.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        View container = findViewById(R.id.container);
        if (container != null) {
            EdgeToEdgeDelegate.apply(this, container);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) menu; // FIXME: Variable 'menuBuilder' can be replaced with pattern variable
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
