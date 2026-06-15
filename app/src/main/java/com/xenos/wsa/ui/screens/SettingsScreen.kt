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

@Composable
fun SettingsScreen(
    navController: NavHostController,
    emulatorManager: EmulatorManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Emulator Configuration", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text("CPU Cores: 4", fontSize = 12.sp, color = Color(0xFFB0B0B0), modifier = Modifier.padding(top = 8.dp))
                Text("RAM: 2048 MB", fontSize = 12.sp, color = Color(0xFFB0B0B0))
                Text("Graphics: OpenGL", fontSize = 12.sp, color = Color(0xFFB0B0B0))
            }
        }
    }
}
