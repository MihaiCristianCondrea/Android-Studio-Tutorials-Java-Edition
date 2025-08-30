package com.d4rk.androidtutorials.java.data.source;

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

/**
 * Volley based implementation of {@link HomeRemoteDataSource}.
 */
public class DefaultHomeRemoteDataSource implements HomeRemoteDataSource {

    private final RequestQueue requestQueue;
    private final String apiUrl;

    public DefaultHomeRemoteDataSource(RequestQueue requestQueue, String apiUrl) {
        this.requestQueue = requestQueue;
        this.apiUrl = apiUrl;
    }

    @Override
    public void fetchPromotedApps(PromotedAppsCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> {
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
                    callback.onResult(result);
                },
                error -> callback.onResult(Collections.emptyList())
        );
        requestQueue.add(request);
    }
}
