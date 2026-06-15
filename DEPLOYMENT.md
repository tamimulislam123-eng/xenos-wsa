# Deployment Guide - Xenos WSA

## Deploying to Android Devices

### Method 1: Direct APK Installation

1. **Download APK**
   - Get `app-release.apk` from build output

2. **Transfer to Device**
   ```bash
   adb push app-release.apk /sdcard/
   ```

3. **Install**
   ```bash
   adb install /sdcard/app-release.apk
   ```

4. **Verify**
   ```bash
   adb shell pm list packages | grep xenos
   ```

### Method 2: Google Play Store

1. Create developer account
2. Upload signed APK
3. Configure app listing
4. Submit for review
5. Publish

### Method 3: Alternative App Stores

- F-Droid
- APKPure
- Amazon Appstore

### Post-Deployment

1. **Monitor Performance**
   ```bash
   adb logcat
   ```

2. **Collect Crash Reports**
   - Implement Firebase Crashlytics
   - Monitor user feedback

3. **Update Strategy**
   - Plan regular updates
   - Test thoroughly before release
   - Communicate with users

## Performance Optimization

### APK Size Reduction
- Enable ProGuard/R8
- Remove unused resources
- Compress assets

### Runtime Performance
- Profile with Android Profiler
- Optimize memory usage
- Reduce CPU load

### Battery Optimization
- Implement battery saver mode
- Optimize background tasks
- Use efficient algorithms

---

**Xenos WSA Deployment Guide**
