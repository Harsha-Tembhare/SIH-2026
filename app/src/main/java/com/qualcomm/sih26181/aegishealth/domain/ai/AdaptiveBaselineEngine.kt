package com.qualcomm.sih26181.aegishealth.domain.ai

import com.qualcomm.sih26181.aegishealth.domain.model.PersonalBaseline
import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData
import kotlin.math.abs
import kotlin.math.sqrt

class AdaptiveBaselineEngine {

    private var currentBaseline = PersonalBaseline()

    fun getBaseline(): PersonalBaseline = currentBaseline

    /**
     * Updates rolling personalized baseline using Exponential Moving Average (EMA)
     */
    fun updateBaseline(vitals: VitalsData): PersonalBaseline {
        val alpha = 0.01 // Smoothing factor for 24h baseline adaptation
        val newHrMean = currentBaseline.hrMean * (1 - alpha) + vitals.heartRate * alpha
        val hrDiff = abs(vitals.heartRate - newHrMean)
        val newHrStdDev = currentBaseline.hrStdDev * (1 - alpha) + hrDiff * alpha
        val newSpo2Baseline = currentBaseline.spo2Baseline * (1 - alpha) + vitals.spo2 * alpha
        val newTempBaseline = currentBaseline.tempBaseline * (1 - alpha) + vitals.bodyTemperature * alpha

        currentBaseline = currentBaseline.copy(
            hrMean = newHrMean,
            hrStdDev = newHrStdDev.coerceAtLeast(3.0),
            spo2Baseline = newSpo2Baseline,
            tempBaseline = newTempBaseline,
            sampleCount = currentBaseline.sampleCount + 1,
            lastUpdated = System.currentTimeMillis()
        )
        return currentBaseline
    }

    /**
     * Computes Z-Score metric for heart rate deviation: Z = (X - μ) / σ
     */
    fun computeHrZScore(currentHr: Double): Double {
        val stdDev = currentBaseline.hrStdDev.coerceAtLeast(3.0)
        return (currentHr - currentBaseline.hrMean) / stdDev
    }
}
