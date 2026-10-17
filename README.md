# VibeSpace 🎵

> A personal, ad-free Android music player built with **Kotlin**, **Jetpack Compose**, and **Media3 ExoPlayer**.

VibeSpace plays your local audio files directly from your device storage with a sleek, Spotify-inspired dark UI, smooth animations, and complete background playback support.

---

## ✨ Features (MVP)

- 📱 **Browse & Search Library**: Automatically scans your device's audio files (MP3, FLAC, AAC, etc.) using Android's `MediaStore` API and caches metadata in Room. Live search across titles, artists, and albums.
- ⏯️ **Modern Playback Engine**: Powered by **Media3 ExoPlayer** with background audio support, audio focus handling, and automatic pause on headphone disconnect.
- 🎛️ **Now Playing & Mini-Player**:
  - Persistent bottom mini-player with progress line and quick controls.
  - Full-screen modal bottom sheet with high-resolution artwork, seek slider, elapsed/remaining timestamps, shuffle, and repeat modes.
- 📜 **Queue Management**: Inspect what's playing next, remove tracks, or clear the queue. Queue and playback progress are persisted across app restarts.
- 📂 **Playlists**: Create custom playlists, add and remove tracks.
- 🔒 **System & Lock Screen Controls**: Integrates with Android `MediaSession` to provide lock screen controls, status bar media notifications, and Bluetooth hardware controls.

---

## 🛠 Tech Stack & Architecture

- **Language**: Kotlin 2.0
- **UI**: Jetpack Compose + Material 3 (custom dark theme with blue accent)
- **Architecture**: MVVM + Repository Pattern + Clean Architecture
- **Dependency Injection**: Hilt (Dagger)
- **Audio Engine**: AndroidX Media3 (ExoPlayer + MediaSessionService + MediaController)
- **Local Database**: Room 2.6 (with Kotlin Flow for real-time UI updates)
- **Image Loading**: Coil 2.7
- **Target OS**: Android 13+ (API 33+) with modern `READ_MEDIA_AUDIO` permission handling

---

## 🚀 How to Run

1. Open **Android Studio** (Hedgehog / Iguana / Jellyfish or newer).
2. Select **Open** and choose this project directory (`c:\Users\Maru\Desktop\coding\1`).
3. Allow Gradle to sync dependencies.
4. Run the project on an Android device or emulator running **API 33+**.
5. Grant the **Audio / Media** permission when prompted on launch to scan local music files into your library.
