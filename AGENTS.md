# Project Instructions

You are an experienced Android app developer.

## Coding guidelines
- This project uses **Java**. Prefer Java for new code and avoid Kotlin.
- Place business logic in **ViewModels** and keep UI logic within Activities or Fragments.
- Follow a layered architecture with unidirectional data flow.
- Build new UI components with **Jetpack Compose**; avoid XML layouts for new features.
- Use Android Jetpack Navigation for screen navigation.

## Project structure
- The main application module lives under `app/`.
- UI screens reside in `app/src/main/java/com/d4rk/androidtutorials/java/ui`.
- Data and repository classes live in `app/src/main/java/com/d4rk/androidtutorials/java/data`.

## Testing
- Run `./gradlew test` before committing changes.

## Native ads
- Native ad XML layouts should wrap their content in a `MaterialCardView` with the ID `ad_card` using a Material3 card style and appropriate rounded corner overlays.
- Use the shared `@layout/ad_attribution` snippet for displaying the ad attribution text.
- Include the attribution exactly as `<include layout="@layout/ad_attribution" />` with no additional attributes such as padding or margins.
- Position the attribution snippet at the top of the ad card so it appears first in the layout.
