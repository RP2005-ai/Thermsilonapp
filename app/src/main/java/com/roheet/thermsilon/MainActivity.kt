package com.roheet.thermsilon

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roheet.thermsilon.ui.theme.MainMenuScreen
import com.roheet.thermsilon.ui.theme.MaintenanceScreen
import com.roheet.thermsilon.ui.theme.ThermsilonTheme
import com.roheet.thermsilon.ui.theme.SplashScreen // Import the new Splash Screen
import com.roheet.thermsilon.ui.theme.AuthScreen // Import the new Auth Screen
import com.roheet.thermsilon.ui.theme.WaterHealthScreen // Assuming you have this screen
import com.roheet.thermsilon.ui.theme.PowerScreen // Assuming you have this screen
import com.roheet.thermsilon.ui.theme.SettingsScreen // Assuming you have this screen
import com.roheet.thermsilon.ui.theme.BottomNavBar // Add this import
import com.jakewharton.threetenabp.AndroidThreeTen // Import for AndroidThreeTen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize date/time compatibility for API < 26
        AndroidThreeTen.init(this)

        // Request necessary permissions for Bluetooth and Location
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        requestPermissions(permissions, 1)

        setContent {
            ThermsilonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThermsilonApp()
                }
            }
        }
    }
}

@Composable
fun ThermsilonApp() {
    val navController = rememberNavController()
    // Set splash_screen as the initial destination
    NavHost(navController = navController, startDestination = "splash_screen") {
        // Splash Screen: First screen shown, navigates to auth_screen after a delay
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        // Authentication Screen: Handles phone number and OTP, navigates to main_menu upon success
        composable("auth_screen") {
            AuthScreen(navController = navController)
        }
        // Main Menu Screen: Your primary application interface
        composable("main_menu") {
            MainMenuScreen(navController = navController)
        }
        // Maintenance Screen: For filter status, auto-clean, etc.
        composable("maintain") {
            MaintenanceScreen(navController = navController)
        }
        // Water Health Screen: (Assuming this exists based on your previous code)
        composable("water_health") {
            WaterHealthScreen(navController)
        }
        // Power Screen: (Assuming this exists based on your previous code)
        composable("power") {
            PowerScreen(navController = navController)
        }
        // Settings Screen: (Assuming this exists based on your previous code)
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        // Add other destinations here as needed
        // For example, if "water_modes" is a separate screen:
        // composable("water_modes") { WaterModesScreen(navController = navController) }
    }
}
