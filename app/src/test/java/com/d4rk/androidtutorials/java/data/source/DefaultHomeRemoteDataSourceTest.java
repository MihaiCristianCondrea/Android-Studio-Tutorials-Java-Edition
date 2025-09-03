package com.d4rk.androidtutorials.java.data.source;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultHomeRemoteDataSourceTest {

    private static class FakeRequestQueue extends RequestQueue {
        Response.Listener<JSONObject> success;
        Response.ErrorListener error;

        FakeRequestQueue() {
            super(null, null);
        }

        @Override
        public <T> Request<T> add(Request<T> request) {
            try {
                Field listenerField = JsonRequest.class.getDeclaredField("mListener");
                listenerField.setAccessible(true);
                success = (Response.Listener<JSONObject>) listenerField.get(request);

                Field errorField = Request.class.getDeclaredField("mErrorListener");
                errorField.setAccessible(true);
                error = (Response.ErrorListener) errorField.get(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return request;
        }

        void triggerSuccess(JSONObject obj) {
            success.onResponse(obj);
        }

        void triggerError(VolleyError volleyError) {
            error.onErrorResponse(volleyError);
        }
    }

    @Test
    public void fetchPromotedApps_parsesAndFilters() throws Exception {
        FakeRequestQueue queue = new FakeRequestQueue();
        DefaultHomeRemoteDataSource source = new DefaultHomeRemoteDataSource(queue, "url");
        final List<PromotedApp>[] result = new List[1];
        source.fetchPromotedApps(apps -> result[0] = apps);

        JSONObject payload = new JSONObject("{\"data\":{\"apps\":[{\"name\":\"One\",\"packageName\":\"com.example.one\",\"iconLogo\":\"icon1\"},{\"name\":\"Two\",\"packageName\":\"com.d4rk.androidtutorials.other\",\"iconLogo\":\"icon2\"}]}}");

        queue.triggerSuccess(payload);

        assertNotNull(result[0]);
        assertEquals(1, result[0].size());
        PromotedApp app = result[0].get(0);
        assertEquals("One", app.name());
        assertEquals("com.example.one", app.packageName());
        assertEquals("icon1", app.iconUrl());
    }

    @Test
    public void fetchPromotedApps_errorReturnsEmpty() {
        FakeRequestQueue queue = new FakeRequestQueue();
        DefaultHomeRemoteDataSource source = new DefaultHomeRemoteDataSource(queue, "url");
        final List<PromotedApp>[] result = new List[1];
        source.fetchPromotedApps(apps -> result[0] = apps);

        queue.triggerError(new VolleyError("boom"));

        assertNotNull(result[0]);
        assertTrue(result[0].isEmpty());
    }
}
