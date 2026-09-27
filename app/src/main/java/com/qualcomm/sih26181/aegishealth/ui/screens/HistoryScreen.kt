package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun HistoryScreen(
    onExportLogs: () -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ENCRYPTED HEALTH LOGS",
                        style = Typography.labelSmall,
                        color = AegisCyan,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Historical Vital Trends",
                        style = Typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onExportLogs,
                    colors = ButtonDefaults.buttonColors(containerColor = AegisSurfaceVariant)
                ) {
                    Text(text = "Export Encrypted Log", color = AegisCyan)
                }
            }
        }

        // 7-Day HR Trend Overview
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AegisSurface)
                    .border(1.dp, AegisBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "7-DAY HEART RATE & SPO₂ STABILITY",
                        style = Typography.labelSmall,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    val hrs = listOf(71, 69, 74, 72, 70, 75, 71)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { index, day ->
                            val heightRatio = hrs[index] / 100f
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .fillMaxHeight(heightRatio)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AegisCyan)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = day, style = Typography.labelSmall, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }

        // Anomaly Event Log Timeline
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "HISTORICAL ANOMALY AUDIT",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AegisSurface)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Elevated Heart Rate Event", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "Peak: 142 BPM | Duration: 4 mins", style = Typography.bodyMedium, color = TextSecondary)
                        }
                        Text(text = "Yesterday 14:22", style = Typography.labelSmall, color = TextMuted)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AegisSurface)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Mild Hypoxia Transient Dip", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "SpO₂: 93% | Resolved automatically", style = Typography.bodyMedium, color = TextSecondary)
                        }
                        Text(text = "3 Days Ago", style = Typography.labelSmall, color = TextMuted)
                    }
                }
            }
        }
    }
}
