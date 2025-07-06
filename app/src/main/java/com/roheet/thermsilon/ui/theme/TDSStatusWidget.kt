package com.roheet.thermsilon.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TDSStatusWidget(
    modifier: Modifier = Modifier
) {
    var targetTDS by remember { mutableStateOf((50..150).random()) }

    val animatedTDS by animateIntAsState(targetTDS, label = "TDSValue")

    val tdsColor = if (animatedTDS > 100) Color.Red else Color.Green

    // Simulate data updates every 10 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(10_000)
            targetTDS = (50..150).random()
        }
    }

    Box(
        modifier = modifier
            .size(120.dp)
            .shadow(6.dp, shape = CircleShape)
            .background(Color.Black, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$animatedTDS ppm",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "TDS Status",
                color = tdsColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
