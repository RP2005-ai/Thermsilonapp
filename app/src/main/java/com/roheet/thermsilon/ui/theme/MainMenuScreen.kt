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
import com.roheet.thermsilon.ui.components.TDSStatusWidget
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        containerColor = Color.Black // ✅ Full black background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF0D1B2A)) // ✅ Black to dark blue gradient
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🔝 Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Add Drawer or Menu */ }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
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

            // 🔵 Live TDS Status Widget
            TDSStatusWidget(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            // 💧 Water Mode Cards
            Column(modifier = Modifier.fillMaxWidth()) {
                val waterModes = listOf(
                    "CHILL\n(5-10°C)" to "❄️",
                    "COLD\n(15-20°C)" to "💧",
                    "NORMAL\n(ambient)" to "≈",
                    "LUKEWARM\n(40°C)" to "♨️",
                    "DELICATE BREW" to "☕",
                    "INSTANT NOODLES" to "🍜"
                )

                for (i in waterModes.indices step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TempModeCard(
                            label = waterModes[i].first,
                            icon = waterModes[i].second,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate("water_modes") }
                        )
                        if (i + 1 < waterModes.size) {
                            TempModeCard(
                                label = waterModes[i + 1].first,
                                icon = waterModes[i + 1].second,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate("water_modes") }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempModeCard(label: String, icon: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF102542) // ✅ Bluish textured card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = icon, fontSize = 36.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
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
