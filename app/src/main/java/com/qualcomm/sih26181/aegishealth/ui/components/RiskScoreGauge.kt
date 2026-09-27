package com.qualcomm.sih26181.aegishealth.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.AiRiskAssessment
import com.qualcomm.sih26181.aegishealth.domain.model.RiskLevel
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun RiskScoreGauge(
    assessment: AiRiskAssessment,
    modifier: Modifier = Modifier
) {
    val gaugeColor = when (assessment.riskLevel) {
        RiskLevel.LOW -> RiskLow
        RiskLevel.ELEVATED -> RiskElevated
        RiskLevel.HIGH -> RiskHigh
        RiskLevel.CRITICAL -> RiskCritical
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(AegisSurface)
            .border(1.dp, AegisBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ON-DEVICE QUALCOMM AI RISK FUSION",
                style = Typography.labelSmall,
                color = TextSecondary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Outer Track
                    drawArc(
                        color = AegisSurfaceVariant,
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Gauge Fill Arc
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(RiskLow, gaugeColor)
                        ),
                        startAngle = 140f,
                        sweepAngle = 260f * (assessment.overallRiskScore / 100f),
                        useCenter = false,
                        style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${assessment.overallRiskScore}%",
                        style = Typography.displayLarge.copy(fontSize = 36.sp),
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = assessment.riskLevel.name,
                        style = Typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = gaugeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Primary Vector: ${assessment.primaryThreat}",
                    style = Typography.bodyMedium.copy(fontSize = 12.sp),
                    color = TextSecondary
                )
                Text(
                    text = "Inference: ${assessment.onDeviceInferenceTimeMs}ms",
                    style = Typography.labelSmall,
                    color = AegisCyan
                )
            }
        }
    }
}
