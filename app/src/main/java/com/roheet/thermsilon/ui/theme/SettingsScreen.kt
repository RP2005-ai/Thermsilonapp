package com.roheet.thermsilon.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.roheet.thermsilon.ui.theme.BottomNavBar

/**
 * Settings screen with independent expandable sections and scrolling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) },
        bottomBar = { BottomNavBar(navController = navController) }, // <-- added bottom nav bar
        containerColor = Color.Black,
        contentColor = Color.White
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /* ——— Profile ——— */
            SettingsSection(title = "👤 Profile", expandable = true) {
                SettingsItem("Edit Profile", Icons.Default.Person)
                SettingsItem("View Water Stats", Icons.Default.BarChart)
            }

            /* ——— Privacy Policy ——— */
            SettingsSection(title = "📄 Privacy Policy", expandable = true) {
                SettingsItem("View Privacy Policy", Icons.Default.Description)
            }

            /* ——— Manual & Guidelines ——— */
            SettingsSection(title = "🧾 User Manual & Guidelines", expandable = true) {
                SettingsItem("User Manual", Icons.Default.MenuBook)
            }

            /* ——— Help & Support ——— */
            SettingsSection(title = "🆘 Help & Support", expandable = true) {
                SettingsItem("FAQs", Icons.Default.HelpOutline)
                SettingsItem("Contact Us", Icons.Default.Email)
                SettingsItem("Feedback", Icons.Default.Feedback)
                SettingsItem("Report Issue", Icons.Default.Report)
            }

            /* ——— Advanced Settings ——— */
            SettingsSection(title = "⚙️ Advanced Settings", expandable = true) {
                SettingsItem("🌐 Language", Icons.Default.Language)
                SettingsItem("🎨 Theme", Icons.Default.Palette)
                SettingsItem("🔐 App Lock", Icons.Default.Lock)
                SettingsItem("🔔 Notifications", Icons.Default.Notifications)
            }

            /* ——— About App ——— */
            SettingsSection(title = "ℹ️ About App") {
                SettingsItem("Version Info", Icons.Default.Info)
                SettingsItem("Developer Credits", Icons.Default.Code)
                SettingsItem("Terms of Service", Icons.Default.Gavel)
            }
        }
    }
}

/************************ Helper Composables ************************/

@Composable
fun SettingsSection(
    title: String,
    expandable: Boolean = false,
    expandedInit: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(expandedInit) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = expandable) { if (expandable) expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        if (!expandable || expanded) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
        }
    }
}

@Composable
fun SettingsItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF102542)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Icon(icon, contentDescription = text, tint = Color.White)
            Spacer(Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}
