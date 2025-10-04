# 2048 Android Game

A fully functional implementation of the popular 2048 puzzle game for Android.

## Features

- 🎮 Classic 2048 gameplay
- 📱 Intuitive swipe controls
- 🎨 Beautiful, modern UI with smooth animations
- 📊 Score tracking with best score display
- 🏆 Win detection when reaching 2048
- 🔄 New game functionality
- ⚡ Responsive touch gestures

## Game Rules

- Swipe in any direction (up, down, left, right) to move all tiles
- When two tiles with the same number touch, they merge into one
- Each move adds a new tile (2 or 4) to the board
- The goal is to create a tile with the value 2048
- The game ends when no more moves are possible

## Project Structure

```
2048-clone/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/game/twenty48/
│   │       │   ├── MainActivity.kt        # Main activity
│   │       │   ├── GameView.kt           # Custom view for game board
│   │       │   ├── GameManager.kt        # Game logic controller
│   │       │   ├── Grid.kt               # Grid data structure
│   │       │   ├── Tile.kt               # Tile model
│   │       │   └── Direction.kt          # Direction enum
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml # Main layout
│   │       │   └── values/
│   │       │       ├── strings.xml       # String resources
│   │       │       ├── colors.xml        # Color definitions
│   │       │       └── themes.xml        # App theme
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Requirements

- Android Studio (Arctic Fox or newer)
- JDK 17 or higher
- Android SDK 24 (Android 7.0) or higher
- Kotlin 1.9.0

## Setup Instructions

1. **Clone or Download the Project**
   ```bash
   cd "2048-clone"
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory and open it

3. **Sync Gradle**
   - Android Studio should automatically prompt you to sync Gradle
   - If not, click "File" → "Sync Project with Gradle Files"

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green triangle) in Android Studio
   - Select your target device
   - The app should build and install automatically

## Building the APK

### Debug APK
```bash
./gradlew assembleDebug
```
The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK
```bash
./gradlew assembleRelease
```
The APK will be located at: `app/build/outputs/apk/release/app-release.apk`

## Technical Details

### Architecture

- **MVVM-inspired Pattern**: Separation of concerns between UI (GameView) and logic (GameManager)
- **Custom Views**: Custom GameView extends View for efficient rendering
- **Gesture Detection**: Uses GestureDetector for smooth swipe recognition
- **State Management**: GameManager handles all game state and notifies UI of changes

### Key Classes

- **GameManager**: Core game logic including tile movement, merging, and win/lose detection
- **Grid**: 4x4 grid data structure managing tile positions
- **Tile**: Represents individual tiles with values and positions
- **GameView**: Custom view that handles rendering and touch input
- **MainActivity**: Main activity coordinating UI components

### Color Scheme

The app uses the classic 2048 color scheme:
- Background: `#FAF8EF`
- Grid: `#BBADA0`
- Tiles: Different colors for each value (2, 4, 8, 16, etc.)

## Customization

### Change Grid Size
To change the grid size from 4x4 to another size, modify the `size` parameter in:
- `GameManager.kt`: Constructor parameter
- `GameView.kt`: `gridSize` property

### Modify Colors
Edit colors in:
- `app/src/main/res/values/colors.xml`
- `GameView.kt`: `getTileColor()` method

### Adjust Difficulty
In `Tile.kt`, change the probability of spawning 4 instead of 2:
```kotlin
return Tile(if (Math.random() < 0.9) 2 else 4, row, col)
// Change 0.9 to adjust probability (higher = more 2s, lower = more 4s)
```

## Troubleshooting

### Build Fails
- Ensure you have JDK 17 installed
- Check that Android SDK is properly installed
- Sync Gradle files again

### App Crashes on Launch
- Check that minimum SDK version is Android 7.0 (API 24) or higher
- Verify all dependencies are properly resolved

### Swipe Gestures Not Working
- Ensure you're testing on a physical device or a properly configured emulator
- Check that touch events are not being intercepted by parent views

## License

This is a learning project. Feel free to use and modify as needed.

## Credits

Based on the original 2048 game by Gabriele Cirulli.

