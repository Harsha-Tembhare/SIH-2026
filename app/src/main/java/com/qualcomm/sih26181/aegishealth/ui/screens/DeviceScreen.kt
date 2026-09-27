package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.data.repository.DiscoveredBleDevice
import com.qualcomm.sih26181.aegishealth.data.repository.SimulationScenario
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun DeviceScreen(
    currentScenario: SimulationScenario,
    onSelectScenario: (SimulationScenario) -> Unit,
    isHardwareConnected: Boolean,
    connectedHardwareName: String?,
    rawPayloadLog: String,
    isScanning: Boolean,
    scannedDevices: List<DiscoveredBleDevice>,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onConnectDevice: (DiscoveredBleDevice) -> Unit,
    onDisconnectDevice: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isHardwareModeActive by remember { mutableStateOf(isHardwareConnected) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AegisBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "QUALCOMM HARDWARE & BLE PAIRING CENTER",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Biometric Sensor Connection",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        // Mode Switcher Header Card (Hardware vs Simulator)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AegisSurface)
                    .border(1.dp, AegisBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHardwareConnected) "REAL BLE HARDWARE ACTIVE" else "SIMULATION MODE ACTIVE",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isHardwareConnected) AegisEmerald else AegisCyan
                        )
                        Text(
                            text = if (isHardwareConnected) 
                                "Connected to: ${connectedHardwareName ?: "ESP32 / Wearable Device"}"
                            else 
                                "Using local realistic biometric telemetry simulator",
                            style = Typography.bodyMedium.copy(fontSize = 12.sp),
                            color = TextSecondary
                        )
                    }

                    Switch(
                        checked = isHardwareModeActive || isHardwareConnected,
                        onCheckedChange = { checked ->
                            isHardwareModeActive = checked
                            if (checked && !isScanning && !isHardwareConnected) {
                                onStartScan()
                            } else if (!checked && isHardwareConnected) {
                                onDisconnectDevice()
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AegisEmerald,
                            checkedTrackColor = AegisEmerald.copy(alpha = 0.3f),
                            uncheckedThumbColor = AegisCyan,
                            uncheckedTrackColor = AegisSurfaceVariant
                        )
                    )
                }
            }
        }

        // Live Raw Payload Log Monitor Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A))
                    .border(1.dp, AegisCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Console",
                            tint = AegisCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LIVE HARDWARE TELEMETRY CONSOLE",
                            style = Typography.labelSmall,
                            color = AegisCyan,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = rawPayloadLog,
                        style = Typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF38BDF8)
                    )
                }
            }
        }

        // Section 1: Real Hardware Scanner & Device List
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REAL HARDWARE SCANNER (BLE / ESP32 / SENSORS)",
                        style = Typography.labelSmall,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )

                    Button(
                        onClick = { if (isScanning) onStopScan() else onStartScan() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isScanning) AegisRed else AegisCyan
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isScanning) Icons.Default.BluetoothSearching else Icons.Default.Bluetooth,
                            contentDescription = "Scan",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isScanning) "STOP SCAN" else "SCAN DEVICES",
                            style = Typography.labelSmall.copy(fontSize = 11.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                if (isHardwareConnected) {
                    // Connected Status Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(AegisEmerald.copy(alpha = 0.12f))
                            .border(1.dp, AegisEmerald, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.BluetoothConnected,
                                    contentDescription = "Connected",
                                    tint = AegisEmerald,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = connectedHardwareName ?: "Ble Hardware Device",
                                        style = Typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Status: Universal GATT Stream Active",
                                        style = Typography.labelSmall,
                                        color = AegisEmerald
                                    )
                                }
                            }

                            Button(
                                onClick = onDisconnectDevice,
                                colors = ButtonDefaults.buttonColors(containerColor = AegisRed.copy(alpha = 0.8f)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("DISCONNECT", style = Typography.labelSmall, color = Color.White)
                            }
                        }
                    }
                }

                if (isScanning && scannedDevices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AegisSurface)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AegisCyan,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Scanning for nearby BLE hardware...",
                                style = Typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // List of scanned hardware devices
        if (scannedDevices.isNotEmpty()) {
            items(scannedDevices) { device ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AegisSurface)
                        .border(1.dp, AegisBorder, RoundedCornerShape(12.dp))
                        .clickable { onConnectDevice(device) }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = device.name,
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "MAC: ${device.address} | RSSI: ${device.rssi} dBm",
                                style = Typography.bodyMedium.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = { onConnectDevice(device) },
                            colors = ButtonDefaults.buttonColors(containerColor = AegisCyan),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("PAIR & CONNECT", style = Typography.labelSmall, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section 2: Real-Time Anomaly Injectors / Simulator Controls
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "HARDWARE SIMULATOR & ANOMALY INJECTORS",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                ScenarioSelectionCard(
                    title = "Normal Rest / Baseline Telemetry",
                    description = "HR 72 BPM, SpO2 98%, Temp 36.6°C, AQI 42",
                    isSelected = currentScenario == SimulationScenario.NORMAL && !isHardwareConnected,
                    onClick = { onSelectScenario(SimulationScenario.NORMAL) },
                    accentColor = AegisEmerald
                )

                ScenarioSelectionCard(
                    title = "Arrhythmia / Tachycardia Pulse Spike",
                    description = "Rapid HR surge to 148 BPM, baseline z-score anomaly",
                    isSelected = currentScenario == SimulationScenario.ARRHYTHMIA_TACHYCARDIA && !isHardwareConnected,
                    onClick = { onSelectScenario(SimulationScenario.ARRHYTHMIA_TACHYCARDIA) },
                    accentColor = AegisAmber
                )

                ScenarioSelectionCard(
                    title = "Sudden Fall / High-G Impact Event",
                    description = "Acceleration surge to 3.85G, immobility flag triggered",
                    isSelected = currentScenario == SimulationScenario.SUDDEN_FALL_IMPACT && !isHardwareConnected,
                    onClick = { onSelectScenario(SimulationScenario.SUDDEN_FALL_IMPACT) },
                    accentColor = AegisRed
                )

                ScenarioSelectionCard(
                    title = "Heat Stroke & Hyperthermia Threat",
                    description = "Ambient 42.5°C, core temp surge to 39.4°C + humidity strain",
                    isSelected = currentScenario == SimulationScenario.HEAT_STROKE_EXHAUSTION && !isHardwareConnected,
                    onClick = { onSelectScenario(SimulationScenario.HEAT_STROKE_EXHAUSTION) },
                    accentColor = RiskHigh
                )

                ScenarioSelectionCard(
                    title = "Hypoxia / Respiratory Dip",
                    description = "SpO2 drop to 84%, elevated AQI 185 pollution trigger",
                    isSelected = currentScenario == SimulationScenario.HYPOXIA_SPO2_DROP && !isHardwareConnected,
                    onClick = { onSelectScenario(SimulationScenario.HYPOXIA_SPO2_DROP) },
                    accentColor = AegisViolet
                )
            }
        }
    }
}

@Composable
fun ScenarioSelectionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.15f) else AegisSurface)
            .border(
                1.dp,
                if (isSelected) accentColor else AegisBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = Typography.bodyLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = description, style = Typography.bodyMedium.copy(fontSize = 12.sp), color = TextSecondary)
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = accentColor)
            )
        }
    }
}
