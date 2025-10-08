package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.images;

import android.os.Bundle;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityImagesBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.images.tabs.ImagesTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.images.tabs.ImagesTabLayoutFragment;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import java.util.Arrays;

public class ImagesActivity extends SyntaxFabActivity {
    private ActivityImagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.image_view,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        ImagesTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        ImagesTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }
}
