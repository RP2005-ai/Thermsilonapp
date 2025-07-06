package com.roheet.thermsilon.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment  // ✅ Fix for Alignment.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedTDSDial(currentTDS: Int, modifier: Modifier) {
    val animatedTDS by animateIntAsState(
        targetValue = currentTDS,
        animationSpec = tween(durationMillis = 1000),
        label = "TDS Animation"
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center // ✅ This now works
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = animatedTDS / 500f * 360f
            drawArc(
                color = Color(0xFF3F51B5),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 18f, cap = StrokeCap.Round),
                size = Size(size.width, size.height),
                topLeft = Offset.Zero
            )
        }
        Text(
            text = "$animatedTDS ppm",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp
        )
    }
}
