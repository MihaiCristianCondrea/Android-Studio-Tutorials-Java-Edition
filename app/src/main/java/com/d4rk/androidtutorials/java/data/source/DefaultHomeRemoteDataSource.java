package com.d4rk.androidtutorials.java.data.source;

import android.os.Handler;
import android.os.Looper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Volley based implementation of {@link HomeRemoteDataSource}.
 */
public class DefaultHomeRemoteDataSource implements HomeRemoteDataSource {

    private final RequestQueue requestQueue;
    private final String apiUrl;
    private final Executor executor;
    private final Handler mainHandler;

    public DefaultHomeRemoteDataSource(RequestQueue requestQueue, String apiUrl) {
        this.requestQueue = requestQueue;
        this.apiUrl = apiUrl;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void fetchPromotedApps(PromotedAppsCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> executor.execute(() -> {
                    List<PromotedApp> result = parseResponse(response);
                    mainHandler.post(() -> callback.onResult(result));
                }),
                error -> mainHandler.post(() -> callback.onResult(Collections.emptyList()))
        );
        requestQueue.add(request);
    }

    private List<PromotedApp> parseResponse(JSONObject response) {
        List<PromotedApp> result = new ArrayList<>();
        try {
            JSONArray apps = response.getJSONObject("data").getJSONArray("apps");
            for (int i = 0; i < apps.length(); i++) {
                JSONObject obj = apps.getJSONObject(i);
                String pkg = obj.getString("packageName");
                if (pkg.contains("com.d4rk.androidtutorials")) {
                    continue;
                }
                result.add(new PromotedApp(
                        obj.getString("name"),
                        pkg,
                        obj.getString("iconLogo")
                ));
            }
        } catch (JSONException e) {
            result = Collections.emptyList();
        }
        return result;
    }
}
