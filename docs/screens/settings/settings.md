# Settings

## Overview
The settings screen allows users to configure application preferences. The main entry point is `SettingsActivity.java`, which hosts `SettingsFragment.java` for displaying the preference items. A `SettingsViewModel.java` handles the business logic and data persistence for the settings.

## Key Components
- **`SettingsActivity.java`**: The main activity for settings. It sets up the fragment and handles theme changes based on preferences.
- **`SettingsFragment.java`**: Displays the actual settings UI using AndroidX Preference components.
- **`SettingsViewModel.java`**: Manages settings data, listens for preference changes, and applies them (e.g., dark mode).

## Integration
To launch the settings screen:
```java
Intent intent = new Intent(context, SettingsActivity.class);
startActivity(intent);
```

