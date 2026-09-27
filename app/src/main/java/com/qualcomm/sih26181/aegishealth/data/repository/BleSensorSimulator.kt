package com.qualcomm.sih26181.aegishealth.data.repository

import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

enum class SimulationScenario {
    NORMAL,
    ARRHYTHMIA_TACHYCARDIA,
    SUDDEN_FALL_IMPACT,
    HEAT_STROKE_EXHAUSTION,
    HYPOXIA_SPO2_DROP
}

class BleSensorSimulator {

    var currentScenario: SimulationScenario = SimulationScenario.NORMAL
    var isConnected: Boolean = true
    var rssiDbm: Int = -58

    /**
     * Emits continuous real-time BLE telemetry packet stream every 800ms
     */
    fun observeTelemetry(): Flow<VitalsData> = flow {
        var baseHr = 72.0
        var baseSpo2 = 98.0
        var baseTemp = 36.6
        var baseAqi = 45.0
        var baseHumidity = 52.0

        while (true) {
            val noiseHr = Random.nextDouble(-1.5, 1.5)
            val noiseSpo2 = Random.nextDouble(-0.3, 0.3)
            val noiseTemp = Random.nextDouble(-0.05, 0.05)

            var accelX = 0.02 + Random.nextDouble(-0.05, 0.05)
            var accelY = 0.98 + Random.nextDouble(-0.05, 0.05)
            var accelZ = 0.04 + Random.nextDouble(-0.05, 0.05)

            when (currentScenario) {
                SimulationScenario.NORMAL -> {
                    baseHr = (baseHr + noiseHr).coerceIn(68.0, 76.0)
                    baseSpo2 = (baseSpo2 + noiseSpo2).coerceIn(97.5, 99.5)
                    baseTemp = 36.6 + noiseTemp
                    baseAqi = 42.0 + Random.nextDouble(-3.0, 3.0)
                }
                SimulationScenario.ARRHYTHMIA_TACHYCARDIA -> {
                    baseHr = (baseHr + Random.nextDouble(4.0, 12.0)).coerceIn(135.0, 168.0)
                    baseSpo2 = 96.0 + noiseSpo2
                    baseTemp = 37.1 + noiseTemp
                }
                SimulationScenario.SUDDEN_FALL_IMPACT -> {
                    accelX = 2.85 + Random.nextDouble(-0.4, 0.4)
                    accelY = 3.65 + Random.nextDouble(-0.5, 0.5)
                    accelZ = 1.90 + Random.nextDouble(-0.3, 0.3)
                    baseHr = 125.0 + noiseHr
                }
                SimulationScenario.HEAT_STROKE_EXHAUSTION -> {
                    baseTemp = (baseTemp + 0.15).coerceAtMost(39.4)
                    baseHr = (baseHr + 2.0).coerceAtMost(132.0)
                    baseHumidity = 82.0
                    baseAqi = 88.0
                }
                SimulationScenario.HYPOXIA_SPO2_DROP -> {
                    baseSpo2 = (baseSpo2 - 1.2).coerceAtLeast(84.0)
                    baseHr = (baseHr + 1.8).coerceAtMost(118.0)
                    baseAqi = 185.0
                }
            }

            val data = VitalsData(
                timestamp = System.currentTimeMillis(),
                heartRate = baseHr,
                spo2 = baseSpo2,
                bodyTemperature = baseTemp,
                accelX = accelX,
                accelY = accelY,
                accelZ = accelZ,
                ambientTemp = if (currentScenario == SimulationScenario.HEAT_STROKE_EXHAUSTION) 42.5 else 29.0,
                humidity = baseHumidity,
                aqi = baseAqi,
                signalQualityIndex = if (currentScenario == SimulationScenario.SUDDEN_FALL_IMPACT) 64.0 else 97.2
            )

            emit(data)
            delay(800)
        }
    }
}
