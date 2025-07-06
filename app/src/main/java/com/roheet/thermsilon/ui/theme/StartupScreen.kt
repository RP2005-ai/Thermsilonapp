package com.roheet.thermsilon.ui.theme

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun StartupScreen(navController: NavHostController, bluetoothHelper: BluetoothManagerHelper) {
    val context = LocalContext.current
    val isSupported = remember { bluetoothHelper.isBluetoothSupported() }
    val isEnabled = remember { bluetoothHelper.isBluetoothEnabled() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔌 Thermsilon Bluetooth Setup", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Bluetooth Supported: $isSupported")
        Text("Bluetooth Enabled: $isEnabled")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (isSupported && isEnabled) {
                navController.navigate("main_menu")
            } else {
                Toast.makeText(context, "Please enable Bluetooth first", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Connect & Continue")
        }
    }
}
