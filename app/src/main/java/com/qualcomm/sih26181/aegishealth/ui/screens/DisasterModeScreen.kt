package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.qualcomm.sih26181.aegishealth.domain.model.AiRiskAssessment
import com.qualcomm.sih26181.aegishealth.domain.model.EmergencyPayload
import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun DisasterModeScreen(
    vitals: VitalsData,
    assessment: AiRiskAssessment,
    emergencyPayload: EmergencyPayload?,
    onGenerateSmsPayload: () -> Unit,
    pendingQueueCount: Int,
    onFlushQueue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMeshBroadcasting by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AegisDarkRed)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DISASTER & OFFLINE PROTOCOL",
                        style = Typography.labelSmall,
                        color = AegisRed,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Emergency Mesh Beacon",
                        style = Typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AegisRed)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ZERO INTERNET MODE",
                        style = Typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // GPS Tracker Coordinates
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E0008))
                    .border(1.dp, AegisRed, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "CACHED OFFLINE GPS POSITION",
                        style = Typography.labelSmall,
                        color = AegisRed,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Lat: 28.6139° N, Lng: 77.2090° E",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Acc: ± 3.2m",
                            style = Typography.labelSmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        // Emergency SMS Payload Generator
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E0008))
                    .border(1.dp, AegisRed, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "COMPACT SMS ENCODED PAYLOAD",
                        style = Typography.labelSmall,
                        color = AegisRed,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = emergencyPayload?.compactHexProtocol ?: "AEGIS|GPS:28.6139,77.2090|HR:118|SPO2:91|RISK:88|FALL:0",
                        style = Typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                        color = AegisAmber
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = onGenerateSmsPayload,
                            colors = ButtonDefaults.buttonColors(containerColor = AegisRed)
                        ) {
                            Text(text = "Broadcast Emergency SMS", color = Color.White)
                        }
                    }
                }
            }
        }

        // Offline BLE Mesh Beacon Broadcast Toggle
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E0008))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bluetooth LE Mesh Beacon",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Continuously advertises compact SOS packet to nearby Aegis peer nodes.",
                            style = Typography.bodyMedium,
                            color = Color.LightGray
                        )
                    }

                    Switch(
                        checked = isMeshBroadcasting,
                        onCheckedChange = { isMeshBroadcasting = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = AegisRed)
                    )
                }
            }
        }

        // Offline Emergency Queue Status
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E0008))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Offline Retry Sync Queue",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$pendingQueueCount emergency payloads queued",
                            style = Typography.bodyMedium,
                            color = AegisAmber
                        )
                    }

                    if (pendingQueueCount > 0) {
                        Button(
                            onClick = onFlushQueue,
                            colors = ButtonDefaults.buttonColors(containerColor = AegisSurfaceVariant)
                        ) {
                            Text(text = "Sync Now", color = AegisCyan)
                        }
                    }
                }
            }
        }
    }
}
