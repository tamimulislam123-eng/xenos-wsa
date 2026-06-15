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
import com.xenos.wsa.filesystem.FileManager

@Composable
fun FileManagerScreen(
    navController: NavHostController,
    fileManager: FileManager
) {
    var currentPath by remember { mutableStateOf("/") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text("File Manager", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Path: $currentPath", fontSize = 12.sp, color = Color(0xFFB0B0B0), modifier = Modifier.padding(top = 8.dp))
        
        Button(
            onClick = { /* Browse */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F88E5))
        ) {
            Text("Browse Files", fontSize = 14.sp)
        }
    }
}
