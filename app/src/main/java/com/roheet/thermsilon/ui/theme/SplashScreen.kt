package com.roheet.thermsilon.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.roheet.thermsilon.R // Make sure this R is correctly imported for your project's resources
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // LaunchedEffect runs a coroutine when the composable enters the composition
    // and cancels it when it leaves. It's perfect for side effects like delays.
    LaunchedEffect(key1 = true) {
        // Delay for 3 seconds (3000 milliseconds)
        delay(3000L)
        // Navigate to the authentication screen, popping the splash screen from the back stack
        navController.navigate("auth_screen") {
            // This ensures that when the user presses back from the auth screen,
            // they don't go back to the splash screen.
            popUpTo("splash_screen") {
                inclusive = true
            }
        }
    }

    // UI for the Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // White background for the logo
        contentAlignment = Alignment.Center
    ) {
        // Display your logo.
        // Make sure you've placed your logo file (e.g., 'app_logo.jpg') in your
        // Android project's 'res/drawable' folder.
        Image(
            painter = painterResource(id = R.drawable.app_logo), // CORRECTED: Assumes 'app_logo.jpg' is in res/drawable
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp) // Adjust size as needed
        )
    }
}