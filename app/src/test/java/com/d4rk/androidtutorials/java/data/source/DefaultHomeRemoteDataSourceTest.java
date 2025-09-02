package com.d4rk.androidtutorials.java.data.source;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 33)
public class DefaultHomeRemoteDataSourceTest {

    @Test
    public void fetchPromotedAppsFiltersAndMapsResults() throws Exception {
        RequestQueue queue = mock(RequestQueue.class);
        ArgumentCaptor<JsonObjectRequest> captor = ArgumentCaptor.forClass(JsonObjectRequest.class);

        DefaultHomeRemoteDataSource source = new DefaultHomeRemoteDataSource(queue, "url");

        final List<PromotedApp>[] results = new List[2];
        final int[] index = {0};
        source.fetchPromotedApps(apps -> results[index[0]++] = apps);

        verify(queue).add(captor.capture());
        JsonObjectRequest request = captor.getValue();

        String json = "{\"data\":{\"apps\":[{\"name\":\"Name1\",\"packageName\":\"pkg1\",\"iconLogo\":\"icon1\"},{\"name\":\"Name2\",\"packageName\":\"com.d4rk.androidtutorials.other\",\"iconLogo\":\"icon2\"}]}}";
        JSONObject response = new JSONObject(json);

        Field listenerField = Request.class.getDeclaredField("mListener");
        listenerField.setAccessible(true);
        @SuppressWarnings("unchecked")
        com.android.volley.Response.Listener<JSONObject> listener =
                (com.android.volley.Response.Listener<JSONObject>) listenerField.get(request);
        listener.onResponse(response);

        assertEquals(1, results[0].size());
        PromotedApp app = results[0].get(0);
        assertEquals("Name1", app.name());
        assertEquals("pkg1", app.packageName());
        assertEquals("icon1", app.iconUrl());

        Field errorField = Request.class.getDeclaredField("mErrorListener");
        errorField.setAccessible(true);
        com.android.volley.Response.ErrorListener errorListener =
                (com.android.volley.Response.ErrorListener) errorField.get(request);
        errorListener.onErrorResponse(new VolleyError());

        assertTrue(results[1].isEmpty());
    }
}
