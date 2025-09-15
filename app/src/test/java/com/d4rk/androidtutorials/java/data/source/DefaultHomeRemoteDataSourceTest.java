package com.d4rk.androidtutorials.java.data.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Handler;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class DefaultHomeRemoteDataSourceTest {

    @Test
    @SuppressWarnings("unchecked")
    public void fetchPromotedApps_filtersInternalPackages() throws Exception {
        RequestQueue requestQueue = mock(RequestQueue.class);
        DefaultHomeRemoteDataSource dataSource = new DefaultHomeRemoteDataSource(requestQueue, "https://example.com");
        makeSynchronous(dataSource);
        HomeRemoteDataSource.PromotedAppsCallback callback = mock(HomeRemoteDataSource.PromotedAppsCallback.class);

        dataSource.fetchPromotedApps(callback);

        ArgumentCaptor<JsonObjectRequest> requestCaptor = ArgumentCaptor.forClass(JsonObjectRequest.class);
        verify(requestQueue).add(requestCaptor.capture());
        JsonObjectRequest capturedRequest = requestCaptor.getValue();

        JSONObject response = new JSONObject()
                .put("data", new JSONObject()
                        .put("apps", new JSONArray()
                                .put(new JSONObject()
                                        .put("name", "Android Tutorials")
                                        .put("packageName", "com.d4rk.androidtutorials.sample")
                                        .put("iconLogo", "internal.png"))
                                .put(new JSONObject()
                                        .put("name", "Cool App")
                                        .put("packageName", "com.example.cool")
                                        .put("iconLogo", "cool.png"))
                                .put(new JSONObject()
                                        .put("name", "Another App")
                                        .put("packageName", "org.sample.another")
                                        .put("iconLogo", "another.png"))));

        invokeDeliverResponse(capturedRequest, response);

        ArgumentCaptor<List> appsCaptor = ArgumentCaptor.forClass(List.class);
        verify(callback).onResult(appsCaptor.capture());
        List<PromotedApp> apps = appsCaptor.getValue();
        List<PromotedApp> expected = Arrays.asList(
                new PromotedApp("Cool App", "com.example.cool", "cool.png"),
                new PromotedApp("Another App", "org.sample.another", "another.png"));
        assertEquals(expected, apps);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void fetchPromotedApps_errorReturnsEmptyList() throws Exception {
        RequestQueue requestQueue = mock(RequestQueue.class);
        DefaultHomeRemoteDataSource dataSource = new DefaultHomeRemoteDataSource(requestQueue, "https://example.com");
        makeSynchronous(dataSource);
        HomeRemoteDataSource.PromotedAppsCallback callback = mock(HomeRemoteDataSource.PromotedAppsCallback.class);

        dataSource.fetchPromotedApps(callback);

        ArgumentCaptor<JsonObjectRequest> requestCaptor = ArgumentCaptor.forClass(JsonObjectRequest.class);
        verify(requestQueue).add(requestCaptor.capture());
        JsonObjectRequest capturedRequest = requestCaptor.getValue();

        capturedRequest.deliverError(new VolleyError("network"));

        ArgumentCaptor<List> appsCaptor = ArgumentCaptor.forClass(List.class);
        verify(callback).onResult(appsCaptor.capture());
        List<PromotedApp> apps = appsCaptor.getValue();
        assertTrue(apps.isEmpty());
    }

    private static void makeSynchronous(DefaultHomeRemoteDataSource dataSource) throws Exception {
        Handler handler = mock(Handler.class);
        when(handler.post(any())).thenAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return true;
        });

        setField(dataSource, "executor", (Executor) Runnable::run);
        setField(dataSource, "mainHandler", handler);
    }

    private static void setField(DefaultHomeRemoteDataSource dataSource, String name, Object value) throws Exception {
        Field field = DefaultHomeRemoteDataSource.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(dataSource, value);
    }

    private static void invokeDeliverResponse(JsonObjectRequest request, JSONObject response) throws Exception {
        Method deliverResponse = JsonObjectRequest.class.getDeclaredMethod("deliverResponse", JSONObject.class);
        deliverResponse.setAccessible(true);
        deliverResponse.invoke(request, response);
    }
}
