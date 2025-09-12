package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.d4rk.androidtutorials.java.databinding.ActivityWebviewBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class WebViewActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityWebviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        new FastScrollerBuilder(binding.webView).useMd2Style().build();
        setupWebView();
        setupFloatingButton();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebView webView = binding.webView;
        webView.loadUrl("https://d4rk7355608.github.io/profile/#home");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private void setupFloatingButton() {
        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "WebView");
            startActivity(intent);
        });
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
