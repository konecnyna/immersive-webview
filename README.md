# Immersive WebView

A modern Android immersive WebView app built with Jetpack Compose, Material 3, and AndroidX WebKit.

## Features

- Full-screen immersive WebView that draws behind the status bar
- Customizable URL via edit dialog
- Minimal bottom navigation: Back, Forward, Reload, Edit URL
- Material You dynamic colors (Android 12+)
- Edge-to-edge display
- Loading progress indicator

## Tech Stack

- **Kotlin 2.1** with Compose compiler plugin
- **Jetpack Compose** with Material 3
- **AndroidX WebKit** for modern WebView APIs
- **ViewModel** + **StateFlow** for state management
- **Edge-to-edge** via `enableEdgeToEdge()`
- **Version catalog** (`libs.versions.toml`) for dependency management

## Building

```bash
gradle assembleDebug
```

## Testing

```bash
# Unit tests
gradle testDebugUnitTest

# Instrumented tests (requires device/emulator)
gradle connectedDebugAndroidTest
```

## Download

Check the [Releases](https://github.com/konecnyna/immersive-webview/releases) page for the latest APK.
