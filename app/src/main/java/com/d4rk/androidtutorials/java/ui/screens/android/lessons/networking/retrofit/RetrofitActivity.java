package com.d4rk.androidtutorials.java.ui.screens.android.lessons.networking.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRetrofitBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.gson.annotations.SerializedName;

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
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityRetrofitBinding binding;
    private JsonPlaceholderApi api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(JsonPlaceholderApi.class);

        binding.buttonFetch.setOnClickListener(v -> {
            binding.buttonFetch.setEnabled(false);
            api.getTodo().enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                    if (response.isSuccessful()) {
                        displayTodoTitle(response);
                    } else {
                        showGeneralErrorMessage();
                    }
                    binding.buttonFetch.setEnabled(true);
                }

                @Override
                public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                    showGeneralErrorMessage();
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

    private void displayTodoTitle(@NonNull Response<Todo> response) {
        Todo todo = response.body();
        if (todo != null) {
            if (todo.title != null && !todo.title.isEmpty()) {
                binding.textViewResult.setText(todo.title);
                return;
            }
        }
        showGeneralErrorMessage();
    }

    private void showGeneralErrorMessage() {
        binding.textViewResult.setText(R.string.snack_general_error);
    }

    interface JsonPlaceholderApi {
        @GET("todos/1")
        Call<Todo> getTodo();
    }

    @Keep
    public static final class Todo {
        @SerializedName("title")
        @Nullable
        public String title;
    }
}
