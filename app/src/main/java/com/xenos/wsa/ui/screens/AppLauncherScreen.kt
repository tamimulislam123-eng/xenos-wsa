package com.xenos.wsa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.xenos.wsa.emulator.EmulatorManager
import com.xenos.wsa.filesystem.FileManager

@Composable
fun AppLauncherScreen(
    navController: NavHostController,
    emulatorManager: EmulatorManager,
    fileManager: FileManager
) {
    val installedApps by remember { mutableStateOf(fileManager.getInstalledApps()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text(
            "Application Launcher",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Button(
            onClick = { /* Open file picker */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F88E5))
        ) {
            Text("Install New Application", fontSize = 14.sp)
        }
        
        Text(
            "Installed Applications (${installedApps.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (installedApps.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Text(
                        "No applications installed",
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFB0B0B0)
                    )
                }
            } else {
                installedApps.forEach { app ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(app.name, color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("${app.architecture.name} - ${app.size / 1024 / 1024}MB", 
                                color = Color(0xFFB0B0B0), fontSize = 12.sp)
                            Button(
                                onClick = { emulatorManager.loadExecutable(app) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F88E5))
                            ) {
                                Text("Launch", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
