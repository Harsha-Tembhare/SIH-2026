package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.*
import com.qualcomm.sih26181.aegishealth.ui.components.*
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun DashboardScreen(
    vitals: VitalsData,
    baseline: PersonalBaseline,
    assessment: AiRiskAssessment,
    alerts: List<HealthAlert>,
    onTriggerSos: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AegisBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AEGIS GUARDIAN",
                        style = Typography.labelSmall,
                        color = AegisCyan,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Medical Command",
                        style = Typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )
                }

                QuickSosButton(onClick = onTriggerSos)
            }
        }

        // Active Alert Banner if exists
        if (alerts.any { !it.isResolved }) {
            item {
                val active = alerts.first { !it.isResolved }
                AlertBanner(alert = active)
            }
        }

        // On-Device Risk Gauge
        item {
            RiskScoreGauge(assessment = assessment)
        }

        // Live Vital Rings (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "PHYSIOLOGICAL & SENSOR RINGS",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VitalRingCard(
                        title = "Heart Rate",
                        value = "${vitals.heartRate.toInt()}",
                        unit = "BPM",
                        progress = (vitals.heartRate / 180.0).toFloat(),
                        accentColor = AegisRed,
                        subText = "Base: ${baseline.hrMean.toInt()} BPM",
                        modifier = Modifier.weight(1f)
                    )
                    VitalRingCard(
                        title = "SpO₂ Saturation",
                        value = "${vitals.spo2.toInt()}",
                        unit = "%",
                        progress = (vitals.spo2 / 100.0).toFloat(),
                        accentColor = AegisCyan,
                        subText = "Base: ${baseline.spo2Baseline}%",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VitalRingCard(
                        title = "Core Temp",
                        value = String.format("%.1f", vitals.bodyTemperature),
                        unit = "°C",
                        progress = ((vitals.bodyTemperature - 35) / 5.0).toFloat(),
                        accentColor = AegisAmber,
                        subText = "Heat Index ${String.format("%.1f", vitals.heatIndex)}°C",
                        modifier = Modifier.weight(1f)
                    )
                    VitalRingCard(
                        title = "Air Quality AQI",
                        value = "${vitals.aqi.toInt()}",
                        unit = "PM2.5",
                        progress = (vitals.aqi / 300.0).toFloat(),
                        accentColor = AegisViolet,
                        subText = if (vitals.aqi > 100) "Unhealthy" else "Good Air",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Signal SQI & Baseline Status
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AegisSurface, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Adaptive Baseline Status",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Confidence Score: ${baseline.adaptiveConfidenceScore}% | ${baseline.sampleCount} samples",
                            style = Typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = "100% Offline Edge",
                        style = Typography.labelSmall,
                        color = AegisEmerald
                    )
                }
            }
        }
    }
}
