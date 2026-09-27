package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AegisBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "GUARDIAN SETTINGS & CONTACTS",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "System Configuration",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        // Qualcomm Edge AI Hardware Accelerator Info
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AegisSurface)
                    .border(1.dp, AegisCyan, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "QUALCOMM SNAPDRAGON EDGE AI",
                        style = Typography.labelSmall,
                        color = AegisCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "NPU Hardware Acceleration: Active (Hexagon Vector Extensions)",
                        style = Typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Inference Model: aegis_health_quantized_v2.tflite (0.8MB)",
                        style = Typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Emergency Contacts Management
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "EMERGENCY CONTACT RECIPIENTS",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AegisSurface)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Dr. Robert Vance (Primary Cardiologist)", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "+1 (555) 019-2831 | SMS Disaster Node", style = Typography.bodyMedium, color = TextSecondary)
                        }
                        Text(text = "PRIMARY", style = Typography.labelSmall, color = AegisEmerald, fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AegisSurface)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Elena Smith (Family Kin)", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "+1 (555) 018-9920 | Emergency Beacon", style = Typography.bodyMedium, color = TextSecondary)
                        }
                        Text(text = "SECONDARY", style = Typography.labelSmall, color = TextMuted)
                    }
                }
            }
        }
    }
}
