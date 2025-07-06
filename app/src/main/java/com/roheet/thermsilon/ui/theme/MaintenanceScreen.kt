package com.roheet.thermsilon.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

@Composable
fun MaintenanceScreen(navController: NavHostController) {
    val lastFilterDate = remember { LocalDate.now().minusDays(15) }
    val nextFilterDate = remember { lastFilterDate.plusMonths(6) }
    val remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), nextFilterDate).toInt()
    val daysSinceLastChange = ChronoUnit.DAYS.between(lastFilterDate, LocalDate.now()).toInt()
    val isWarning = remainingDays <= 7
    val usedPercentage = (daysSinceLastChange / 180f).coerceIn(0f, 1f)

    var isFlushing by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(30) }

    LaunchedEffect(isFlushing) {
        if (isFlushing) {
            for (i in 30 downTo 1) {
                remainingTime = i
                delay(1000L)
            }
            isFlushing = false
            remainingTime = 30
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        containerColor = Color.Black
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Black, Color(0xFF0D1B2A))
                    )
                )
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 🔧 Filter Status Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFCCE6F4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular usage indicator
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(end = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val stroke = 6.dp.toPx()
                            val diameter = size.minDimension - stroke
                            val topLeft = Offset(stroke / 2, stroke / 2)
                            val sizeArc = Size(diameter, diameter)

                            drawArc(
                                color = Color.Red,
                                startAngle = -90f,
                                sweepAngle = usedPercentage * 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round),
                                size = sizeArc,
                                topLeft = topLeft
                            )

                            drawArc(
                                color = Color.Green,
                                startAngle = -90f + usedPercentage * 360f,
                                sweepAngle = (1f - usedPercentage) * 360f,
                                useCenter = false,
                                style = Stroke(width = stroke, cap = StrokeCap.Round),
                                size = sizeArc,
                                topLeft = topLeft
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "Next Change Due:",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "In $remainingDays days",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWarning) Color.Red else Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Last Filter Change:",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "$daysSinceLastChange days ago",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🚿 Auto-Clean Status Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD6F0C0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(end = 16.dp)
                            .clickable(enabled = !isFlushing) {
                                isFlushing = true
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = "Auto Clean Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                        )
                    }

                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "Auto-Clean Status:",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = if (isFlushing) "Flushing in progress..." else "Tap to start Auto-Clean",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFlushing) Color.Red else Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isFlushing)
                                "Time remaining: $remainingTime sec"
                            else
                                "Last cleaned: 15 hours ago",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🧾 Book Service + Insurance Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ServiceIconCard(
                    label = "Book Service",
                    icon = Icons.Default.Build,
                    backgroundColor = Color(0xFFFFECB3),
                    modifier = Modifier.weight(1f)
                )

                ServiceIconCard(
                    label = "Insurances",
                    icon = Icons.Default.Security,
                    backgroundColor = Color(0xFFD1C4E9),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ServiceIconCard(
    label: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Black,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}
