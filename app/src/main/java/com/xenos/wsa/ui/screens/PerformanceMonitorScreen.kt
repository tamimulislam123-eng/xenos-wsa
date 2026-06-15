package com.xenos.wsa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun PerformanceMonitorScreen(
    navController: NavHostController,
    emulatorManager: EmulatorManager
) {
    val state by remember { mutableStateOf(emulatorManager.getState()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text("Performance Monitor", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Running Processes: ${state.processes.size}", fontSize = 14.sp, color = Color.White)
                state.processes.forEach { process ->
                    Text("${process.name} (PID: ${process.pid})", fontSize = 12.sp, color = Color(0xFFB0B0B0), modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
