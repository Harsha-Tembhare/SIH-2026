package com.qualcomm.sih26181.aegishealth.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object LiveMonitor : Screen("live_monitor", "Live", Icons.Default.MonitorHeart)
    object AiRisk : Screen("ai_risk", "AI Risk", Icons.Default.Psychology)
    object History : Screen("history", "History", Icons.Default.ShowChart)
    object Summary : Screen("summary", "Summary", Icons.Default.Assessment)
    object Alerts : Screen("alerts", "Alerts", Icons.Default.NotificationsActive)
    object DisasterMode : Screen("disaster_mode", "Disaster", Icons.Default.Sos)
    object Privacy : Screen("privacy", "Privacy", Icons.Default.Security)
    object Device : Screen("device", "Device", Icons.Default.Bluetooth)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    companion object {
        val bottomBarScreens = listOf(
            Dashboard, LiveMonitor, AiRisk, History, Summary, Alerts, DisasterMode, Privacy, Device, Settings
        )
    }
}
