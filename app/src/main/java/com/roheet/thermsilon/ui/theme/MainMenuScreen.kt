package com.roheet.thermsilon.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.roheet.thermsilon.ui.components.TDSStatusWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF0D1B2A))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** ---------- HEADER ---------- **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO open drawer */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
                Text(
                    text = "AE HELLO",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            /** ---------- LIVE TDS WIDGET ---------- **/
            TDSStatusWidget(Modifier.padding(vertical = 8.dp))

            /** ---------- WATER MODE CARDS ---------- **/
            val waterModes = listOf(
                Triple("CHILL\n(5-10°C)",  "❄️", "Chill"),
                Triple("COLD\n(15-20°C)",  "💧", "Cold"),
                Triple("NORMAL\n(ambient)",  "≈",  "Normal"),
                Triple("LUKEWARM\n(40°C)",   "♨️", "Lukewarm"),
                Triple("DELICATE BREW",       "☕", "DelicateBrew"),
                Triple("INSTANT NOODLES",      "🍜", "InstantNoodles")
            )

            for (i in waterModes.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val (label1, icon1, route1) = waterModes[i]
                    TempModeCard(
                        label = label1,
                        icon = icon1,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate("dispense/$route1") }
                    )

                    if (i + 1 < waterModes.size) {
                        val (label2, icon2, route2) = waterModes[i + 1]
                        TempModeCard(
                            label = label2,
                            icon = icon2,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate("dispense/$route2") }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempModeCard(
    label: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF102542)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(icon, fontSize = 36.sp, color = Color.White)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
