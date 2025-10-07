package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.sdk;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.data.model.AndroidVersion;
import com.d4rk.androidtutorials.java.databinding.ActivityAndroidSdkBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import java.util.Arrays;
import java.util.List;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class AndroidSDK extends UpNavigationActivity {
    private final List<AndroidVersion> androidVersions = Arrays.asList(
            new AndroidVersion("1.0", "1", "BASE", "None", "2008"),
            new AndroidVersion("1.1", "2", "BASE_1_1", "Petit Four", "2009"),
            new AndroidVersion("1.5", "3", "CUPCAKE", "Cupcake", "2009"),
            new AndroidVersion("1.6", "4", "DONUT", "Donut", "2009"),
            new AndroidVersion("2.0", "5", "ECLAIR", "Eclair", "2009"),
            new AndroidVersion("2.0.1", "6", "ECLAIR_0_1", "Eclair", "2009"),
            new AndroidVersion("2.1", "7", "ECLAIR_MR1", "Eclair", "2010"),
            new AndroidVersion("2.2", "8", "FROYO", "Froyo", "2010"),
            new AndroidVersion("2.3.0/2.3.2", "9", "GINGERBREAD", "Gingerbread", "2010"),
            new AndroidVersion("2.3.3/2.3.7", "10", "GINGERBREAD_MR1", "Gingerbread", "2010"),
            new AndroidVersion("3.0", "11", "HONEYCOMB", "Honeycomb", "2011"),
            new AndroidVersion("3.1", "12", "HONEYCOMB_MR1", "Honeycomb", "2011"),
            new AndroidVersion("3.2", "13", "HONEYCOMB_MR2", "Honeycomb", "2011"),
            new AndroidVersion("4.0.1/4.0.2", "14", "ICE_CREAM_SANDWICH", "Ice Cream Sandwich", "2011"),
            new AndroidVersion("4.0.3/4.0.4", "15", "ICE_CREAM_SANDWICH_MR1", "Ice Cream Sandwich", "2011"),
            new AndroidVersion("4.1", "16", "JELLYBEAN", "Jelly Bean", "2012"),
            new AndroidVersion("4.2", "17", "JELLYBEAN_MR1", "Jelly Bean", "2012"),
            new AndroidVersion("4.3", "18", "JELLYBEAN_MR2", "Jelly Bean", "2013"),
            new AndroidVersion("4.4", "19", "JELLYBEAN_MR2", "KitKat", "2013"),
            new AndroidVersion("4.4W", "20", "JKITKAT_WATCH", "KitKat", "2013"),
            new AndroidVersion("5.0", "21", "LOLLIPOP, L", "Lollipop", "2014"),
            new AndroidVersion("5.1", "22", "LOLLIPOP_MR1", "Lollipop", "2015"),
            new AndroidVersion("6.0", "23", "M", "Marshmallow", "2015"),
            new AndroidVersion("7.0", "24", "N", "Nougat", "2016"),
            new AndroidVersion("7.1", "25", "N_MR1", "Nougat", "2016"),
            new AndroidVersion("8.0", "26", "O", "Oreo", "2017"),
            new AndroidVersion("8.1", "27", "O_MR1", "Oreo", "2017"),
            new AndroidVersion("9", "28", "P", "Pie", "2018"),
            new AndroidVersion("10", "29", "Q", "Quince Tart", "2019"),
            new AndroidVersion("11", "30", "R", "Red Velvet Cake", "2020"),
            new AndroidVersion("12", "31", "S", "Snow Cone", "2021"),
            new AndroidVersion("12L", "32", "S_V2", "Snow Cone", "2021"),
            new AndroidVersion("13", "33", "T", "Tiramisu", "2022"),
            new AndroidVersion("14", "34", "U", "Upside Down Cake", "2023"),
            new AndroidVersion("15", "35", "V", "Vanilla Ice Cake", "2024"),
            new AndroidVersion("16", "36", "Baklava", "Baklava", "2025")
    );
    private ActivityAndroidSdkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAndroidSdkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeDelegate.apply(this, binding.scrollView);

        AdUtils.loadBanner(binding.adViewBottom);
        AdUtils.loadBanner(binding.adView);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        createDynamicTable();
    }

    private void createDynamicTable() {
        TableLayout tableLayout = binding.tableLayout;
        for (AndroidVersion version : androidVersions) {
            TableRow row = new TableRow(this);

            row.addView(createCell(version.version()));
            row.addView(createCell(version.api()));
            row.addView(createCell(version.codeName()));
            row.addView(createCell(version.codenameLiteral()));
            row.addView(createCell(version.year()));

            tableLayout.addView(row);
        }
    }

    private TextView createCell(String text) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
        );
        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setText(text);
        textView.setTextSize(11f);
        textView.setPadding(4, 4, 4, 4);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }
}