package com.d4rk.androidtutorials.java.ui.screens.android.lessons.networking.retrofit;

import android.os.Bundle;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRetrofitBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.networking.retrofit.tabs.RetrofitTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.networking.retrofit.tabs.RetrofitTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

/**
 * Demonstrates a simple HTTP request using Retrofit.
 */
public class RetrofitActivity extends SyntaxFabActivity {
    private ActivityRetrofitBinding binding;
    private JsonPlaceholderApi api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRetrofitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        LessonUiUtils.setupDescriptionSection(binding.descriptionSection, R.string.summary_retrofit, false);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
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

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.retrofit,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        RetrofitTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        RetrofitTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
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
