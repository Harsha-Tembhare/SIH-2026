package com.qualcomm.sih26181.aegishealth.domain.model

enum class RiskLevel {
    LOW,        // 0 - 25%
    ELEVATED,   // 26 - 55%
    HIGH,       // 56 - 79%
    CRITICAL    // 80 - 100%
}

data class ExplainableFactor(
    val factorName: String,
    val impactPercentage: Int, // e.g. +35%
    val status: String,        // e.g. "SpO2 drop to 89%"
    val baselineComparison: String, // e.g. "-9.2% from 98.2% baseline"
    val severity: RiskLevel
)

data class AiRiskAssessment(
    val overallRiskScore: Int = 12, // 0 - 100
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val primaryThreat: String = "Normal Physiological Rhythm",
    val explainableFactors: List<ExplainableFactor> = emptyList(),
    val confidence: Double = 98.4,
    val onDeviceInferenceTimeMs: Long = 3,
    val timestamp: Long = System.currentTimeMillis(),
    val requiresEmergencyAction: Boolean = false
)
