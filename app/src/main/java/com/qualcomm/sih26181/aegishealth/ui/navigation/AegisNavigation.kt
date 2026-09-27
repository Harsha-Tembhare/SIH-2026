package com.qualcomm.sih26181.aegishealth.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.qualcomm.sih26181.aegishealth.data.repository.DiscoveredBleDevice
import com.qualcomm.sih26181.aegishealth.data.repository.SimulationScenario
import com.qualcomm.sih26181.aegishealth.domain.model.*
import com.qualcomm.sih26181.aegishealth.ui.screens.*
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun AegisNavigation(
    navController: NavHostController,
    vitals: VitalsData,
    baseline: PersonalBaseline,
    assessment: AiRiskAssessment,
    alerts: List<HealthAlert>,
    waveformPoints: List<Float>,
    currentScenario: SimulationScenario,
    emergencyPayload: EmergencyPayload?,
    privacySettings: PrivacySettings,
    pendingQueueCount: Int,
    isHardwareConnected: Boolean,
    connectedHardwareName: String?,
    rawPayloadLog: String,
    isScanning: Boolean,
    scannedDevices: List<DiscoveredBleDevice>,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onConnectDevice: (DiscoveredBleDevice) -> Unit,
    onDisconnectDevice: () -> Unit,
    onSelectScenario: (SimulationScenario) -> Unit,
    onTriggerSos: () -> Unit,
    onGenerateSmsPayload: () -> Unit,
    onFlushQueue: () -> Unit,
    onUpdatePrivacy: (PrivacySettings) -> Unit,
    onPurgeVault: () -> Unit,
    onExportLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                vitals = vitals,
                baseline = baseline,
                assessment = assessment,
                alerts = alerts,
                onTriggerSos = onTriggerSos
            )
        }
        composable(Screen.LiveMonitor.route) {
            LiveMonitorScreen(
                vitals = vitals,
                waveformPoints = waveformPoints
            )
        }
        composable(Screen.AiRisk.route) {
            AiRiskScreen(
                assessment = assessment,
                baseline = baseline
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onExportLogs = onExportLogs
            )
        }
        composable(Screen.Summary.route) {
            SummaryScreen(
                baseline = baseline
            )
        }
        composable(Screen.Alerts.route) {
            AlertsScreen(
                alerts = alerts
            )
        }
        composable(Screen.DisasterMode.route) {
            DisasterModeScreen(
                vitals = vitals,
                assessment = assessment,
                emergencyPayload = emergencyPayload,
                onGenerateSmsPayload = onGenerateSmsPayload,
                pendingQueueCount = pendingQueueCount,
                onFlushQueue = onFlushQueue
            )
        }
        composable(Screen.Privacy.route) {
            PrivacyScreen(
                privacySettings = privacySettings,
                onUpdatePrivacy = onUpdatePrivacy,
                onPurgeVault = onPurgeVault
            )
        }
        composable(Screen.Device.route) {
            DeviceScreen(
                currentScenario = currentScenario,
                onSelectScenario = onSelectScenario,
                isHardwareConnected = isHardwareConnected,
                connectedHardwareName = connectedHardwareName,
                rawPayloadLog = rawPayloadLog,
                isScanning = isScanning,
                scannedDevices = scannedDevices,
                onStartScan = onStartScan,
                onStopScan = onStopScan,
                onConnectDevice = onConnectDevice,
                onDisconnectDevice = onDisconnectDevice
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

@Composable
fun AegisBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = AegisSurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp
    ) {
        Screen.bottomBarScreens.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title, fontSize = 10.sp) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AegisCyan,
                    selectedTextColor = AegisCyan,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = AegisSurfaceVariant
                )
            )
        }
    }
}
