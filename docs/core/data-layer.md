# Data Layer

This page outlines how the app manages and persists data.

## Repositories

Repositories expose data to the rest of the app and hide the underlying storage.

```java
public interface MainRepository {
    boolean shouldShowStartupScreen();
    void markStartupScreenShown();
}
```

`DefaultMainRepository` implements these methods using `SharedPreferences`:

```java
public class DefaultMainRepository implements MainRepository {
    private final Context context;

    public DefaultMainRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public boolean shouldShowStartupScreen() {
        SharedPreferences startup = context.getSharedPreferences("startup", Context.MODE_PRIVATE);
        return startup.getBoolean("value", true);
    }

    @Override
    public void markStartupScreenShown() {
        SharedPreferences startup = context.getSharedPreferences("startup", Context.MODE_PRIVATE);
        startup.edit().putBoolean("value", false).apply();
    }
}
```

## Data sources

Remote and local sources supply the repositories with data. For example, `DefaultHomeRemoteDataSource` uses Volley to fetch promoted apps:

```java
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
                response -> { /* parse and callback */ },
                error -> { /* handle error */ }
        );
        requestQueue.add(request);
    }
}
```

## Models

Model classes like `PromotedApp` encapsulate the data returned by the layer:

```java
public record PromotedApp(String name, String packageName, String iconUrl) {}
```

## See also

- [[Architecture]] – overview of app layers.
- [[Core Module]] – shared utilities and components.

