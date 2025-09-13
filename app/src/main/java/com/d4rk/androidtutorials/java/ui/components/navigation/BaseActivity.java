package com.d4rk.androidtutorials.java.ui.components.navigation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
