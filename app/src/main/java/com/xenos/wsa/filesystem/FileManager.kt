package com.xenos.wsa.filesystem

import android.content.Context
import android.os.Environment
import com.xenos.wsa.models.ExecutableInfo
import com.xenos.wsa.models.FileSystemEntry
import com.xenos.wsa.models.CpuArchitecture
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Virtual File Manager
 * Manages file system access and executable installation
 */
class FileManager(
    private val context: Context
) {
    private val installedAppsDir = File(context.filesDir, "installed_apps")
    private val virtualDrivesDir = File(context.cacheDir, "virtual_drives")
    
    init {
        ensureDirectories()
    }
    
    /**
     * Ensure required directories exist
     */
    private fun ensureDirectories() {
        installedAppsDir.mkdirs()
        virtualDrivesDir.mkdirs()
    }
    
    /**
     * Browse files in a directory
     */
    fun browseDirectory(path: String): List<FileSystemEntry> {
        return try {
            val dir = File(path)
            if (!dir.isDirectory) return emptyList()
            
            dir.listFiles()?.map { file ->
                FileSystemEntry(
                    name = file.name,
                    path = file.absolutePath,
                    isDirectory = file.isDirectory,
                    size = file.length(),
                    modifiedTime = file.lastModified(),
                    permissions = getFilePermissions(file),
                    owner = "system"
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Get file permissions string
     */
    private fun getFilePermissions(file: File): String {
        val perms = StringBuilder()
        perms.append(if (file.isDirectory) "d" else "-")
        perms.append(if (file.canRead()) "r" else "-")
        perms.append(if (file.canWrite()) "w" else "-")
        perms.append(if (file.canExecute()) "x" else "-")
        perms.append("-----")
        return perms.toString()
    }
    
    /**
     * Parse PE executable header to get architecture and entry point
     */
    fun parseExecutable(filePath: String): ExecutableInfo? {
        return try {
            val file = File(filePath)
            if (!file.exists() || !file.canRead()) return null
            
            val buffer = ByteArray(512)
            file.inputStream().use { it.read(buffer) }
            
            // Check MZ header
            if (buffer[0] != 'M'.code.toByte() || buffer[1] != 'Z'.code.toByte()) {
                return null
            }
            
            // Get PE offset
            val peOffset = ByteBuffer.wrap(buffer, 0x3C, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
            
            if (peOffset < 0 || peOffset > buffer.size - 4) return null
            
            // Read PE header
            val peBuffer = ByteArray(512)
            file.inputStream().use {
                it.skip(peOffset.toLong())
                it.read(peBuffer)
            }
            
            // Check PE signature
            if (peBuffer[0] != 'P'.code.toByte() || 
                peBuffer[1] != 'E'.code.toByte() ||
                peBuffer[2] != 0.toByte() || 
                peBuffer[3] != 0.toByte()) {
                return null
            }
            
            // Get machine type (offset 0x4)
            val machine = ByteBuffer.wrap(peBuffer, 0x4, 2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .short
                .toInt() and 0xFFFF
            
            val architecture = when (machine) {
                0x014C -> CpuArchitecture.X86
                0x8664 -> CpuArchitecture.X86_64
                else -> CpuArchitecture.X86_64
            }
            
            // Get entry point (offset 0x28 in optional header)
            val entryPoint = ByteBuffer.wrap(peBuffer, 0x28, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
                .toLong() and 0xFFFFFFFFL
            
            // Get image base (offset 0x34 in optional header)
            val imageBase = ByteBuffer.wrap(peBuffer, 0x34, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
                .toLong() and 0xFFFFFFFFL
            
            ExecutableInfo(
                name = file.name,
                path = file.absolutePath,
                size = file.length(),
                architecture = architecture,
                subsystem = "Windows",
                entryPoint = entryPoint,
                imageBase = imageBase,
                fileTime = file.lastModified()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Install executable to virtual drive
     */
    fun installExecutable(sourcePath: String, appName: String): Boolean {
        return try {
            val sourceFile = File(sourcePath)
            if (!sourceFile.exists()) return false
            
            val destDir = File(installedAppsDir, appName)
            destDir.mkdirs()
            
            val destFile = File(destDir, sourceFile.name)
            sourceFile.copyTo(destFile, overwrite = true)
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get installed applications
     */
    fun getInstalledApps(): List<ExecutableInfo> {
        return try {
            installedAppsDir.listFiles()?.flatMap { appDir ->
                appDir.listFiles()?.mapNotNull { file ->
                    if (file.extension == "exe") {
                        parseExecutable(file.absolutePath)
                    } else {
                        null
                    }
                } ?: emptyList()
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Uninstall application
     */
    fun uninstallApp(appName: String): Boolean {
        return try {
            val appDir = File(installedAppsDir, appName)
            appDir.deleteRecursively()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get storage space info
     */
    fun getStorageInfo(): Pair<Long, Long> {
        return try {
            val stat = android.os.StatFs(context.filesDir.absolutePath)
            val totalSpace = stat.totalBytes
            val freeSpace = stat.availableBytes
            Pair(totalSpace, freeSpace)
        } catch (e: Exception) {
            Pair(0L, 0L)
        }
    }
    
    /**
     * Create virtual drive
     */
    fun createVirtualDrive(driveLetter: Char, sizeInMb: Int): Boolean {
        return try {
            val driveDir = File(virtualDrivesDir, "$driveLetter:")
            driveDir.mkdirs()
            
            // Create drive info file
            val infoFile = File(driveDir, ".driveinfo")
            infoFile.writeText("drive=$driveLetter\nsize=$sizeInMb\nformat=NTFS")
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get virtual drives
     */
    fun getVirtualDrives(): List<FileSystemEntry> {
        return try {
            virtualDrivesDir.listFiles()?.map { driveDir ->
                FileSystemEntry(
                    name = driveDir.name,
                    path = driveDir.absolutePath,
                    isDirectory = true,
                    size = driveDir.totalSpace,
                    modifiedTime = driveDir.lastModified()
                )
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
