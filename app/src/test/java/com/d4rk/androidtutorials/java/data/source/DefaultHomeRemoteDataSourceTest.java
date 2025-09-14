package com.d4rk.androidtutorials.java.data.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.android.volley.RequestQueue;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class DefaultHomeRemoteDataSourceTest {

    private DefaultHomeRemoteDataSource dataSource;
    private Method parseMethod;

    @Before
    public void setUp() throws Exception {
        dataSource = mock(DefaultHomeRemoteDataSource.class);
        Field queueField = DefaultHomeRemoteDataSource.class.getDeclaredField("requestQueue");
        queueField.setAccessible(true);
        queueField.set(dataSource, mock(RequestQueue.class));
        Field urlField = DefaultHomeRemoteDataSource.class.getDeclaredField("apiUrl");
        urlField.setAccessible(true);
        urlField.set(dataSource, "https://example.com");
        parseMethod = DefaultHomeRemoteDataSource.class.getDeclaredMethod("parseResponse", JSONObject.class);
        parseMethod.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    private List<PromotedApp> invokeParse(JSONObject json) throws Exception {
        return (List<PromotedApp>) parseMethod.invoke(dataSource, json);
    }

    @Test
    public void parseResponseFiltersOwnPackages() throws Exception {
        String json = "{\"data\":{\"apps\":[" +
                "{\"name\":\"App1\",\"packageName\":\"com.example.app1\",\"iconLogo\":\"logo1\"}," +
                "{\"name\":\"App2\",\"packageName\":\"com.d4rk.androidtutorials.sample\",\"iconLogo\":\"logo2\"}]}}";
        List<PromotedApp> result = invokeParse(new JSONObject(json));
        assertEquals(1, result.size());
        assertEquals("com.example.app1", result.get(0).packageName());
    }

    @Test
    public void parseResponseMalformedJsonReturnsEmptyList() throws Exception {
        String json = "{\"data\":{}}";
        List<PromotedApp> result = invokeParse(new JSONObject(json));
        assertTrue(result.isEmpty());
    }
}
