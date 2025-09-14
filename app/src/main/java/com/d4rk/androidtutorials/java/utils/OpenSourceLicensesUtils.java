package com.d4rk.androidtutorials.java.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.d4rk.androidtutorials.java.R;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Utility class for loading and parsing open source license data.
 */
public class OpenSourceLicensesUtils {
    private static final String TAG = "OpenSourceLicensesUtils";
    private static final String WORK_NAME = "license_loader";
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public static void loadHtmlData(Context context, HtmlDataCallback callback) {
        Context appContext = context.getApplicationContext();
        WorkManager workManager = WorkManager.getInstance(appContext);
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LoadHtmlWorker.class)
                .addTag(WORK_NAME)
                .build();
        workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request);
        LiveData<WorkInfo> liveData = workManager.getWorkInfoByIdLiveData(request.getId());
        Observer<WorkInfo> observer = new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo info) {
                if (info != null && info.getState().isFinished()) {
                    Data output = info.getOutputData();
                    final String changelogHtml = output.getString("changelogHtml");
                    final String eulaHtml = output.getString("eulaHtml");
                    mainHandler.post(() -> callback.onHtmlDataLoaded(changelogHtml, eulaHtml));
                    liveData.removeObserver(this);
                }
            }
        };
        liveData.observeForever(observer);
    }

    static class LoadHtmlWorker extends Worker {
        LoadHtmlWorker(@NonNull Context context, @NonNull WorkerParameters params) {
            super(context, params);
        }

        @NonNull
        @Override
        public Result doWork() {
            Context context = getApplicationContext();
            String packageName = context.getPackageName();
            String currentVersion = getAppVersion(context);
            String changelogUrl = "https://raw.githubusercontent.com/MihaiCristianCondrea/" + packageName + "/refs/heads/main/CHANGELOG.md";
            String eulaUrl = "https://raw.githubusercontent.com/MihaiCristianCondrea/" + packageName + "/refs/heads/main/EULA.md";

            String changelogMarkdown = loadMarkdown(context, changelogUrl, R.string.error_loading_changelog);
            String extractedChangelog = extractLatestVersionChangelog(changelogMarkdown, currentVersion);
            String changelogHtml = markdownToHtml(extractedChangelog);

            String eulaMarkdown = loadMarkdown(context, eulaUrl, R.string.error_loading_eula);
            String eulaHtml = markdownToHtml(eulaMarkdown);

            Data output = new Data.Builder()
                    .putString("changelogHtml", changelogHtml)
                    .putString("eulaHtml", eulaHtml)
                    .build();
            return Result.success(output);
        }
    }

    private static String loadMarkdown(Context context, String urlString, int errorStringId) {
        Request request = new Request.Builder().url(urlString).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                Log.e(TAG, "Failed to load URL: " + urlString + " with response code: " + (response != null ? response.code() : -1));
                return context.getString(errorStringId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading markdown from URL: " + urlString, e);
            return context.getString(errorStringId);
        }
    }

    private static String extractLatestVersionChangelog(String markdown, String currentVersion) {
        String regexPattern = "(?m)^#\\s+Version\\s+" + Pattern.quote(currentVersion) + ":\\s*(.*?)^(#\\s+Version\\s+|$)";
        Pattern pattern = Pattern.compile(regexPattern, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdown);

        if (matcher.find()) {
            return Objects.requireNonNull(matcher.group(1)).trim();
        } else {
            Log.e(TAG, "No changelog available for version " + currentVersion);
            return "No changelog available for version " + currentVersion;
        }
    }

    private static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            Log.e(TAG, "Error getting app version", e);
            return "1.0.0";
        }
    }

    public interface HtmlDataCallback {
        void onHtmlDataLoaded(String changelogHtml, String eulaHtml);
    }

    public static void shutdown(Context context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME);
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}

