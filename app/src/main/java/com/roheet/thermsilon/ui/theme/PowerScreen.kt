package com.roheet.thermsilon.ui.theme

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.clickable
import kotlin.math.sin
import kotlin.math.PI

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PowerScreen(navController: NavHostController) {
    // Hardware-connected data (replace with actual hardware data)
    val currentEnergyUsage by remember { mutableFloatStateOf(2.4f) } // kWh today
    val dailyEnergyLimit by remember { mutableFloatStateOf(8.0f) } // kWh limit
    val bluetoothConnected by remember { mutableStateOf(true) }
    val batteryBackupLevel by remember { mutableIntStateOf(85) } // Battery backup percentage
    val solarBatteryLevel by remember { mutableIntStateOf(92) } // Solar battery percentage
    val solarPowerGenerating by remember { mutableStateOf(true) }

    // Power saving mode state
    var powerSavingMode by remember { mutableStateOf("Auto") } // Off, On, Auto

    // Monthly data for graph (replace with actual data from hardware)
    val monthlyData = remember {
        listOf(6.2f, 5.8f, 7.1f, 4.9f, 6.5f, 8.2f, 5.3f, 6.8f, 7.5f, 5.9f, 6.1f, 7.8f,
            4.7f, 6.3f, 5.5f, 7.2f, 6.9f, 5.1f, 6.7f, 8.0f, 5.4f, 6.6f, 7.3f, 5.8f,
            6.4f, 7.0f, 5.6f, 6.2f, 7.4f, 2.4f) // Last 30 days
    }

    val hapticFeedback = LocalHapticFeedback.current

    val usagePercentage = (currentEnergyUsage / dailyEnergyLimit * 100).coerceAtMost(100f)
    val remainingEnergy = (dailyEnergyLimit - currentEnergyUsage).coerceAtLeast(0f)

    // Animation for progress
    val animatedProgress by animateFloatAsState(
        targetValue = usagePercentage,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "progress"
    )

    // Animation for battery levels
    val animatedBatteryLevel by animateFloatAsState(
        targetValue = batteryBackupLevel.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "battery"
    )

    val animatedSolarLevel by animateFloatAsState(
        targetValue = solarBatteryLevel.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "solar"
    )

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
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "POWER MANAGEMENT",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
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

                // Today's Energy Usage Circle Card
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
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "TODAY'S ENERGY USAGE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700),
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
                                            Color(0xFFFFD700),
                                            Color(0xFFFFA500),
                                            Color(0xFFFF6B35)
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
                                Text(
                                    text = "⚡",
                                    fontSize = 36.sp
                                )
                                Text(
                                    text = "${currentEnergyUsage}kWh",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "of ${dailyEnergyLimit}kWh",
                                    fontSize = 16.sp,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Usage info
                        AnimatedVisibility(
                            visible = usagePercentage >= 90f,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Text(
                                text = "⚠️ High Energy Usage",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B35)
                            )
                        }

                        AnimatedVisibility(
                            visible = usagePercentage < 90f,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${String.format("%.1f", remainingEnergy)}kWh remaining",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Daily limit: ${dailyEnergyLimit}kWh",
                                    fontSize = 14.sp,
                                    color = Color(0xFFFFD700)
                                )
                            }
                        }
                    }
                }

                // Power Saving Mode Card
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
                            text = "🔋 POWER SAVING MODE",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Off", "On", "Auto").forEach { mode ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            powerSavingMode = mode
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (powerSavingMode == mode)
                                            Color(0xFF4CAF50).copy(alpha = 0.3f)
                                        else
                                            Color(0xFF1E3A52).copy(alpha = 0.5f)
                                    )
                                ) {
                                    Text(
                                        text = mode,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (powerSavingMode == mode) Color(0xFF4CAF50) else Color.White,
                                        modifier = Modifier.padding(12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Battery Status Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Battery Backup
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF102542).copy(alpha = 0.8f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔋 BATTERY BACKUP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(80.dp)
                            ) {
                                Canvas(
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    val strokeWidth = 8.dp.toPx()
                                    val radius = size.minDimension / 2 - strokeWidth / 2

                                    // Background circle
                                    drawCircle(
                                        color = Color(0xFF1E3A52),
                                        radius = radius,
                                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                    )

                                    // Progress arc
                                    val sweepAngle = (animatedBatteryLevel / 100f) * 360f
                                    drawArc(
                                        color = when {
                                            batteryBackupLevel > 60 -> Color(0xFF4CAF50)
                                            batteryBackupLevel > 30 -> Color(0xFFFFD700)
                                            else -> Color(0xFFFF6B35)
                                        },
                                        startAngle = -90f,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                    )
                                }

                                Text(
                                    text = "${batteryBackupLevel}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Solar Battery
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF102542).copy(alpha = 0.8f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "☀️ SOLAR BATTERY",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(80.dp)
                            ) {
                                Canvas(
                                    modifier = Modifier.size(80.dp)
                                ) {
                                    val strokeWidth = 8.dp.toPx()
                                    val radius = size.minDimension / 2 - strokeWidth / 2

                                    // Background circle
                                    drawCircle(
                                        color = Color(0xFF1E3A52),
                                        radius = radius,
                                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                    )

                                    // Progress arc
                                    val sweepAngle = (animatedSolarLevel / 100f) * 360f
                                    drawArc(
                                        brush = Brush.sweepGradient(
                                            colors = listOf(
                                                Color(0xFFFFD700),
                                                Color(0xFFFFA500)
                                            )
                                        ),
                                        startAngle = -90f,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(strokeWidth, cap = StrokeCap.Round)
                                    )
                                }

                                Text(
                                    text = "${solarBatteryLevel}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Monthly Usage Chart Card
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
                            text = "📈 MONTHLY USAGE TREND",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Simple line chart representation
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        ) {
                            val chartWidth = size.width - 32.dp.toPx()
                            val chartHeight = size.height - 32.dp.toPx()
                            val maxValue = monthlyData.maxOrNull() ?: 8.0f
                            val stepWidth = chartWidth / (monthlyData.size - 1)

                            // Draw line chart
                            for (i in 0 until monthlyData.size - 1) {
                                val x1 = 16.dp.toPx() + i * stepWidth
                                val y1 = 16.dp.toPx() + (1 - monthlyData[i] / maxValue) * chartHeight
                                val x2 = 16.dp.toPx() + (i + 1) * stepWidth
                                val y2 = 16.dp.toPx() + (1 - monthlyData[i + 1] / maxValue) * chartHeight

                                drawLine(
                                    color = Color(0xFFFFD700),
                                    start = androidx.compose.ui.geometry.Offset(x1, y1),
                                    end = androidx.compose.ui.geometry.Offset(x2, y2),
                                    strokeWidth = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }

                            // Draw current day point
                            val lastIndex = monthlyData.size - 1
                            val lastX = 16.dp.toPx() + lastIndex * stepWidth
                            val lastY = 16.dp.toPx() + (1 - monthlyData[lastIndex] / maxValue) * chartHeight

                            drawCircle(
                                color = Color(0xFFFF6B35),
                                radius = 6.dp.toPx(),
                                center = androidx.compose.ui.geometry.Offset(lastX, lastY)
                            )
                        }

                        Text(
                            text = "Monthly Average: ${String.format("%.1f", monthlyData.average())}kWh",
                            fontSize = 12.sp,
                            color = Color(0xFFFFD700),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PowerStatCard(
                        title = "Daily Limit",
                        value = "${dailyEnergyLimit}kWh",
                        icon = "🎯",
                        modifier = Modifier.weight(1f)
                    )

                    PowerStatCard(
                        title = "Usage %",
                        value = "${usagePercentage.toInt()}%",
                        icon = "📊",
                        modifier = Modifier.weight(1f)
                    )

                    PowerStatCard(
                        title = "Solar Status",
                        value = if (solarPowerGenerating) "Active" else "Inactive",
                        icon = "☀️",
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
fun PowerStatCard(
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
                color = Color(0xFFFFD700)
            )
        }
    }
}
