package com.roheet.thermsilon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.roheet.thermsilon.ui.theme.BottomNavBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispenseScreen(
    mode: String,
    navController: NavController
) {
    var isPreparing by remember { mutableStateOf(false) }
    var isReady by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun startDispense(amount: Int) {
        isPreparing = true
        isReady = false
        println("Dispensing $amount ml of $mode water")
        coroutineScope.launch {
            delay(5000L) // simulate 5‑second preparation
            isPreparing = false
            isReady = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Volume") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController = navController) },
        containerColor = Color.Black,
        contentColor = Color.White
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF0D1B2A))
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Mode: $mode", style = MaterialTheme.typography.titleMedium)
            Text("How much water do you need?", style = MaterialTheme.typography.headlineSmall)

            QuantityButton("50 ml") { startDispense(50) }
            QuantityButton("100 ml") { startDispense(100) }
            QuantityButton("200 ml") { startDispense(200) }

            if (isPreparing) {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp) // ⬆ bigger circle
                        .clip(CircleShape)
                        .background(Color.Green.copy(alpha = 0.8f)), // 🌟 bright green circle
                    contentAlignment = Alignment.Center
                ) {
                    Text("Preparing…", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            if (isReady) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("✅ Water is ready to be poured", color = Color.Green, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun QuantityButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}