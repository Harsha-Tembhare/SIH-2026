package com.qualcomm.sih26181.aegishealth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.HealthAlert
import com.qualcomm.sih26181.aegishealth.domain.model.RiskLevel
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun AlertBanner(
    alert: HealthAlert,
    modifier: Modifier = Modifier
) {
    val bannerColor = when (alert.severity) {
        RiskLevel.LOW -> AegisEmerald
        RiskLevel.ELEVATED -> AegisAmber
        RiskLevel.HIGH -> RiskHigh
        RiskLevel.CRITICAL -> RiskCritical
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bannerColor.copy(alpha = 0.15f))
            .border(1.dp, bannerColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alert.title,
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = bannerColor
                )
                Text(
                    text = alert.message,
                    style = Typography.bodyMedium.copy(fontSize = 12.sp),
                    color = TextPrimary
                )
            }
            Text(
                text = alert.category.name,
                style = Typography.labelSmall,
                color = bannerColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(bannerColor.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
