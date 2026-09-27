package com.qualcomm.sih26181.aegishealth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.qualcomm.sih26181.aegishealth.data.local.EncryptedStorageManager
import com.qualcomm.sih26181.aegishealth.data.repository.BleSensorSimulator
import com.qualcomm.sih26181.aegishealth.data.repository.DiscoveredBleDevice
import com.qualcomm.sih26181.aegishealth.data.repository.HardwareBleManager
import com.qualcomm.sih26181.aegishealth.data.repository.SimulationScenario
import com.qualcomm.sih26181.aegishealth.domain.ai.AdaptiveBaselineEngine
import com.qualcomm.sih26181.aegishealth.domain.ai.ExplainableAiEngine
import com.qualcomm.sih26181.aegishealth.domain.disaster.DisasterManager
import com.qualcomm.sih26181.aegishealth.domain.model.*
import com.qualcomm.sih26181.aegishealth.domain.signal.SignalProcessor
import com.qualcomm.sih26181.aegishealth.ui.navigation.AegisBottomNavigationBar
import com.qualcomm.sih26181.aegishealth.ui.navigation.AegisNavigation
import com.qualcomm.sih26181.aegishealth.ui.theme.AegisHealthTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val bleSimulator = BleSensorSimulator()
    private lateinit var hardwareBleManager: HardwareBleManager

    private val signalProcessor = SignalProcessor()
    private val baselineEngine = AdaptiveBaselineEngine()
    private val aiEngine = ExplainableAiEngine(baselineEngine)
    private val disasterManager = DisasterManager()
    private lateinit var storageManager: EncryptedStorageManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            hardwareBleManager.startScan()
        } else {
            Toast.makeText(this, "BLE permissions required to scan hardware", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storageManager = EncryptedStorageManager(this)
        hardwareBleManager = HardwareBleManager(this)

        setContent {
            AegisHealthTheme {
                val navController = rememberNavController()

                var currentVitals by remember { mutableStateOf(VitalsData()) }
                var currentBaseline by remember { mutableStateOf(baselineEngine.getBaseline()) }
                var currentAssessment by remember { mutableStateOf(AiRiskAssessment()) }
                var alertsList by remember { mutableStateOf<List<HealthAlert>>(emptyList()) }
                var waveformPoints by remember { mutableStateOf<List<Float>>(emptyList()) }
                var currentScenario by remember { mutableStateOf(SimulationScenario.NORMAL) }
                var emergencyPayload by remember { mutableStateOf<EmergencyPayload?>(null) }
                var privacySettings by remember { mutableStateOf(PrivacySettings()) }
                var pendingQueueCount by remember { mutableStateOf(0) }
                var phaseOffset by remember { mutableFloatStateOf(0f) }

                val isHardwareConnected by hardwareBleManager.isConnected.collectAsState()
                val connectedHardwareName by hardwareBleManager.connectedDeviceName.collectAsState()
                val rawPayloadLog by hardwareBleManager.rawPayloadLog.collectAsState()
                val isScanning by hardwareBleManager.isScanning.collectAsState()
                val scannedDevices by hardwareBleManager.scannedDevices.collectAsState()

                // Process telemetry from Real Hardware when connected
                LaunchedEffect(isHardwareConnected) {
                    if (isHardwareConnected) {
                        lifecycleScope.launch {
                            hardwareBleManager.hardwareVitalsFlow.collectLatest { rawHardwareData ->
                                val validated = signalProcessor.validateAndProcess(rawHardwareData)
                                currentVitals = validated

                                currentBaseline = baselineEngine.updateBaseline(validated)
                                val assessment = aiEngine.evaluateRisk(validated)
                                currentAssessment = assessment

                                phaseOffset = (phaseOffset + 0.1f) % 10f
                                waveformPoints = signalProcessor.generateWaveformPoints(
                                    count = 100,
                                    hrBpm = validated.heartRate,
                                    phaseOffset = phaseOffset
                                )

                                checkAndTriggerAlert(assessment, alertsList) { newAlerts ->
                                    alertsList = newAlerts
                                }
                            }
                        }
                    }
                }

                // Process telemetry from Simulator when real hardware is not connected
                LaunchedEffect(isHardwareConnected) {
                    if (!isHardwareConnected) {
                        lifecycleScope.launch {
                            bleSimulator.observeTelemetry().collectLatest { rawSim ->
                                val validated = signalProcessor.validateAndProcess(rawSim)
                                currentVitals = validated

                                currentBaseline = baselineEngine.updateBaseline(validated)
                                val assessment = aiEngine.evaluateRisk(validated)
                                currentAssessment = assessment

                                phaseOffset = (phaseOffset + 0.1f) % 10f
                                waveformPoints = signalProcessor.generateWaveformPoints(
                                    count = 100,
                                    hrBpm = validated.heartRate,
                                    phaseOffset = phaseOffset
                                )

                                checkAndTriggerAlert(assessment, alertsList) { newAlerts ->
                                    alertsList = newAlerts
                                }
                            }
                        }
                    }
                }

                Scaffold(
                    bottomBar = { AegisBottomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    AegisNavigation(
                        navController = navController,
                        vitals = currentVitals,
                        baseline = currentBaseline,
                        assessment = currentAssessment,
                        alerts = alertsList,
                        waveformPoints = waveformPoints,
                        currentScenario = currentScenario,
                        emergencyPayload = emergencyPayload,
                        privacySettings = privacySettings,
                        pendingQueueCount = pendingQueueCount,
                        isHardwareConnected = isHardwareConnected,
                        connectedHardwareName = connectedHardwareName,
                        rawPayloadLog = rawPayloadLog,
                        isScanning = isScanning,
                        scannedDevices = scannedDevices,
                        onStartScan = { checkPermissionsAndScan() },
                        onStopScan = { hardwareBleManager.stopScan() },
                        onConnectDevice = { device ->
                            hardwareBleManager.connectToDevice(device.device)
                            Toast.makeText(this, "Connecting to ${device.name}...", Toast.LENGTH_SHORT).show()
                        },
                        onDisconnectDevice = {
                            hardwareBleManager.disconnect()
                            Toast.makeText(this, "Disconnected hardware", Toast.LENGTH_SHORT).show()
                        },
                        onSelectScenario = { scenario ->
                            currentScenario = scenario
                            bleSimulator.currentScenario = scenario
                            Toast.makeText(this, "Simulation scenario: ${scenario.name}", Toast.LENGTH_SHORT).show()
                        },
                        onTriggerSos = {
                            val payload = disasterManager.createCompactEmergencyPayload(
                                currentVitals, currentAssessment
                            )
                            emergencyPayload = payload
                            pendingQueueCount = disasterManager.getPendingQueue().size
                            Toast.makeText(this, "SOS Emergency Payload Generated & Encrypted!", Toast.LENGTH_LONG).show()
                        },
                        onGenerateSmsPayload = {
                            val payload = disasterManager.createCompactEmergencyPayload(
                                currentVitals, currentAssessment
                            )
                            emergencyPayload = payload
                            pendingQueueCount = disasterManager.getPendingQueue().size
                            Toast.makeText(this, "Compact Emergency SMS Broadcasted!", Toast.LENGTH_SHORT).show()
                        },
                        onFlushQueue = {
                            val flushed = disasterManager.flushQueueOnConnectivity()
                            pendingQueueCount = 0
                            Toast.makeText(this, "Synced $flushed offline payloads!", Toast.LENGTH_SHORT).show()
                        },
                        onUpdatePrivacy = { updated ->
                            privacySettings = updated
                        },
                        onPurgeVault = {
                            storageManager.purgeVault()
                            alertsList = emptyList()
                            Toast.makeText(this, "Local Vault Purged!", Toast.LENGTH_SHORT).show()
                        },
                        onExportLogs = {
                            Toast.makeText(this, "Encrypted Log Exported to Local Storage!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkPermissionsAndScan() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            hardwareBleManager.startScan()
        } else {
            requestPermissionLauncher.launch(missing.toTypedArray())
        }
    }

    private fun checkAndTriggerAlert(
        assessment: AiRiskAssessment,
        currentAlerts: List<HealthAlert>,
        onUpdate: (List<HealthAlert>) -> Unit
    ) {
        if (assessment.overallRiskScore > 50 && currentAlerts.none { it.title == assessment.primaryThreat }) {
            val alert = HealthAlert(
                title = assessment.primaryThreat,
                message = "Automated Aegis Guard anomaly trigger: ${assessment.primaryThreat}",
                severity = assessment.riskLevel,
                category = when {
                    assessment.primaryThreat.contains("Cardio") -> AlertCategory.CARDIO
                    assessment.primaryThreat.contains("Fall") -> AlertCategory.MOTION_FALL
                    assessment.primaryThreat.contains("Heat") -> AlertCategory.HEAT_STROKE
                    assessment.primaryThreat.contains("Hypoxia") -> AlertCategory.RESPIRATORY
                    else -> AlertCategory.POLLUTION_ENVIRONMENT
                }
            )
            onUpdate(listOf(alert) + currentAlerts)
        }
    }
}
