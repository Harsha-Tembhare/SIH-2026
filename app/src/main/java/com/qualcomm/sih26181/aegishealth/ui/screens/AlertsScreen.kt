package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.HealthAlert
import com.qualcomm.sih26181.aegishealth.ui.components.AlertBanner
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun AlertsScreen(
    alerts: List<HealthAlert>,
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
                text = "REAL-TIME & HISTORIC ALERTS",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Emergency Alert Audit",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        if (alerts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AegisSurface, shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "No active or critical health alerts triggered.",
                        style = Typography.bodyLarge,
                        color = AegisEmerald
                    )
                }
            }
        } else {
            items(alerts) { alert ->
                AlertBanner(alert = alert)
            }
        }
    }
}
