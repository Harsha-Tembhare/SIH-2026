package com.qualcomm.sih26181.aegishealth.domain.model

data class PersonalBaseline(
    val id: String = "default_user",
    val hrMean: Double = 70.0,
    val hrStdDev: Double = 6.5,
    val spo2Baseline: Double = 98.2,
    val tempBaseline: Double = 36.5,
    val motionBaselineAvg: Double = 1.0,
    val sampleCount: Int = 1440,
    val lastUpdated: Long = System.currentTimeMillis(),
    val adaptiveConfidenceScore: Double = 94.0 // 0-100%
)
