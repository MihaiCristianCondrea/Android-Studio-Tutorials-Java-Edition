# Help Screen

## Overview
The Help screen provides users with access to frequently asked questions (FAQs), options to submit feedback, and links to important information like the app's privacy policy, terms of service, and open-source licenses.

## Structure
The Help screen is implemented as an `Activity` that hosts different sections using `PreferenceFragmentCompat`:
-   **`HelpActivity`**: The main activity for this screen. It manages the layout and hosts the fragments.
-   **`FaqFragment`**: Displays a list of frequently asked questions, loaded from `R.xml.preferences_faq`.
-   **`FeedbackFragment`**: Contains preferences related to user feedback, including an option to rate the app. This is loaded from `R.xml.preferences_feedback`.

## Features
The Help screen offers the following functionalities, accessible primarily through an options menu:
-   **View FAQs**: Users can browse a list of common questions and answers.
-   **Provide Feedback**: Users can initiate a review flow or be directed to the Google Play Store to leave a review.
-   **View in Google Play**: Opens the app's listing on the Google Play Store.
-   **Version Info**: Displays a dialog with the app's name, version, and copyright information.
-   **Beta Program**: Opens a link to join the app's beta testing program on Google Play.
-   **Terms of Service**: Opens a web link to the app's Terms of Service.
-   **Privacy Policy**: Opens a web link to the app's Privacy Policy.
-   **Open Source Licenses**: Displays a screen with a list of open-source libraries used in the app and their licenses.

## Ads
The Help page displays a single native ad between the FAQ list and the Contact Us card.
The banner is rendered by `HelpNativeAdBanner` which uses the shared `native_ad` configuration.
An **Ad** label is shown to comply with policy requirements and padding ensures the banner
does not interfere with surrounding content.
See [Ads](settings/privacy/ads.md) for more information on ad configuration.
*(Note: Presence of ads should be confirmed by checking layout files like `activity_help.xml`)*

## Integration
To launch the Help screen, use the following Kotlin code:
```kotlin
startActivity(Intent(context, HelpActivity::class.java))
```
Or in Java:
```java
Intent intent = new Intent(context, HelpActivity.class);
startActivity(intent);
```