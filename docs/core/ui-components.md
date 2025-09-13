# UI Components

This page groups common Android View components used in AppToolkit.

## Buttons

Use buttons to trigger actions.

**XML**

```xml
<Button
    android:id="@+id/button_submit"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Submit" />
```

**Java**

```java
Button button = findViewById(R.id.button_submit);
button.setOnClickListener(v -> {
    // handle action
});
```

## Dialogs

Dialogs display critical information or request decisions.

```java
new AlertDialog.Builder(context)
    .setTitle("Title")
    .setMessage("Message")
    .setPositiveButton("OK", (d, w) -> {})
    .show();
```

## Form Fields

Collect user input with `EditText`.

**XML**

```xml
<EditText
    android:id="@+id/edit_name"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

**Java**

```java
EditText nameField = findViewById(R.id.edit_name);
String name = nameField.getText().toString();
```

## Layouts

Arrange UI elements with containers like `LinearLayout`.

**XML**

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">
    <TextView
        android:id="@+id/header"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Header" />
    <Button
        android:id="@+id/button_tap"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Tap" />
</LinearLayout>
```

## Feedback

Provide feedback with components like `Snackbar` or progress indicators.

```java
Snackbar.make(view, "Message", Snackbar.LENGTH_SHORT).show();
```

Return to [[Home]].

