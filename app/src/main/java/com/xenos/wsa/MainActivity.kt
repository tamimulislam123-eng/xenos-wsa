package com.xenos.wsa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xenos.wsa.emulator.EmulatorManager
import com.xenos.wsa.filesystem.FileManager
import com.xenos.wsa.models.*
import com.xenos.wsa.ui.screens.*

/**
 * Main Activity for Xenos WSA
 * Windows Subsystem for Android
 */
class MainActivity : ComponentActivity() {
    private lateinit var emulatorManager: EmulatorManager
    private lateinit var fileManager: FileManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize managers
        val config = EmulatorConfig(
            mode = EmulationMode.LIGHTWEIGHT,
            graphicsMode = GraphicsMode.OPENGL,
            performanceProfile = PerformanceProfile.BALANCED,
            cpuArchitecture = CpuArchitecture.X86_64,
            windowsVersion = WindowsVersion.WINDOWS_10,
            cpuCores = 4,
            ramMb = 2048
        )
        
        emulatorManager = EmulatorManager(this, config)
        fileManager = FileManager(this)
        
        setContent {
            XenosWSAApp(emulatorManager, fileManager)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        emulatorManager.cleanup()
    }
}

@Composable
fun XenosWSAApp(
    emulatorManager: EmulatorManager,
    fileManager: FileManager
) {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf("home") }
    
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF1F88E5),
            secondary = Color(0xFF03DAC6),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top App Bar
                TopAppBar(
                    title = { Text("Xenos WSA - Windows Subsystem for Android") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0D47A1)
                    )
                )
                
                // Navigation
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.weight(1f)
                ) {
                    composable("home") {
                        HomeScreen(navController, emulatorManager, fileManager)
                    }
                    composable("launcher") {
                        AppLauncherScreen(navController, emulatorManager, fileManager)
                    }
                    composable("filemanager") {
                        FileManagerScreen(navController, fileManager)
                    }
                    composable("settings") {
                        SettingsScreen(navController, emulatorManager)
                    }
                    composable("monitor") {
                        PerformanceMonitorScreen(navController, emulatorManager)
                    }
                }
                
                // Bottom Navigation
                BottomNavigation(
                    navController = navController,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BottomNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "home" to "Home",
        "launcher" to "Apps",
        "filemanager" to "Files",
        "settings" to "Settings",
        "monitor" to "Monitor"
    )
    
    Row(
        modifier = modifier
            .background(Color(0xFF1E1E1E))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (route, label) ->
            Button(
                onClick = { navController.navigate(route) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F88E5)
                )
            ) {
                Text(label, fontSize = 12.sp)
            }
        }
    }
}
