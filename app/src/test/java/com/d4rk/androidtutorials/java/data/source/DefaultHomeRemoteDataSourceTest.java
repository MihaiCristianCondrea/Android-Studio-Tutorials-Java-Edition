package com.d4rk.androidtutorials.java.data.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultHomeRemoteDataSourceTest {

    private RequestQueue requestQueue;
    private MockedStatic<Executors> executorsMock;
    private MockedConstruction<Handler> handlerConstruction;
    private MockedConstruction<JsonObjectRequest> requestConstruction;
    private Response.Listener<JSONObject> successListener;
    private Response.ErrorListener errorListener;
    private int capturedMethod;
    private String capturedUrl;
    private DefaultHomeRemoteDataSource dataSource;

    @Before
    public void setUp() {
        requestQueue = mock(RequestQueue.class);
        capturedMethod = -1;
        capturedUrl = null;
        successListener = null;
        errorListener = null;

        Executor immediate = Runnable::run;
        executorsMock = mockStatic(Executors.class);
        executorsMock.when(Executors::newSingleThreadExecutor).thenReturn(immediate);

        handlerConstruction = mockConstruction(Handler.class, (mock, context) ->
                when(mock.post(any(Runnable.class))).thenAnswer(invocation -> {
                    Runnable runnable = invocation.getArgument(0);
                    runnable.run();
                    return true;
                })
        );

        requestConstruction = mockConstruction(JsonObjectRequest.class, (mock, context) -> {
            capturedMethod = (int) context.arguments().get(0);
            capturedUrl = (String) context.arguments().get(1);
            successListener = (Response.Listener<JSONObject>) context.arguments().get(3);
            errorListener = (Response.ErrorListener) context.arguments().get(4);
        });

        dataSource = new DefaultHomeRemoteDataSource(requestQueue, "https://example.com/api");
    }

    @After
    public void tearDown() {
        requestConstruction.close();
        handlerConstruction.close();
        executorsMock.close();
    }

    @Test
    public void fetchPromotedApps_addsRequestAndDeliversParsedList() throws JSONException {
        AtomicReference<List<PromotedApp>> resultRef = new AtomicReference<>();
        dataSource.fetchPromotedApps(resultRef::set);

        assertEquals(Request.Method.GET, capturedMethod);
        assertEquals("https://example.com/api", capturedUrl);
        verify(requestQueue).add(requestConstruction.constructed().get(0));
        assertNotNull("Success listener should be captured", successListener);

        successListener.onResponse(buildResponse());

        List<PromotedApp> result = resultRef.get();
        assertNotNull(result);
        assertEquals(1, result.size());
        PromotedApp app = result.get(0);
        assertEquals("Cool App", app.name());
        assertEquals("com.example.cool", app.packageName());
        assertEquals("https://example.com/icon.png", app.iconUrl());
    }

    @Test
    public void fetchPromotedApps_returnsEmptyListWhenParseFails() {
        AtomicReference<List<PromotedApp>> resultRef = new AtomicReference<>();
        dataSource.fetchPromotedApps(resultRef::set);

        assertNotNull(successListener);
        successListener.onResponse(new JSONObject());

        List<PromotedApp> result = resultRef.get();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void fetchPromotedApps_returnsEmptyListOnVolleyError() {
        AtomicReference<List<PromotedApp>> resultRef = new AtomicReference<>();
        dataSource.fetchPromotedApps(resultRef::set);

        assertNotNull(errorListener);
        errorListener.onErrorResponse(new VolleyError("boom"));

        List<PromotedApp> result = resultRef.get();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private static JSONObject buildResponse() throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray apps = new JSONArray();
        apps.put(new JSONObject()
                .put("name", "Cool App")
                .put("packageName", "com.example.cool")
                .put("iconLogo", "https://example.com/icon.png"));
        apps.put(new JSONObject()
                .put("name", "Ignore App")
                .put("packageName", "com.d4rk.androidtutorials.other")
                .put("iconLogo", "https://example.com/ignore.png"));
        data.put("apps", apps);
        response.put("data", data);
        return response;
    }
}
