# Offline Keyboard

A fully offline, open-source Android keyboard (IME) written in Kotlin with Jetpack Compose. No network permission, no telemetry, no accounts — typing stays on the device.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4)
![License](https://img.shields.io/badge/license-MIT-blue)

## Features

- **Turkish Q layout** — `ğ ü ş ı ö ç` on the primary layout, `tr_TR` subtype.
- **Candidate strip** — prefix suggestions from a built-in frequency-ranked dictionary (English + Turkish), using an in-memory trie.
- **Number row** — optional top row with digits.
- **Symbols & emoji keyboards** — dedicated symbol page and a tabbed emoji picker with categories.
- **Automatic numeric PIN keyboard** — a large numeric pad appears automatically for numeric and password fields (including Turkish hints such as `şifre`, `parola`, `pin`, `kod`).
- **5 themes** — System, Light, Dark, Gboard Blue (default), Pitch Black.
- **Haptics and key sound**, both toggleable, plus number-row and suggestion switches.
- **Setup app** — a Compose screen with an in-app test field to try the keyboard immediately, a theme picker, and behavior toggles.

## Requirements

| Tool | Version |
| --- | --- |
| Android SDK | API 37 (`compileSdk` / `targetSdk`), `minSdk` 36 |
| JDK | 25 (provided by the Gradle toolchain, downloaded automatically) |
| Gradle | 9.7.1 (wrapper included) |
| AGP | 9.4.1 |

## Build

1. Point Gradle at your Android SDK by creating `local.properties`:

   ```properties
   sdk.dir=/path/to/Android/Sdk
   ```

2. Build the debug APK:

   ```bash
   ./gradlew assembleDebug
   ```

   The APK lands in `app/build/outputs/apk/debug/app-debug.apk`.

3. Install it on a device or emulator:

   ```bash
   ./gradlew installDebug
   # or
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

## Enable the keyboard

1. Go to **Settings → System → Languages & input → On-screen keyboard → Manage keyboards**.
2. Turn on **Offline Modern Keyboard**.
3. Select it as the active input method (keyboard switcher button in the navigation bar, or the system IME picker).
4. Open any text field and start typing — or launch **Offline Keyboard** and use the test field
   at the top of the screen.

## Project layout

```
app/src/main/java/com/keyboardd/
├── MainActivity.kt              # Setup and settings screen
├── data/
│   ├── DictionaryRepository.kt  # Trie + frequency-ranked word list
│   └── KeyboardSettings.kt      # SharedPreferences-backed settings
├── service/
│   └── KeyboardService.kt       # InputMethodService hosting Compose UI
└── ui/
    ├── KeyboardScreen.kt        # Mode routing, input handling
    ├── components/              # QWERTY, symbols, emoji, PIN, candidate strip
    └── theme/                   # Themes and color palettes
```

## Privacy

The app declares a single permission, `VIBRATE`. There is no `INTERNET` permission, so no keystrokes, suggestions, or usage data can leave the device. Settings are stored locally in `SharedPreferences`, and the bundled dictionary is compiled into the APK.

## License

[MIT](LICENSE)
