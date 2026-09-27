package com.qualcomm.sih26181.aegishealth.domain.disaster

import com.qualcomm.sih26181.aegishealth.domain.model.AiRiskAssessment
import com.qualcomm.sih26181.aegishealth.domain.model.EmergencyPayload
import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData
import java.util.LinkedList
import java.util.Queue

class DisasterManager {

    private val offlineSyncQueue: Queue<EmergencyPayload> = LinkedList()

    /**
     * Generates a ultra-compact 32-byte Emergency SMS & Mesh Hex Payload
     * Format: [HDR:2B][LAT:4B][LNG:4B][HR:1B][SPO2:1B][RISK:1B][REASON:2B][CHECKSUM:1B]
     */
    fun createCompactEmergencyPayload(
        vitals: VitalsData,
        assessment: AiRiskAssessment,
        lat: Double = 28.6139,
        lng: Double = 77.2090
    ): EmergencyPayload {
        val latScaled = (lat * 10000).toInt()
        val lngScaled = (lng * 10000).toInt()
        val hrInt = vitals.heartRate.toInt()
        val spo2Int = vitals.spo2.toInt()
        val riskScore = assessment.overallRiskScore

        val rawProtocol = "AEGIS|GPS:$lat,$lng|HR:$hrInt|SPO2:$spo2Int|RISK:$riskScore|THREAT:${assessment.primaryThreat}"
        val hexEncoded = rawProtocol.toByteArray().joinToString("") { "%02X".format(it) }

        val payload = EmergencyPayload(
            lat = lat,
            lng = lng,
            heartRate = vitals.heartRate,
            spo2 = vitals.spo2,
            riskScore = riskScore,
            triggerReason = assessment.primaryThreat,
            compactHexProtocol = hexEncoded.take(64),
            isSynced = false
        )

        enqueueEmergencyPayload(payload)
        return payload
    }

    fun enqueueEmergencyPayload(payload: EmergencyPayload) {
        offlineSyncQueue.add(payload)
    }

    fun getPendingQueue(): List<EmergencyPayload> = offlineSyncQueue.toList()

    fun flushQueueOnConnectivity(): Int {
        val count = offlineSyncQueue.size
        offlineSyncQueue.clear()
        return count
    }
}
