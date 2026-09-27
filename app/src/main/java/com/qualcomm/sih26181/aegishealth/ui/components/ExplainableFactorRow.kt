package com.qualcomm.sih26181.aegishealth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.ExplainableFactor
import com.qualcomm.sih26181.aegishealth.domain.model.RiskLevel
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun ExplainableFactorRow(
    factor: ExplainableFactor,
    modifier: Modifier = Modifier
) {
    val barColor = when (factor.severity) {
        RiskLevel.LOW -> RiskLow
        RiskLevel.ELEVATED -> RiskElevated
        RiskLevel.HIGH -> RiskHigh
        RiskLevel.CRITICAL -> RiskCritical
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AegisSurface)
            .border(1.dp, AegisBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = factor.factorName,
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "+${factor.impactPercentage}% Risk",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = factor.status,
                style = Typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Baseline Shift: ${factor.baselineComparison}",
                style = Typography.labelSmall,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (factor.impactPercentage / 50f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = barColor,
                trackColor = AegisSurfaceVariant
            )
        }
    }
}
