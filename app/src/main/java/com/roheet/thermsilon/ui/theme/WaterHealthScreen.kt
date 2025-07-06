package com.roheet.thermsilon.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.sin
import kotlin.math.PI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterHealthScreen(navController: NavHostController) {
    // Hardware-connected data (replace with actual Bluetooth data)
    val currentIntake by remember { mutableIntStateOf(1250) } // From hardware
    val dailyGoal by remember { mutableIntStateOf(2000) }
    val bluetoothConnected by remember { mutableStateOf(true) }
    var showCelebration by remember { mutableStateOf(false) }

    // Weekly data for graph (replace with actual data from hardware)
    val weeklyData = remember {
        listOf(1800, 2200, 1900, 2100, 1600, 2300, 1250) // Last 7 days
    }
    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val hapticFeedback = LocalHapticFeedback.current

    val progressPercentage = (currentIntake.toFloat() / dailyGoal.toFloat() * 100).coerceAtMost(100f)
    val remainingWater = (dailyGoal - currentIntake).coerceAtLeast(0)

    // Animation for progress
    val animatedProgress by animateFloatAsState(
        targetValue = progressPercentage,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progress"
    )

    // Check if goal achieved
    LaunchedEffect(currentIntake, dailyGoal) {
        if (currentIntake >= dailyGoal && !showCelebration) {
            showCelebration = true
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color(0xFF0D1B2A),
                            Color(0xFF102542)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center, // Center the items in the Row
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            navController.popBackStack()
                        }
                    ) {
                        // Back Button
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "HYDRATION TRACKER",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f), // Allow the text to take available space
                        textAlign = TextAlign.Center
                    )
                    // Add a Spacer to balance the IconButton on the left, effectively centering the Text
                    Spacer(modifier = Modifier.width(48.dp)) // Approximate width of IconButton
                }

                // Connection Status
                if (!bluetoothConnected) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF8B0000).copy(alpha = 0.9f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Hardware not connected",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Today's Progress Circle Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF102542).copy(alpha = 0.8f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth() // Ensure the Column takes full width for centering
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // Center vertically as well
                    ) {
                        Text(
                            text = "TODAY'S PROGRESS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF87CEEB),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(220.dp)
                        ) {
                            // Progress Circle
                            Canvas(
                                modifier = Modifier.size(220.dp)
                            ) {
                                val strokeWidth = 18.dp.toPx()
                                val radius = size.minDimension / 2 - strokeWidth / 2

                                // Background circle
                                drawCircle(
                                    color = Color(0xFF1E3A52),
                                    radius = radius,
                                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                )

                                // Progress arc
                                val sweepAngle = (animatedProgress / 100f) * 360f
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        colors = listOf(
                                            Color(0xFF4FC3F7),
                                            Color(0xFF29B6F6),
                                            Color(0xFF03A9F4)
                                        )
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            // Center content
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val celebrationScale by animateFloatAsState(
                                    targetValue = if (showCelebration) 1.2f else 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1000),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "celebration"
                                )

                                Text(
                                    text = if (showCelebration) "🎉" else "💧",
                                    fontSize = 36.sp,
                                    modifier = Modifier.scale(celebrationScale)
                                )
                                Text(
                                    text = "${currentIntake}ml",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "of ${dailyGoal}ml",
                                    fontSize = 16.sp,
                                    color = Color(0xFF87CEEB)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress info
                        AnimatedVisibility(
                            visible = progressPercentage >= 100f,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Text(
                                text = "🏆 Daily Goal Achieved!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4FC3F7)
                            )
                        }

                        AnimatedVisibility(
                            visible = progressPercentage < 100f,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${remainingWater}ml remaining",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Keep drinking to reach your goal!",
                                    fontSize = 14.sp,
                                    color = Color(0xFF87CEEB)
                                )
                            }
                        }
                    }
                }

                // Weekly Chart Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF102542).copy(alpha = 0.8f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📊 WEEKLY CONSUMPTION",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Chart
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            val chartWidth = size.width - 32.dp.toPx()
                            val chartHeight = size.height - 32.dp.toPx()
                            val maxValue = weeklyData.maxOrNull() ?: 2000
                            val barWidth = chartWidth / weeklyData.size
                            val spacing = 8.dp.toPx()

                            weeklyData.forEachIndexed { index, value ->
                                val barHeight = (value.toFloat() / maxValue) * chartHeight
                                val x = 16.dp.toPx() + index * barWidth + spacing
                                val y = size.height - 16.dp.toPx() - barHeight

                                // Draw bar
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF4FC3F7),
                                            Color(0xFF29B6F6)
                                        )
                                    ),
                                    topLeft = androidx.compose.ui.geometry.Offset(x, y),
                                    size = androidx.compose.ui.geometry.Size(
                                        barWidth - spacing * 2,
                                        barHeight
                                    )
                                )

                                // Draw day labels
                                drawContext.canvas.nativeCanvas.apply {
                                    val paint = android.graphics.Paint().apply {
                                        color = Color(0xFF87CEEB).toArgb()
                                        textSize = 12.sp.toPx()
                                        textAlign = android.graphics.Paint.Align.CENTER
                                    }
                                    drawText(
                                        weekDays[index],
                                        x + (barWidth - spacing * 2) / 2,
                                        size.height - 4.dp.toPx(),
                                        paint
                                    )
                                }
                            }
                        }
                    }
                }

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Daily Goal",
                        value = "${dailyGoal}ml",
                        icon = "🎯",
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Progress",
                        value = "${progressPercentage.toInt()}%",
                        icon = "📈",
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Weekly Avg",
                        value = "${weeklyData.average().toInt()}ml",
                        icon = "📊",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Sync Status
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (bluetoothConnected)
                            Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else
                            Color(0xFF8B0000).copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (bluetoothConnected) Icons.Default.Sync else Icons.Default.SyncDisabled,
                            contentDescription = "Sync Status",
                            tint = if (bluetoothConnected) Color(0xFF4CAF50) else Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (bluetoothConnected) "Synced with hardware" else "Sync unavailable",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102542).copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF87CEEB)
            )
        }
    }
}
