package com.d4rk.androidtutorials.java.data.source.remote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.source.DefaultHomeRemoteDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(RobolectricTestRunner.class)
public class DefaultHomeRemoteDataSourceTest {

    private RequestQueue requestQueue;
    private DefaultHomeRemoteDataSource dataSource;
    private AtomicReference<JsonObjectRequest> lastRequest;

    @Before
    public void setUp() {
        requestQueue = mock(RequestQueue.class);
        lastRequest = new AtomicReference<>();
        when(requestQueue.add(any(Request.class))).thenAnswer(invocation -> {
            Request<?> request = invocation.getArgument(0);
            if (request instanceof JsonObjectRequest) {
                lastRequest.set((JsonObjectRequest) request);
            }
            return request;
        });
        dataSource = new DefaultHomeRemoteDataSource(requestQueue, "https://example.com/api");
    }

    @Test
    public void fetchPromotedApps_withValidResponse_deliversParsedAppsOnMainThread() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<PromotedApp>> resultRef = new AtomicReference<>();
        AtomicReference<Thread> threadRef = new AtomicReference<>();

        dataSource.fetchPromotedApps(apps -> {
            resultRef.set(apps);
            threadRef.set(Thread.currentThread());
            latch.countDown();
        });

        JsonObjectRequest request = captureLastRequest();
        deliverSuccess(request, createValidResponse());

        assertTrue("Callback not invoked", waitForCallback(latch));

        List<PromotedApp> apps = resultRef.get();
        assertNotNull(apps);
        assertEquals(1, apps.size());

        PromotedApp promotedApp = apps.get(0);
        assertEquals("Example App", promotedApp.name());
        assertEquals("com.example.promoted", promotedApp.packageName());
        assertEquals("https://example.com/icon.png", promotedApp.iconUrl());

        assertEquals(Looper.getMainLooper().getThread(), threadRef.get());
    }

    @Test
    public void fetchPromotedApps_withMalformedResponse_returnsEmptyListOnMainThread() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<PromotedApp>> resultRef = new AtomicReference<>();
        AtomicReference<Thread> threadRef = new AtomicReference<>();

        dataSource.fetchPromotedApps(apps -> {
            resultRef.set(apps);
            threadRef.set(Thread.currentThread());
            latch.countDown();
        });

        JsonObjectRequest request = captureLastRequest();
        deliverSuccess(request, createMalformedResponse());

        assertTrue("Callback not invoked", waitForCallback(latch));

        List<PromotedApp> apps = resultRef.get();
        assertNotNull(apps);
        assertTrue(apps.isEmpty());
        assertEquals(Looper.getMainLooper().getThread(), threadRef.get());
    }

    @Test
    public void fetchPromotedApps_onNetworkError_returnsEmptyListOnMainThread() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<PromotedApp>> resultRef = new AtomicReference<>();
        AtomicReference<Thread> threadRef = new AtomicReference<>();

        dataSource.fetchPromotedApps(apps -> {
            resultRef.set(apps);
            threadRef.set(Thread.currentThread());
            latch.countDown();
        });

        JsonObjectRequest request = captureLastRequest();
        deliverError(request, new VolleyError("network"));

        assertTrue("Callback not invoked", waitForCallback(latch));

        List<PromotedApp> apps = resultRef.get();
        assertNotNull(apps);
        assertTrue(apps.isEmpty());
        assertEquals(Looper.getMainLooper().getThread(), threadRef.get());
    }

    @Test
    public void parseResponse_skipsTutorialPackages() throws Exception {
        JSONObject response = createValidResponse();
        Method parseResponse = DefaultHomeRemoteDataSource.class
                .getDeclaredMethod("parseResponse", JSONObject.class);
        parseResponse.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<PromotedApp> apps = (List<PromotedApp>) parseResponse.invoke(dataSource, response);
        assertEquals(1, apps.size());
        assertEquals("com.example.promoted", apps.get(0).packageName());
    }

    private JsonObjectRequest captureLastRequest() {
        JsonObjectRequest request = lastRequest.get();
        assertNotNull("Expected a JsonObjectRequest to be enqueued", request);
        return request;
    }

    private void deliverSuccess(JsonObjectRequest request, JSONObject response) throws Exception {
        @SuppressWarnings("unchecked")
        Response.Listener<JSONObject> listener = (Response.Listener<JSONObject>) getFieldValue(request, "mListener");
        assertNotNull(listener);
        listener.onResponse(response);
    }

    private void deliverError(JsonObjectRequest request, VolleyError error) throws Exception {
        Response.ErrorListener listener = (Response.ErrorListener) getFieldValue(request, "mErrorListener");
        assertNotNull(listener);
        listener.onErrorResponse(error);
    }

    private Object getFieldValue(Object target, String fieldName) throws Exception {
        Class<?> type = target.getClass();
        while (type != null) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException ignored) {
                type = type.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    private boolean waitForCallback(CountDownLatch latch) throws InterruptedException {
        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1);
        while (latch.getCount() > 0 && System.currentTimeMillis() < deadline) {
            Shadows.shadowOf(Looper.getMainLooper()).idle();
            Thread.sleep(1);
        }
        return latch.getCount() == 0;
    }

    private static JSONObject createValidResponse() throws JSONException {
        JSONObject first = new JSONObject();
        first.put("name", "Example App");
        first.put("packageName", "com.example.promoted");
        first.put("iconLogo", "https://example.com/icon.png");

        JSONObject blocked = new JSONObject();
        blocked.put("name", "Tutorial App");
        blocked.put("packageName", "com.d4rk.androidtutorials");
        blocked.put("iconLogo", "https://example.com/tutorial.png");

        JSONArray apps = new JSONArray();
        apps.put(first);
        apps.put(blocked);

        JSONObject data = new JSONObject();
        data.put("apps", apps);

        JSONObject response = new JSONObject();
        response.put("data", data);
        return response;
    }

    private static JSONObject createMalformedResponse() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("invalid", new JSONArray());

        JSONObject response = new JSONObject();
        response.put("data", data);
        return response;
    }
}
