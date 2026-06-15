-keep class com.xenos.wsa.** { *; }
-keep class com.xenos.wsa.models.** { *; }
-keep class com.xenos.wsa.emulator.** { *; }
-keep class com.xenos.wsa.filesystem.** { *; }
-keepclassmembers class * {
    public <init>(...);
}
