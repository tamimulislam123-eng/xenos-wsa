package com.xenos.wsa.emulator

import android.content.Context
import com.xenos.wsa.models.*
import kotlinx.coroutines.*

/**
 * Unified Emulator Manager
 * Manages both lightweight and QEMU emulation modes
 * Provides unified interface for application execution
 */
class EmulatorManager(
    private val context: Context,
    private val config: EmulatorConfig
) {
    private var lightweightEmulator: LightweightEmulator? = null
    private var qemuEmulator: QemuEmulator? = null
    private var currentMode = config.mode
    private var isInitialized = false
    
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    
    /**
     * Initialize emulator based on selected mode
     */
    fun initialize(): Boolean {
        return try {
            when (currentMode) {
                EmulationMode.LIGHTWEIGHT -> {
                    lightweightEmulator = LightweightEmulator(config)
                }
                EmulationMode.QEMU -> {
                    qemuEmulator = QemuEmulator(context, config)
                }
            }
            isInitialized = true
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Switch emulation mode
     */
    fun switchMode(newMode: EmulationMode): Boolean {
        return try {
            stop()
            currentMode = newMode
            initialize()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Load and execute an executable
     */
    fun loadExecutable(executable: ExecutableInfo): Boolean {
        if (!isInitialized) {
            initialize()
        }
        
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.loadExecutable(executable) ?: false
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.loadExecutable(executable) ?: false
            }
        }
    }
    
    /**
     * Start emulation loop (for lightweight mode)
     */
    fun startEmulationLoop() {
        if (currentMode != EmulationMode.LIGHTWEIGHT) return
        
        scope.launch {
            while (isActive && lightweightEmulator?.getState()?.isRunning == true) {
                lightweightEmulator?.step()
                delay(1) // Small delay to prevent busy-waiting
            }
        }
    }
    
    /**
     * Get running processes
     */
    fun getProcesses(): List<ProcessInfo> {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.getState()?.processes ?: emptyList()
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.getProcesses() ?: emptyList()
            }
        }
    }
    
    /**
     * Terminate a process
     */
    fun terminateProcess(pid: Int): Boolean {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.terminateProcess(pid) ?: false
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.terminateProcess(pid) ?: false
            }
        }
    }
    
    /**
     * Pause emulation
     */
    fun pause(): Boolean {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.pause()
                true
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.pause() ?: false
            }
        }
    }
    
    /**
     * Resume emulation
     */
    fun resume(): Boolean {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.resume()
                true
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.resume() ?: false
            }
        }
    }
    
    /**
     * Stop emulation
     */
    fun stop(): Boolean {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.stop()
                true
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.stop() ?: false
            }
        }
    }
    
    /**
     * Get current emulator state
     */
    fun getState(): EmulatorState {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.getState() ?: EmulatorState()
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.getState() ?: EmulatorState()
            }
        }
    }
    
    /**
     * Load DLL (lightweight mode)
     */
    fun loadDll(dllName: String, path: String): Boolean {
        return if (currentMode == EmulationMode.LIGHTWEIGHT) {
            lightweightEmulator?.loadDll(dllName, path) ?: false
        } else {
            true // QEMU handles DLLs automatically
        }
    }
    
    /**
     * Get registry value
     */
    fun getRegistryValue(path: String): String? {
        return if (currentMode == EmulationMode.LIGHTWEIGHT) {
            lightweightEmulator?.getRegistryValue(path)
        } else {
            null // QEMU has its own registry
        }
    }
    
    /**
     * Set registry value
     */
    fun setRegistryValue(path: String, key: String, value: String) {
        if (currentMode == EmulationMode.LIGHTWEIGHT) {
            lightweightEmulator?.setRegistryValue(path, key, value)
        }
    }
    
    /**
     * Cleanup resources
     */
    fun cleanup() {
        scope.cancel()
        stop()
        lightweightEmulator = null
        qemuEmulator = null
        isInitialized = false
    }
    
    /**
     * Get current mode
     */
    fun getCurrentMode(): EmulationMode = currentMode
    
    /**
     * Check if emulator is running
     */
    fun isRunning(): Boolean {
        return when (currentMode) {
            EmulationMode.LIGHTWEIGHT -> {
                lightweightEmulator?.getState()?.isRunning ?: false
            }
            EmulationMode.QEMU -> {
                qemuEmulator?.isRunning() ?: false
            }
        }
    }
}
