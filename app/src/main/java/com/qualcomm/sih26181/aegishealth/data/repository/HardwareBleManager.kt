package com.qualcomm.sih26181.aegishealth.data.repository

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import com.qualcomm.sih26181.aegishealth.domain.model.VitalsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

data class DiscoveredBleDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val device: BluetoothDevice
)

@SuppressLint("MissingPermission")
class HardwareBleManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        manager?.adapter
    }

    private val bleScanner: BluetoothLeScanner?
        get() = bluetoothAdapter?.bluetoothLeScanner

    private var gatt: BluetoothGatt? = null

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _connectedDeviceName = MutableStateFlow<String?>(null)
    val connectedDeviceName: StateFlow<String?> = _connectedDeviceName

    private val _scannedDevices = MutableStateFlow<List<DiscoveredBleDevice>>(emptyList())
    val scannedDevices: StateFlow<List<DiscoveredBleDevice>> = _scannedDevices

    private val _hardwareVitalsFlow = MutableSharedFlow<VitalsData>(extraBufferCapacity = 64)
    val hardwareVitalsFlow: SharedFlow<VitalsData> = _hardwareVitalsFlow

    private val _rawPayloadLog = MutableStateFlow("Waiting for hardware payload...")
    val rawPayloadLog: StateFlow<String> = _rawPayloadLog

    private val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        val HEART_RATE_MEASUREMENT_UUID: UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
        val HEALTH_THERMOMETER_UUID: UUID = UUID.fromString("00002a1c-0000-1000-8000-00805f9b34fb")
        val CLIENT_CHARACTERISTIC_CONFIG_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device ?: return
            val name = device.name ?: result.scanRecord?.deviceName ?: "Biometric Hardware Node"
            val address = device.address
            val rssi = result.rssi

            val current = _scannedDevices.value.toMutableList()
            val existingIndex = current.indexOfFirst { it.address == address }
            val item = DiscoveredBleDevice(name, address, rssi, device)

            if (existingIndex >= 0) {
                current[existingIndex] = item
            } else {
                current.add(item)
            }
            _scannedDevices.value = current
        }

        override fun onScanFailed(errorCode: Int) {
            _isScanning.value = false
        }
    }

    fun startScan() {
        if (bluetoothAdapter?.isEnabled != true) return
        _scannedDevices.value = emptyList()
        _isScanning.value = true
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        try {
            bleScanner?.startScan(null, settings, scanCallback)
        } catch (e: Exception) {
            _isScanning.value = false
        }
    }

    fun stopScan() {
        try {
            bleScanner?.stopScan(scanCallback)
        } catch (e: Exception) {
            // Ignore
        }
        _isScanning.value = false
    }

    fun connectToDevice(device: BluetoothDevice) {
        stopScan()
        disconnect()
        gatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
    }

    fun disconnect() {
        try {
            gatt?.disconnect()
            gatt?.close()
        } catch (e: Exception) {
            // Ignore
        }
        gatt = null
        _isConnected.value = false
        _connectedDeviceName.value = null
        _rawPayloadLog.value = "Disconnected from hardware."
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                _isConnected.value = true
                _connectedDeviceName.value = gatt.device.name ?: gatt.device.address
                _rawPayloadLog.value = "Connected! Discovering BLE services..."
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                _isConnected.value = false
                _connectedDeviceName.value = null
                _rawPayloadLog.value = "Hardware disconnected."
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                var enabledCount = 0
                // Universally discover and subscribe to ALL notify/indicate characteristics
                for (service in gatt.services) {
                    for (characteristic in service.characteristics) {
                        val props = characteristic.properties
                        val supportsNotify = (props and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0
                        val supportsIndicate = (props and BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0

                        if (supportsNotify || supportsIndicate) {
                            enableNotification(gatt, characteristic, supportsIndicate)
                            enabledCount++
                        }
                    }
                }
                _rawPayloadLog.value = "Connected! Subscribed to $enabledCount GATT channels."
            }
        }

        @Suppress("DEPRECATION")
        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val bytes = characteristic.value ?: return
            parseAndEmitVitals(characteristic.uuid, bytes)
        }
    }

    private fun enableNotification(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        isIndicate: Boolean
    ) {
        gatt.setCharacteristicNotification(characteristic, true)
        val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
        if (descriptor != null) {
            descriptor.value = if (isIndicate) {
                BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            } else {
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            }
            gatt.writeDescriptor(descriptor)
        }
    }

    private fun parseAndEmitVitals(uuid: UUID, bytes: ByteArray) {
        scope.launch {
            val textRepresentation = bytesToReadableText(bytes)
            _rawPayloadLog.value = "RAW: $textRepresentation"

            val parsedVitals = when (uuid) {
                HEART_RATE_MEASUREMENT_UUID -> parseStandardHeartRate(bytes)
                HEALTH_THERMOMETER_UUID -> parseHealthThermometer(bytes)
                else -> parseUniversalPayload(bytes, textRepresentation)
            }

            _hardwareVitalsFlow.emit(parsedVitals)
        }
    }

    private fun bytesToReadableText(bytes: ByteArray): String {
        return try {
            val string = String(bytes, Charsets.UTF_8).trim()
            if (string.all { it in ' '..'~' || it == '\n' || it == '\r' }) {
                string
            } else {
                bytes.joinToString(" ") { String.format("%02X", it) }
            }
        } catch (e: Exception) {
            bytes.joinToString(" ") { String.format("%02X", it) }
        }
    }

    /**
     * Smart Universal Multi-Format Parser
     * Specifically tuned for ESP32/OLED telemetry formats:
     * e.g. "HR:0 SpO2:0% T:25.6C H:48.6% P:1000.0hPa X:-0.6 Y:0.9 Z:0.4"
     */
    private fun parseUniversalPayload(bytes: ByteArray, text: String): VitalsData {
        // 1. Try JSON Parsing (e.g. {"hr": 0, "spo2": 0, "t": 25.6})
        if (text.startsWith("{") && text.endsWith("}")) {
            val jsonResult = parseJsonPayload(text)
            if (jsonResult != null) return jsonResult
        }

        // 2. Parse Key-Value Tokens (e.g. HR:0, SpO2:0%, T:25.6C, H:48.6%, X:-0.6, Y:0.9, Z:0.4)
        val kvResult = parseOledKeyValueText(text)
        if (kvResult != null) return kvResult

        // 3. Try Pure CSV / Space-Separated Numbers (e.g. "0, 0, 25.6, 48.6")
        val csvResult = parseCsvPayload(text)
        if (csvResult != null) return csvResult

        // 4. Fallback: Parse Binary Byte Stream
        return parseBinaryPayload(bytes)
    }

    private fun parseJsonPayload(text: String): VitalsData? {
        return try {
            val json = JSONObject(text)
            val hr = json.optDouble("hr", json.optDouble("heartRate", json.optDouble("bpm", 0.0)))
            val spo2 = json.optDouble("spo2", json.optDouble("o2", 0.0))
            val temp = json.optDouble("t", json.optDouble("temp", json.optDouble("temperature", 25.6)))
            val humidity = json.optDouble("h", json.optDouble("humidity", 48.6))
            val accelX = json.optDouble("x", -0.6)
            val accelY = json.optDouble("y", 0.9)
            val accelZ = json.optDouble("z", 0.4)

            VitalsData(
                timestamp = System.currentTimeMillis(),
                heartRate = hr.coerceIn(0.0, 240.0),
                spo2 = spo2.coerceIn(0.0, 100.0),
                bodyTemperature = temp.coerceIn(10.0, 50.0),
                ambientTemp = temp.coerceIn(10.0, 50.0),
                humidity = humidity.coerceIn(0.0, 100.0),
                accelX = accelX,
                accelY = accelY,
                accelZ = accelZ,
                signalQualityIndex = if (hr == 0.0) 0.0 else 99.0
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseOledKeyValueText(text: String): VitalsData? {
        var hr = 0.0
        var spo2 = 0.0
        var temp = 25.6
        var humidity = 48.6
        var accelX = -0.6
        var accelY = 0.9
        var accelZ = 0.4
        var foundAny = false

        try {
            // Split string by spaces, commas, newlines, or semicolons
            val tokens = text.replace("\n", " ").replace("\r", " ").replace(";", " ").replace(",", " ")
                .split(Regex("\\s+"))

            for (token in tokens) {
                val parts = when {
                    token.contains(":") -> token.split(":")
                    token.contains("=") -> token.split("=")
                    else -> continue
                }

                if (parts.size == 2) {
                    val key = parts[0].trim().uppercase()
                    val rawVal = parts[1].trim()
                    val cleanValStr = rawVal
                        .replace("%", "")
                        .replace("C", "")
                        .replace("c", "")
                        .replace("hPa", "")
                        .replace("°", "")
                        .trim()

                    val valNum = cleanValStr.toDoubleOrNull() ?: continue
                    when (key) {
                        "HR", "BPM", "PULSE" -> {
                            hr = valNum
                            foundAny = true
                        }
                        "SPO2", "O2" -> {
                            spo2 = valNum
                            foundAny = true
                        }
                        "T", "TEMP", "TEMPERATURE" -> {
                            temp = valNum
                            foundAny = true
                        }
                        "H", "HUMIDITY", "RH" -> {
                            humidity = valNum
                            foundAny = true
                        }
                        "X", "ACCELX" -> {
                            accelX = valNum
                            foundAny = true
                        }
                        "Y", "ACCELY" -> {
                            accelY = valNum
                            foundAny = true
                        }
                        "Z", "ACCELZ" -> {
                            accelZ = valNum
                            foundAny = true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }

        return if (foundAny) {
            VitalsData(
                timestamp = System.currentTimeMillis(),
                heartRate = hr.coerceIn(0.0, 240.0),
                spo2 = spo2.coerceIn(0.0, 100.0),
                bodyTemperature = temp.coerceIn(10.0, 50.0),
                ambientTemp = temp.coerceIn(10.0, 50.0),
                humidity = humidity.coerceIn(0.0, 100.0),
                accelX = accelX,
                accelY = accelY,
                accelZ = accelZ,
                signalQualityIndex = if (hr == 0.0) 0.0 else 99.0
            )
        } else null
    }

    private fun parseCsvPayload(text: String): VitalsData? {
        return try {
            val numbers = text.replace(";", ",").split(Regex("[,\\s]+"))
                .mapNotNull { token ->
                    val clean = token.replace("%", "").replace("C", "").replace("hPa", "").trim()
                    clean.toDoubleOrNull()
                }

            if (numbers.isNotEmpty()) {
                val hr = numbers.getOrNull(0) ?: 0.0
                val spo2 = numbers.getOrNull(1) ?: 0.0
                val temp = numbers.getOrNull(2) ?: 25.6
                val humidity = numbers.getOrNull(3) ?: 48.6

                VitalsData(
                    timestamp = System.currentTimeMillis(),
                    heartRate = hr.coerceIn(0.0, 240.0),
                    spo2 = spo2.coerceIn(0.0, 100.0),
                    bodyTemperature = temp.coerceIn(10.0, 50.0),
                    ambientTemp = temp.coerceIn(10.0, 50.0),
                    humidity = humidity.coerceIn(0.0, 100.0),
                    signalQualityIndex = if (hr == 0.0) 0.0 else 99.0
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun parseBinaryPayload(bytes: ByteArray): VitalsData {
        if (bytes.isEmpty()) return VitalsData()

        val hr = (bytes[0].toInt() and 0xFF).toDouble()
        val spo2 = if (bytes.size >= 2) (bytes[1].toInt() and 0xFF).toDouble() else 0.0

        val tempRaw = if (bytes.size >= 4) {
            ((bytes[2].toInt() and 0xFF) or ((bytes[3].toInt() and 0xFF) shl 8))
        } else 0
        val bodyTemp = if (tempRaw > 0) tempRaw / 100.0 else 25.6

        return VitalsData(
            timestamp = System.currentTimeMillis(),
            heartRate = hr.coerceIn(0.0, 240.0),
            spo2 = spo2.coerceIn(0.0, 100.0),
            bodyTemperature = bodyTemp.coerceIn(10.0, 50.0),
            ambientTemp = bodyTemp.coerceIn(10.0, 50.0),
            signalQualityIndex = if (hr == 0.0) 0.0 else 98.5
        )
    }

    private fun parseStandardHeartRate(bytes: ByteArray): VitalsData {
        if (bytes.isEmpty()) return VitalsData()
        val flags = bytes[0].toInt()
        val is16Bit = (flags and 0x01) != 0
        val hr = if (is16Bit && bytes.size >= 3) {
            ((bytes[2].toInt() and 0xFF) shl 8) or (bytes[1].toInt() and 0xFF)
        } else if (bytes.size >= 2) {
            bytes[1].toInt() and 0xFF
        } else 0

        return VitalsData(
            timestamp = System.currentTimeMillis(),
            heartRate = hr.toDouble().coerceIn(0.0, 240.0),
            spo2 = 0.0,
            bodyTemperature = 25.6
        )
    }

    private fun parseHealthThermometer(bytes: ByteArray): VitalsData {
        if (bytes.size < 5) return VitalsData()
        val tempRaw = ((bytes[1].toInt() and 0xFF)) or
                ((bytes[2].toInt() and 0xFF) shl 8) or
                ((bytes[3].toInt() and 0xFF) shl 16)
        val tempFloat = tempRaw / 100.0

        return VitalsData(
            timestamp = System.currentTimeMillis(),
            bodyTemperature = tempFloat.coerceIn(10.0, 50.0),
            ambientTemp = tempFloat.coerceIn(10.0, 50.0)
        )
    }
}
