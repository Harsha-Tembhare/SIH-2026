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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.PrivacySettings
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun PrivacyScreen(
    privacySettings: PrivacySettings,
    onUpdatePrivacy: (PrivacySettings) -> Unit,
    onPurgeVault: () -> Unit,
    modifier: Modifier = Modifier
) {
    var settings by remember { mutableStateOf(privacySettings) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AegisBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "GRANULAR PRIVACY & VAULT CONSENT",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Android Keystore Encrypted Local Vault",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        // Encryption Vault Status Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AegisSurface)
                    .border(1.dp, AegisEmerald, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Android Keystore AES-256 GCM",
                            style = Typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = AegisEmerald
                        )
                        Text(
                            text = "Encrypted Local Room DB + SQLCipher",
                            style = Typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = "ENCRYPTED",
                        style = Typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AegisEmerald
                    )
                }
            }
        }

        // Sensor Permission Toggles
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "GRANULAR SENSOR CONSENT CONTROLS",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                SensorConsentToggleItem(
                    title = "Heart Rate Sensor (PPG)",
                    subtitle = "Continuous pulse & cardiac anomaly tracking",
                    checked = settings.hrConsent,
                    onCheckedChange = {
                        settings = settings.copy(hrConsent = it)
                        onUpdatePrivacy(settings)
                    }
                )

                SensorConsentToggleItem(
                    title = "SpO₂ Pulse Oximeter",
                    subtitle = "Blood oxygen saturation monitoring",
                    checked = settings.spo2Consent,
                    onCheckedChange = {
                        settings = settings.copy(spo2Consent = it)
                        onUpdatePrivacy(settings)
                    }
                )

                SensorConsentToggleItem(
                    title = "3-Axis Motion Accelerometer",
                    subtitle = "High-G impact fall detection",
                    checked = settings.motionConsent,
                    onCheckedChange = {
                        settings = settings.copy(motionConsent = it)
                        onUpdatePrivacy(settings)
                    }
                )

                SensorConsentToggleItem(
                    title = "AQI & Environment Location",
                    subtitle = "Air quality index & thermal stress risk",
                    checked = settings.locationConsent,
                    onCheckedChange = {
                        settings = settings.copy(locationConsent = it)
                        onUpdatePrivacy(settings)
                    }
                )

                SensorConsentToggleItem(
                    title = "Strict Zero-Cloud Enforcement",
                    subtitle = "Blocks 100% network data outbound connections",
                    checked = settings.zeroCloudMode,
                    onCheckedChange = {
                        settings = settings.copy(zeroCloudMode = it)
                        onUpdatePrivacy(settings)
                    }
                )
            }
        }

        // Data Vault Purge Action
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AegisSurface)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "1-Click Local Data Vault Purge",
                        style = Typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = AegisRed
                    )
                    Text(
                        text = "Instantly wipes all encrypted database logs, personalized baselines, and emergency cache from device storage.",
                        style = Typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onPurgeVault,
                        colors = ButtonDefaults.buttonColors(containerColor = AegisRed)
                    ) {
                        Text(text = "Purge All Local Encrypted Data", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SensorConsentToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = Typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(text = subtitle, style = Typography.bodyMedium.copy(fontSize = 12.sp), color = TextSecondary)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = AegisCyan)
            )
        }
    }
}
