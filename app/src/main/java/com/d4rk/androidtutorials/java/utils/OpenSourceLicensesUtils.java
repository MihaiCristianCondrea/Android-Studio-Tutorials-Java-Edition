package com.d4rk.androidtutorials.java.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.d4rk.androidtutorials.java.R;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenSourceLicensesUtils {
    private static final String TAG = "OpenSourceLicensesUtils";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void loadHtmlData(final Context context, final HtmlDataCallback callback) {
        executor.execute(() -> {
            String packageName = context.getPackageName();
            String currentVersion = getAppVersion(context);
            String changelogUrl = "https://raw.githubusercontent.com/MihaiCristianCondrea/" + packageName + "/refs/heads/main/CHANGELOG.md";
            String eulaUrl = "https://raw.githubusercontent.com/MihaiCristianCondrea/" + packageName + "/refs/heads/main/EULA.md";

            String changelogMarkdown = loadMarkdown(context, changelogUrl, R.string.error_loading_changelog);
            String extractedChangelog = extractLatestVersionChangelog(changelogMarkdown, currentVersion);
            String changelogHtml = markdownToHtml(extractedChangelog);

            String eulaMarkdown = loadMarkdown(context, eulaUrl, R.string.error_loading_eula);
            String eulaHtml = markdownToHtml(eulaMarkdown);

            mainHandler.post(() -> callback.onHtmlDataLoaded(changelogHtml, eulaHtml));
        });
    }

    private static String loadMarkdown(Context context, String urlString, int errorStringId) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString();
            } else {
                Log.e(TAG, "Failed to load URL: " + urlString + " with response code: " + responseCode);
                return context.getString(errorStringId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading markdown from URL: " + urlString, e);
            return context.getString(errorStringId);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String extractLatestVersionChangelog(String markdown, String currentVersion) {
        // Define the regex pattern to match the latest version section
        String regexPattern = "(?m)^#\\s+Version\\s+" + Pattern.quote(currentVersion) + ":\\s*(.*?)^(#\\s+Version\\s+|$)";
        Pattern pattern = Pattern.compile(regexPattern, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(markdown);

        if (matcher.find()) {
            // Group 1 contains the changelog for the current version
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
            return "1.0.0"; // Fallback version
        }
    }

    public interface HtmlDataCallback {
        void onHtmlDataLoaded(String changelogHtml, String eulaHtml);
    }
}