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
import androidx.navigation.navArgument
import com.roheet.thermsilon.ui.theme.*
import com.roheet.thermsilon.ui.screens.DispenseScreen
import com.jakewharton.threetenabp.AndroidThreeTen
import com.roheet.thermsilon.ui.screens.SettingsScreen


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
    NavHost(navController = navController, startDestination = "splash_screen") {

        // Splash Screen: shown first
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        // Authentication Screen
        composable("auth_screen") {
            AuthScreen(navController = navController)
        }

        // Main Menu Screen
        composable("main_menu") {
            MainMenuScreen(navController = navController)
        }

        // Maintenance Screen
        composable("maintain") {
            MaintenanceScreen(navController = navController)
        }

        // Water Health Screen
        composable("water_health") {
            WaterHealthScreen(navController = navController)
        }

        // Power Screen
        composable("power") {
            PowerScreen(navController = navController)
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(navController = navController)
        }


        // Dispense Screen (shared for all water modes)
        composable(
            route = "dispense/{mode}",
            arguments = listOf(navArgument("mode") { defaultValue = "Any" })
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "Any"
            DispenseScreen(mode = mode, navController = navController)
        }
    }
}
