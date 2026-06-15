# Xenos WSA - Windows Subsystem for Android

A complete Android application that emulates x86/x86-64 CPU architecture on ARM Android devices, enabling the execution of Windows applications on Android 12 and lower, including Android Go Edition.

## Features

✅ **Dual Emulation Modes**
- **Lightweight Mode**: Custom lightweight x86 emulator optimized for Android Go devices
- **QEMU Mode**: Full x86/x86-64 CPU emulation with better performance

✅ **Windows Compatibility**
- PE (Portable Executable) file support
- Windows API compatibility layer
- Registry simulation
- DLL module loading
- Virtual file system

✅ **File Management**
- Browse Android file system
- Install .exe applications
- Virtual drive management
- Permission-based access control

✅ **Graphics Rendering**
- OpenGL support
- Vulkan support
- Software rendering fallback

✅ **Performance Optimization**
- Multiple performance profiles (Power Saving, Balanced, Performance)
- Real-time performance monitoring
- CPU and memory management

✅ **Android Integration**
- Native Android app with home screen shortcut
- Material Design 3 UI
- Jetpack Compose framework
- Support for Android 12 and lower

## Project Structure

```
xenos-wsa/
├── app/
│   ├── src/main/
│   │   ├── java/com/xenos/wsa/
│   │   │   ├── MainActivity.kt                 # Main entry point
│   │   │   ├── models/
│   │   │   │   └── EmulatorModels.kt          # Data models
│   │   │   ├── emulator/
│   │   │   │   ├── LightweightEmulator.kt     # Custom emulator
│   │   │   │   ├── QemuEmulator.kt            # QEMU wrapper
│   │   │   │   ├── EmulatorManager.kt         # Unified manager
│   │   │   │   └── EmulatorService.kt         # Background service
│   │   │   ├── filesystem/
│   │   │   │   └── FileManager.kt             # File operations
│   │   │   └── ui/screens/
│   │   │       ├── HomeScreen.kt
│   │   │       ├── AppLauncherScreen.kt
│   │   │       ├── FileManagerScreen.kt
│   │   │       ├── SettingsScreen.kt
│   │   │       └── PerformanceMonitorScreen.kt
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   └── mipmap-hdpi/
│   │   │       └── ic_launcher.png
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## Architecture

### Lightweight Emulator
- Custom x86 CPU instruction decoder
- Virtual memory management (4GB address space)
- Process management
- Windows registry simulation
- DLL module loading

### QEMU Integration
- Full x86/x86-64 CPU emulation
- Pre-built QEMU binaries (downloaded on first launch)
- Process management via QEMU monitor
- Graphics support (OpenGL, Vulkan)

### Windows API Compatibility
- Registry access
- File system operations
- Process management
- Memory management
- DLL imports

## Building the APK

### Prerequisites
- Android Studio 2023.1 or later
- Android SDK 34 (API level 34)
- Kotlin 1.9.0
- Gradle 8.1

### Build Steps

1. **Clone/Download the project**
   ```bash
   cd xenos-wsa
   ```

2. **Build the APK**
   ```bash
   ./gradlew assembleRelease
   ```

3. **APK Location**
   ```
   app/build/outputs/apk/release/app-release.apk
   ```

### Build Variants

**Debug Build** (for testing)
```bash
./gradlew assembleDebug
```

**Release Build** (optimized, for distribution)
```bash
./gradlew assembleRelease
```

## Installation

### On Android Device

1. **Enable Unknown Sources**
   - Settings → Security → Unknown Sources (Enable)

2. **Install APK**
   ```bash
   adb install app-release.apk
   ```

3. **Or manually**
   - Copy APK to device
   - Open file manager
   - Tap APK to install

### Create Home Screen Shortcut
- Long-press on home screen
- Select "Apps"
- Find "Xenos WSA"
- Drag to home screen

## Usage

### Installing Windows Applications

1. Open Xenos WSA
2. Tap "Apps" → "Install New Application"
3. Select .exe file from device storage
4. Wait for installation to complete
5. Tap "Launch" to run

### Switching Emulation Mode

1. Open Xenos WSA
2. Go to "Home" tab
3. Select desired mode:
   - **Lightweight**: For Android Go, slower but works everywhere
   - **QEMU**: For better performance, requires more resources

### File Management

1. Tap "Files" tab
2. Browse Android file system
3. Create virtual drives
4. Manage installed applications

### Performance Monitoring

1. Tap "Monitor" tab
2. View real-time metrics:
   - CPU usage
   - Memory usage
   - Running processes
   - FPS

## System Requirements

### Minimum (Lightweight Mode)
- Android 12 or lower
- 512 MB RAM
- 100 MB storage
- ARM processor

### Recommended (QEMU Mode)
- Android 12 or lower
- 2+ GB RAM
- 500 MB storage
- ARMv8 processor

### Android Go Edition
- Fully supported with Lightweight mode
- May require performance optimization

## Configuration

### Emulator Settings

Edit `MainActivity.kt` to customize:

```kotlin
val config = EmulatorConfig(
    mode = EmulationMode.LIGHTWEIGHT,           // LIGHTWEIGHT or QEMU
    graphicsMode = GraphicsMode.OPENGL,         // OPENGL, VULKAN, SOFTWARE
    performanceProfile = PerformanceProfile.BALANCED,  // POWER_SAVING, BALANCED, PERFORMANCE
    cpuArchitecture = CpuArchitecture.X86_64,   // X86 or X86_64
    windowsVersion = WindowsVersion.WINDOWS_10, // Windows version
    cpuCores = 4,                               // CPU cores to allocate
    ramMb = 2048,                               // RAM in MB
    enableSound = true,                         // Enable audio
    enableNetworking = true,                    // Enable networking
    enableClipboard = true,                     // Enable clipboard sharing
    enableGpu = true                            // Enable GPU acceleration
)
```

## Permissions

The app requires the following permissions:

- `READ_EXTERNAL_STORAGE` - Browse files
- `WRITE_EXTERNAL_STORAGE` - Save files
- `MANAGE_EXTERNAL_STORAGE` - Full file access
- `INTERNET` - Network access
- `ACCESS_NETWORK_STATE` - Network status

## Troubleshooting

### App crashes on startup
- Clear app data: Settings → Apps → Xenos WSA → Storage → Clear Data
- Reinstall the app

### Slow performance
- Switch to QEMU mode (if device has enough resources)
- Close other apps
- Reduce graphics quality to Software rendering

### Cannot install applications
- Check file permissions
- Ensure .exe file is valid Windows executable
- Try different applications

### QEMU not downloading
- Check internet connection
- Ensure sufficient storage space
- Try manual download and placement in cache directory

## Development

### Adding New Features

1. **Create new Compose screen**
   ```kotlin
   @Composable
   fun NewFeatureScreen(navController: NavHostController) {
       // UI code
   }
   ```

2. **Add to navigation**
   ```kotlin
   composable("newfeature") {
       NewFeatureScreen(navController)
   }
   ```

3. **Add to bottom navigation**
   ```kotlin
   val items = listOf(
       "newfeature" to "New Feature"
   )
   ```

### Building Custom Emulator Extensions

1. Extend `LightweightEmulator` class
2. Implement additional CPU instructions
3. Add Windows API support

## Performance Tips

1. **For Android Go**
   - Use Lightweight mode
   - Disable GPU acceleration
   - Use Power Saving profile
   - Close background apps

2. **For High-End Devices**
   - Use QEMU mode
   - Enable GPU acceleration
   - Use Performance profile
   - Enable Vulkan rendering

3. **General**
   - Install lightweight applications
   - Avoid resource-heavy Windows apps
   - Monitor performance metrics
   - Clear cache regularly

## Known Limitations

- Not all Windows applications are supported
- Some advanced graphics features may not work
- Network performance depends on Android device
- Audio support is basic
- No printer support

## Future Enhancements

- [ ] DirectX support
- [ ] Advanced networking
- [ ] Printer support
- [ ] Audio passthrough
- [ ] Clipboard integration
- [ ] USB device support
- [ ] Multi-window support
- [ ] Game controller support

## License

This project is provided as-is for educational and personal use.

## Support

For issues, feature requests, or questions:
1. Check the troubleshooting section
2. Review the code comments
3. Test with different applications

## Credits

Built with:
- Android SDK
- Jetpack Compose
- Kotlin
- QEMU
- Material Design 3

---

**Xenos WSA v1.0.0** - Windows Subsystem for Android
