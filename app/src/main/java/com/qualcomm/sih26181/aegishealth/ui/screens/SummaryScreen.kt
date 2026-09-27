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
import com.qualcomm.sih26181.aegishealth.domain.model.PersonalBaseline
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun SummaryScreen(
    baseline: PersonalBaseline,
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
                text = "DAILY HEALTH EXECUTIVE REPORT",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Guardian Insights & Recovery",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        // Daily Score Summary Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AegisSurface)
                    .border(1.dp, AegisBorder, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "DAILY HEALTH INDEX",
                        style = Typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "94 / 100",
                        style = Typography.displayLarge.copy(fontSize = 42.sp),
                        fontWeight = FontWeight.Bold,
                        color = AegisEmerald
                    )
                    Text(
                        text = "Optimal Physiological Stability",
                        style = Typography.bodyLarge,
                        color = AegisEmerald,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Recovery & Strain Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AegisSurface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = "Recovery Score", style = Typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "88%", style = Typography.titleLarge, color = AegisCyan, fontWeight = FontWeight.Bold)
                        Text(text = "Optimal HRV", style = Typography.labelSmall, color = TextSecondary)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(AegisSurface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = "Thermal Strain", style = Typography.labelSmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Low", style = Typography.titleLarge, color = AegisEmerald, fontWeight = FontWeight.Bold)
                        Text(text = "Normal Heat Index", style = Typography.labelSmall, color = TextSecondary)
                    }
                }
            }
        }

        // On-device Clinical Summary Advice
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
                        text = "ON-DEVICE GUARDIAN SUMMARY",
                        style = Typography.labelSmall,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Heart rate baseline is stable around ${baseline.hrMean.toInt()} BPM with normal variability.\n" +
                                "• SpO₂ levels maintained an average of ${baseline.spo2Baseline}% throughout the day.\n" +
                                "• Motion acceleration sensor recorded 0 fall/impact events.\n" +
                                "• Ambient environment remained within tolerable AQI boundaries.",
                        style = Typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
