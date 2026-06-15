# Xenos WSA - Build and Deployment Guide

## Complete Instructions for Building APK and Deploying to Android

### Part 1: Prerequisites

#### System Requirements
- Linux, macOS, or Windows
- Java Development Kit (JDK) 17 or later
- Android SDK (API 34)
- Gradle 8.1 or later
- 4GB RAM minimum
- 5GB disk space

#### Installation

**On Ubuntu/Debian:**
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk-headless
```

**Download Android SDK:**
- Download Android Studio from https://developer.android.com/studio
- Or use command-line tools

### Part 2: Project Setup

```bash
# Navigate to project directory
cd xenos-wsa

# Make gradle wrapper executable
chmod +x gradlew

# Verify gradle installation
./gradlew --version
```

### Part 3: Building APK

#### Debug Build (for testing)
```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

#### Release Build (for distribution)
```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

#### Build with Specific Variant
```bash
# Build only
./gradlew build

# Clean build
./gradlew clean build

# Build and run tests
./gradlew build connectedAndroidTest
```

### Part 4: Signing APK (Required for Release)

#### Generate Keystore
```bash
keytool -genkey -v -keystore xenos-wsa.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias xenos-wsa-key
```

#### Sign APK
```bash
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore xenos-wsa.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  xenos-wsa-key

# Align APK
zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/app-release-signed.apk
```

### Part 5: Installation on Device

#### Via ADB (Android Debug Bridge)

1. **Enable USB Debugging on Device**
   - Settings → Developer Options → USB Debugging (Enable)

2. **Connect Device**
   ```bash
   adb devices
   ```

3. **Install APK**
   ```bash
   # Debug APK
   adb install app/build/outputs/apk/debug/app-debug.apk

   # Release APK
   adb install app/build/outputs/apk/release/app-release-signed.apk
   ```

4. **Run App**
   ```bash
   adb shell am start -n com.xenos.wsa/.MainActivity
   ```

#### Via File Transfer

1. Copy APK to device storage
2. Open file manager on device
3. Navigate to APK file
4. Tap to install
5. Grant permissions
6. Launch app

### Part 6: Troubleshooting Build Issues

#### Issue: Gradle not found
```bash
# Solution: Use gradle wrapper
./gradlew build
```

#### Issue: Android SDK not found
```bash
# Solution: Set ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools
```

#### Issue: Out of memory during build
```bash
# Solution: Increase heap size
export GRADLE_OPTS="-Xmx4096m"
./gradlew build
```

#### Issue: Compilation errors
```bash
# Solution: Clean and rebuild
./gradlew clean
./gradlew build
```

### Part 7: Optimization

#### Reduce APK Size
```bash
# Enable minification
# In app/build.gradle.kts:
# isMinifyEnabled = true
```

#### Faster Build Times
```bash
# Enable parallel builds
./gradlew build --parallel

# Use build cache
./gradlew build --build-cache
```

### Part 8: Distribution

#### Upload to Play Store
1. Create Google Play Developer account
2. Create new app
3. Upload signed APK
4. Fill app details
5. Submit for review

#### Direct Distribution
1. Host APK on website
2. Share download link
3. Users install via file manager

### Part 9: Continuous Integration

#### GitHub Actions Example
```yaml
name: Build APK

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: chmod +x gradlew
      - run: ./gradlew assembleRelease
      - uses: actions/upload-artifact@v2
        with:
          name: app-release.apk
          path: app/build/outputs/apk/release/app-release.apk
```

### Part 10: Post-Build Verification

#### Test APK
```bash
# Install and test
adb install app-release.apk
adb shell am start -n com.xenos.wsa/.MainActivity

# Check logs
adb logcat | grep xenos
```

#### Verify Permissions
```bash
adb shell dumpsys package com.xenos.wsa | grep permissions
```

#### Check App Size
```bash
adb shell du -sh /data/app/com.xenos.wsa*
```

## Build Checklist

- [ ] JDK 17 installed
- [ ] Android SDK API 34 installed
- [ ] Project cloned/downloaded
- [ ] Gradle wrapper executable
- [ ] Dependencies resolved
- [ ] Code compiles without errors
- [ ] Debug APK builds successfully
- [ ] Debug APK installs on device
- [ ] App launches without crashes
- [ ] All features work
- [ ] Release APK builds
- [ ] APK signed with keystore
- [ ] APK aligned
- [ ] Release APK installs
- [ ] Performance acceptable

## Quick Build Commands

```bash
# One-line build and install
./gradlew clean assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk

# Build release with signing
./gradlew assembleRelease

# Run tests
./gradlew connectedAndroidTest

# Check dependencies
./gradlew dependencies

# Generate reports
./gradlew build --info
```

## Support

For build issues:
1. Check Android Studio logs
2. Review Gradle output
3. Verify SDK installation
4. Check Java version
5. Clear Gradle cache: `./gradlew clean`

---

**Last Updated**: June 2026
