package com.d4rk.androidtutorials.java.ui.screens.android.lessons.networking.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.databinding.ActivityRetrofitBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import com.d4rk.androidtutorials.java.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Demonstrates a simple HTTP request using Retrofit.
 */
public class RetrofitActivity extends UpNavigationActivity {
    private ActivityRetrofitBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private JsonPlaceholderApi api;

    interface JsonPlaceholderApi {
        @GET("todos/1")
        Call<Todo> getTodo();
    }

    static class Todo {
        public String title;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.constraintLayout);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceholderApi.class);

        binding.buttonFetch.setOnClickListener(v -> {
            binding.buttonFetch.setEnabled(false);
            api.getTodo().enqueue(new Callback<Todo>() {
                @Override
                public void onResponse(Call<Todo> call, Response<Todo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.textViewResult.setText(response.body().title);
                    } else {
                        binding.textViewResult.setText(R.string.snack_general_error);
                    }
                    binding.buttonFetch.setEnabled(true);
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    binding.textViewResult.setText(R.string.snack_general_error);
                    binding.buttonFetch.setEnabled(true);
                }
            });
        });

        binding.floatingButtonShowSyntax.setOnClickListener(v ->
                startActivity(new Intent(RetrofitActivity.this, RetrofitCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
