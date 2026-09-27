package com.qualcomm.sih26181.aegishealth.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "vitals_history")
data class VitalsEntity(
    @PrimaryKey val timestamp: Long,
    val heartRate: Double,
    val spo2: Double,
    val bodyTemp: Double,
    val accelX: Double,
    val accelY: Double,
    val accelZ: Double,
    val heatIndex: Double,
    val aqi: Double,
    val riskScore: Int
)

@Entity(tableName = "alerts_history")
data class AlertEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val severity: String,
    val title: String,
    val message: String,
    val category: String,
    val isResolved: Boolean
)

@Dao
interface VitalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVitals(vitals: VitalsEntity)

    @Query("SELECT * FROM vitals_history ORDER BY timestamp DESC LIMIT 100")
    fun getRecentVitals(): Flow<List<VitalsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("SELECT * FROM alerts_history ORDER BY timestamp DESC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Query("DELETE FROM vitals_history")
    suspend fun clearVitals()

    @Query("DELETE FROM alerts_history")
    suspend fun clearAlerts()
}

@Database(entities = [VitalsEntity::class, AlertEntity::class], version = 1, exportSchema = false)
abstract class AegisDatabase : RoomDatabase() {
    abstract fun vitalsDao(): VitalsDao
}
