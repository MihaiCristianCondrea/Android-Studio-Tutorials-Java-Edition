# Style Guidance

- Follow official Java coding conventions.
- Prefer immutable `final` fields and keep methods small and focused.
- Name classes and files using `PascalCase`; use `camelCase` for methods and variables.
- Each file should end with a trailing newline.
- Build UI with XML layouts and reference Material 3 components for colors, typography, and spacing.
- Use Java concurrency utilities or libraries such as RxJava for asynchronous work and state streams.
- Inject dependencies with Hilt or Dagger; obtain ViewModels via standard `ViewModelProvider` helpers.

## Contributor Guide: Adding a New Android Lesson

Use the lesson package template below for every new lesson under `ui/screens/android/lessons/`.

### Required package structure

```text
<category>/<lesson>/
├── <Lesson>Activity.java
├── tabs/                    # optional: tab fragments for code/layout split
│   ├── <Lesson>TabCodeFragment.java
│   └── <Lesson>TabLayoutFragment.java
```

### Required raw files

Create both source snippets in `app/src/main/res/raw/`:

- `text_<lesson>_java.txt`
- `text_<lesson>_xml.txt`

Use the same `<lesson>` token (snake_case) in both filenames.

### Registration points

After adding files, register the lesson in all required places:

1. **Activity declaration** in `app/src/main/AndroidManifest.xml`.
2. **Lesson entry** in `app/src/main/res/xml/preferences_android_studio.xml` via an intent target to `<Lesson>Activity`.
3. **Code viewer mapping** so the lesson key resolves to `text_<lesson>_java` and `text_<lesson>_xml` resources when opening syntax/code screens.

### Naming rules

- Class name and filename must match exactly (`<Lesson>Activity.java` -> `class <Lesson>Activity`).
- Keep lesson folders in lowercase (`<category>/<lesson>/`).
- Optional supporting classes are allowed, but each lesson package must contain a single canonical `<Lesson>Activity` entry point.

