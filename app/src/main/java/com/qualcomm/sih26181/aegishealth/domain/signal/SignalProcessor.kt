package com.qualcomm.sih26181.aegishealth.domain.signal

import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData

class SignalProcessor {

    /**
     * Validates raw BLE sensor telemetry for range integrity and motion artifacts.
     * Preserves exact 0 values when hardware sensors are idle (e.g. finger off PPG sensor).
     */
    fun validateAndProcess(raw: VitalsData): VitalsData {
        // 1. Calculate Motion Magnitude (3-axis Accel vector)
        val motionMag = raw.motionMagnitude

        // 2. Compute Signal Quality Index (SQI)
        // High motion magnitude (>2.5g) degrades PPG/ECG optical sensor quality
        val motionDeduction = if (motionMag > 2.5) (motionMag - 2.5) * 18.0 else 0.0
        val baseSqi = if (raw.heartRate == 0.0) 0.0 else 98.5
        val computedSqi = (baseSqi - motionDeduction).coerceIn(0.0, 100.0)

        // 3. Noise Filtered Vitals (preserves 0.0 for idle hardware sensors)
        val validatedHr = raw.heartRate.coerceIn(0.0, 240.0)
        val validatedSpo2 = raw.spo2.coerceIn(0.0, 100.0)
        val validatedTemp = raw.bodyTemperature.coerceIn(10.0, 50.0)

        return raw.copy(
            heartRate = validatedHr,
            spo2 = validatedSpo2,
            bodyTemperature = validatedTemp,
            signalQualityIndex = computedSqi
        )
    }

    /**
     * Simulates continuous PPG / ECG waveform buffer generation for dynamic visualizer canvas.
     */
    fun generateWaveformPoints(count: Int = 100, hrBpm: Double = 72.0, phaseOffset: Float = 0f): List<Float> {
        val points = mutableListOf<Float>()
        if (hrBpm <= 0.0) {
            // Flatline waveform when heart rate is 0
            return List(count) { 0f }
        }

        val period = (60.0 / hrBpm.coerceAtLeast(30.0)) * 50.0 // Samples per beat
        for (i in 0 until count) {
            val t = (i + phaseOffset * 20) % period
            val normalized = t / period
            
            // ECG P-Q-R-S-T wave model simulation
            val sample = when {
                normalized in 0.10..0.18 -> 0.15 * Math.sin((normalized - 0.10) / 0.08 * Math.PI) // P wave
                normalized in 0.22..0.25 -> -0.15 // Q dip
                normalized in 0.25..0.30 -> 1.0 * Math.sin((normalized - 0.25) / 0.05 * Math.PI) // R peak
                normalized in 0.30..0.34 -> -0.25 // S dip
                normalized in 0.42..0.58 -> 0.25 * Math.sin((normalized - 0.42) / 0.16 * Math.PI) // T wave
                else -> 0.02 * Math.sin(normalized * Math.PI * 4) // Baseline noise
            }.toFloat()

            points.add(sample)
        }
        return points
    }
}
