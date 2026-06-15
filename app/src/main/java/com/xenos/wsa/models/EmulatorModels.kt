package com.xenos.wsa.models

import java.io.Serializable

/**
 * Xenos WSA - Windows Subsystem for Android
 * Core data models for emulation system
 */

// Emulation Mode
enum class EmulationMode {
    LIGHTWEIGHT,  // Custom lightweight emulator
    QEMU          // Full QEMU emulator
}

// Graphics Rendering Mode
enum class GraphicsMode {
    OPENGL,           // OpenGL rendering
    VULKAN,           // Vulkan rendering
    SOFTWARE          // Software rendering fallback
}

// Performance Profile
enum class PerformanceProfile {
    POWER_SAVING,     // Minimum resources, slowest
    BALANCED,         // Medium resources and performance
    PERFORMANCE       // Maximum resources, fastest
}

// CPU Architecture
enum class CpuArchitecture {
    X86,              // 32-bit x86
    X86_64            // 64-bit x86-64
}

// Windows Version
enum class WindowsVersion {
    WINDOWS_XP,
    WINDOWS_7,
    WINDOWS_10,
    WINDOWS_11
}

// Executable File Info
data class ExecutableInfo(
    val name: String,
    val path: String,
    val size: Long,
    val architecture: CpuArchitecture,
    val subsystem: String,
    val entryPoint: Long,
    val imageBase: Long,
    val fileTime: Long
) : Serializable

// Virtual Drive Configuration
data class VirtualDrive(
    val driveLetter: Char,
    val mountPath: String,
    val capacity: Long,
    val fileSystem: String = "NTFS",
    val isReadOnly: Boolean = false,
    val isAccessible: Boolean = true
) : Serializable

// Emulator Configuration
data class EmulatorConfig(
    val mode: EmulationMode = EmulationMode.LIGHTWEIGHT,
    val graphicsMode: GraphicsMode = GraphicsMode.OPENGL,
    val performanceProfile: PerformanceProfile = PerformanceProfile.BALANCED,
    val cpuArchitecture: CpuArchitecture = CpuArchitecture.X86_64,
    val windowsVersion: WindowsVersion = WindowsVersion.WINDOWS_10,
    val cpuCores: Int = 4,
    val ramMb: Int = 2048,
    val virtualDrives: List<VirtualDrive> = emptyList(),
    val enableSound: Boolean = true,
    val enableNetworking: Boolean = true,
    val enableClipboard: Boolean = true,
    val enableGpu: Boolean = true
) : Serializable

// Running Process Info
data class ProcessInfo(
    val pid: Int,
    val name: String,
    val path: String,
    val memoryMb: Int,
    val cpuUsage: Float,
    val state: String = "Running"
) : Serializable

// System Metrics
data class SystemMetrics(
    val cpuUsage: Float,
    val memoryUsage: Float,
    val diskUsage: Float,
    val fps: Int,
    val temperature: Int,
    val batteryLevel: Int,
    val networkLatency: Int
) : Serializable

// Installation Package
data class InstallationPackage(
    val id: String,
    val name: String,
    val version: String,
    val publisher: String,
    val installPath: String,
    val size: Long,
    val installTime: Long,
    val isInstalled: Boolean = false,
    val dependencies: List<String> = emptyList()
) : Serializable

// File System Entry
data class FileSystemEntry(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val modifiedTime: Long,
    val permissions: String = "rw-r--r--",
    val owner: String = "system"
) : Serializable

// Registry Entry (Windows Registry simulation)
data class RegistryEntry(
    val path: String,
    val key: String,
    val value: String,
    val type: String = "REG_SZ"
) : Serializable

// DLL Module Info
data class DllModule(
    val name: String,
    val path: String,
    val baseAddress: Long,
    val size: Int,
    val version: String
) : Serializable

// CPU State (for lightweight emulator)
data class CpuState(
    val eax: Long = 0,
    val ebx: Long = 0,
    val ecx: Long = 0,
    val edx: Long = 0,
    val esi: Long = 0,
    val edi: Long = 0,
    val ebp: Long = 0,
    val esp: Long = 0,
    val eip: Long = 0,
    val eflags: Long = 0,
    val cs: Int = 0,
    val ds: Int = 0,
    val es: Int = 0,
    val fs: Int = 0,
    val gs: Int = 0,
    val ss: Int = 0
) : Serializable

// Memory Page
data class MemoryPage(
    val address: Long,
    val size: Int,
    val permissions: String,
    val data: ByteArray
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MemoryPage) return false
        if (address != other.address) return false
        if (size != other.size) return false
        if (permissions != other.permissions) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + size
        result = 31 * result + permissions.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}

// Emulator State
data class EmulatorState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val currentExecutable: ExecutableInfo? = null,
    val cpuState: CpuState = CpuState(),
    val processes: List<ProcessInfo> = emptyList(),
    val metrics: SystemMetrics? = null,
    val loadedDlls: List<DllModule> = emptyList()
) : Serializable
