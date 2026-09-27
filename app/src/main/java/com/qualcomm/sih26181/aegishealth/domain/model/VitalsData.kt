package com.qualcomm.sih26181.aegishealth.domain.model

data class VitalsData(
    val timestamp: Long = System.currentTimeMillis(),
    val heartRate: Double = 72.0,           // bpm
    val spo2: Double = 98.0,                // percentage
    val bodyTemperature: Double = 36.6,     // Celsius
    val accelX: Double = 0.01,
    val accelY: Double = 0.98,              // ~1g baseline gravity
    val accelZ: Double = 0.05,
    val gyroX: Double = 0.0,
    val gyroY: Double = 0.0,
    val gyroZ: Double = 0.0,
    val ambientTemp: Double = 28.5,          // Celsius
    val humidity: Double = 55.0,             // percentage
    val aqi: Double = 42.0,                 // AQI index
    val signalQualityIndex: Double = 96.5    // SQI %
) {
    val motionMagnitude: Double
        get() = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ)

    val heatIndex: Double
        get() {
            // Simplified Steadman formula for Heat Index (°C)
            val t = ambientTemp
            val rh = humidity
            return t + 0.33 * (rh / 100.0 * 6.105 * Math.exp(17.27 * t / (237.7 + t))) - 4.0
        }
}
