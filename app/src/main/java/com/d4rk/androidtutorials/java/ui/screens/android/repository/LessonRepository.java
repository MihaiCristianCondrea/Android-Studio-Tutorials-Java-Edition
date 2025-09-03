package com.d4rk.androidtutorials.java.ui.screens.android.repository;

import com.d4rk.androidtutorials.java.R;

public class LessonRepository implements com.d4rk.androidtutorials.java.data.repository.LessonRepository {

    public com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson getLesson(String lessonName) {
        return switch (lessonName) {
            case "AlertDialog" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.alert_dialog, R.raw.text_alertdialog_java, R.raw.text_center_button_xml);
            case "SnackBar" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.snack_bar, R.raw.text_snack_bar_java, R.raw.text_center_button_xml);
            case "Toast" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.toast, R.raw.text_toast_java, R.raw.text_center_button_xml);
            case "ImageButtons" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.image_buttons, R.raw.text_image_buttons_java, R.raw.text_image_buttons_xml);
            case "RadioButtons" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.radio_buttons, R.raw.text_radio_buttons_java, R.raw.text_radio_buttons_xml);
            case "Switch" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.switches, R.raw.text_toggle_java, R.raw.text_toggle_xml);
            case "Chronometer" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.chronometer, R.raw.text_chronometer_java, R.raw.text_chronometer_layout_xml);
            case "DatePicker" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.datepicker, R.raw.text_datepicker_java, R.raw.text_datepicker_xml);
            case "TimePicker" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.timepicker, R.raw.text_timepicker_java, R.raw.text_timepicker_xml);
            case "InboxNotification" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.inbox_notifications, R.raw.text_inbox_notification_java, R.raw.text_center_button_xml);
            case "SimpleNotification" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.simple_notifications, R.raw.text_simple_notification_java, R.raw.text_center_button_xml);
            case "RatingBar" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.rating_bar, R.raw.text_rating_bar_java, R.raw.text_rating_bar_xml);
            case "PasswordBox" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.password_box, R.raw.text_password_java, R.raw.text_password_xml);
            case "TextBox" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.textbox, R.raw.text_textbox_java, R.raw.text_textbox_xml);
            case "GridView" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.grid_view, R.raw.text_grid_view_java, R.raw.text_grid_view_xml);
            case "WebView" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.web_view, R.raw.text_webview_java, R.raw.text_webview_xml);
            case "Retrofit" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.retrofit, R.raw.text_retrofit_java, R.raw.text_retrofit_xml);
            case "BottomNavigation" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.bottom_navigation, R.raw.text_bottom_navigation_java, R.raw.text_bottom_navigation_xml);
            case "NavigationDrawer" ->
                    new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(R.string.navigation_drawer, R.raw.text_navigation_drawer_java, R.raw.text_navigation_drawer_xml);
            default -> throw new IllegalArgumentException("Unknown lesson: " + lessonName);
        };
    }
}