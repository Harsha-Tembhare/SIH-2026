package com.qualcomm.sih26181.aegishealth.domain.model

enum class AlertCategory {
    CARDIO,
    RESPIRATORY,
    MOTION_FALL,
    HEAT_STROKE,
    POLLUTION_ENVIRONMENT,
    SYSTEM
}

data class HealthAlert(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val severity: RiskLevel = RiskLevel.LOW,
    val title: String,
    val message: String,
    val category: AlertCategory,
    val isResolved: Boolean = false,
    val actionTaken: String? = null
)

data class EmergencyContact(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val phone: String,
    val relationship: String,
    val isPrimary: Boolean = false
)

data class EmergencyPayload(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val lat: Double = 28.6139,
    val lng: Double = 77.2090,
    val heartRate: Double,
    val spo2: Double,
    val riskScore: Int,
    val triggerReason: String,
    val compactHexProtocol: String,
    val isSynced: Boolean = false
)

data class PrivacySettings(
    val hrConsent: Boolean = true,
    val spo2Consent: Boolean = true,
    val motionConsent: Boolean = true,
    val locationConsent: Boolean = true,
    val aqiConsent: Boolean = true,
    val zeroCloudMode: Boolean = true,
    val isKeystoreEncrypted: Boolean = true,
    val autoPurgeOnTamper: Boolean = true
)
