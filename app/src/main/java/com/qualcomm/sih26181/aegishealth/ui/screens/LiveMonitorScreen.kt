package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData
import com.qualcomm.sih26181.aegishealth.ui.components.WaveformVisualizer
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun LiveMonitorScreen(
    vitals: VitalsData,
    waveformPoints: List<Float>,
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
                text = "REAL-TIME TELEMETRY MONITOR",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Sensor Signal Oscilloscope",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        // Live ECG Canvas
        item {
            WaveformVisualizer(
                points = waveformPoints,
                signalQualityIndex = vitals.signalQualityIndex
            )
        }

        // 3-Axis Acceleration Vector
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
                        text = "3-AXIS ACCELEROMETER & GYROSCOPE VECTORS",
                        style = Typography.labelSmall,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "X-Axis: ${String.format("%.3f", vitals.accelX)}G", color = AegisCyan)
                            Text(text = "Y-Axis: ${String.format("%.3f", vitals.accelY)}G", color = AegisEmerald)
                            Text(text = "Z-Axis: ${String.format("%.3f", vitals.accelZ)}G", color = AegisAmber)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Magnitude Vector",
                                style = Typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "${String.format("%.2f", vitals.motionMagnitude)}G",
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (vitals.motionMagnitude > 2.5) AegisRed else TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Detailed Vitals Metrics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "ENVIRONMENT & THERMAL METRICS",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AegisSurface)
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(text = "Ambient Temp", style = Typography.labelSmall, color = TextMuted)
                            Text(
                                text = "${vitals.ambientTemp}°C",
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AegisSurface)
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(text = "Relative Humidity", style = Typography.labelSmall, color = TextMuted)
                            Text(
                                text = "${vitals.humidity}%",
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
