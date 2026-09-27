package com.qualcomm.sih26181.aegishealth.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class VitalsDao_Impl(
  __db: RoomDatabase,
) : VitalsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfVitalsEntity: EntityInsertAdapter<VitalsEntity>

  private val __insertAdapterOfAlertEntity: EntityInsertAdapter<AlertEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfVitalsEntity = object : EntityInsertAdapter<VitalsEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `vitals_history` (`timestamp`,`heartRate`,`spo2`,`bodyTemp`,`accelX`,`accelY`,`accelZ`,`heatIndex`,`aqi`,`riskScore`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: VitalsEntity) {
        statement.bindLong(1, entity.timestamp)
        statement.bindDouble(2, entity.heartRate)
        statement.bindDouble(3, entity.spo2)
        statement.bindDouble(4, entity.bodyTemp)
        statement.bindDouble(5, entity.accelX)
        statement.bindDouble(6, entity.accelY)
        statement.bindDouble(7, entity.accelZ)
        statement.bindDouble(8, entity.heatIndex)
        statement.bindDouble(9, entity.aqi)
        statement.bindLong(10, entity.riskScore.toLong())
      }
    }
    this.__insertAdapterOfAlertEntity = object : EntityInsertAdapter<AlertEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `alerts_history` (`id`,`timestamp`,`severity`,`title`,`message`,`category`,`isResolved`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AlertEntity) {
        statement.bindText(1, entity.id)
        statement.bindLong(2, entity.timestamp)
        statement.bindText(3, entity.severity)
        statement.bindText(4, entity.title)
        statement.bindText(5, entity.message)
        statement.bindText(6, entity.category)
        val _tmp: Int = if (entity.isResolved) 1 else 0
        statement.bindLong(7, _tmp.toLong())
      }
    }
  }

  public override suspend fun insertVitals(vitals: VitalsEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfVitalsEntity.insert(_connection, vitals)
  }

  public override suspend fun insertAlert(alert: AlertEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfAlertEntity.insert(_connection, alert)
  }

  public override fun getRecentVitals(): Flow<List<VitalsEntity>> {
    val _sql: String = "SELECT * FROM vitals_history ORDER BY timestamp DESC LIMIT 100"
    return createFlow(__db, false, arrayOf("vitals_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfHeartRate: Int = getColumnIndexOrThrow(_stmt, "heartRate")
        val _columnIndexOfSpo2: Int = getColumnIndexOrThrow(_stmt, "spo2")
        val _columnIndexOfBodyTemp: Int = getColumnIndexOrThrow(_stmt, "bodyTemp")
        val _columnIndexOfAccelX: Int = getColumnIndexOrThrow(_stmt, "accelX")
        val _columnIndexOfAccelY: Int = getColumnIndexOrThrow(_stmt, "accelY")
        val _columnIndexOfAccelZ: Int = getColumnIndexOrThrow(_stmt, "accelZ")
        val _columnIndexOfHeatIndex: Int = getColumnIndexOrThrow(_stmt, "heatIndex")
        val _columnIndexOfAqi: Int = getColumnIndexOrThrow(_stmt, "aqi")
        val _columnIndexOfRiskScore: Int = getColumnIndexOrThrow(_stmt, "riskScore")
        val _result: MutableList<VitalsEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: VitalsEntity
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpHeartRate: Double
          _tmpHeartRate = _stmt.getDouble(_columnIndexOfHeartRate)
          val _tmpSpo2: Double
          _tmpSpo2 = _stmt.getDouble(_columnIndexOfSpo2)
          val _tmpBodyTemp: Double
          _tmpBodyTemp = _stmt.getDouble(_columnIndexOfBodyTemp)
          val _tmpAccelX: Double
          _tmpAccelX = _stmt.getDouble(_columnIndexOfAccelX)
          val _tmpAccelY: Double
          _tmpAccelY = _stmt.getDouble(_columnIndexOfAccelY)
          val _tmpAccelZ: Double
          _tmpAccelZ = _stmt.getDouble(_columnIndexOfAccelZ)
          val _tmpHeatIndex: Double
          _tmpHeatIndex = _stmt.getDouble(_columnIndexOfHeatIndex)
          val _tmpAqi: Double
          _tmpAqi = _stmt.getDouble(_columnIndexOfAqi)
          val _tmpRiskScore: Int
          _tmpRiskScore = _stmt.getLong(_columnIndexOfRiskScore).toInt()
          _item =
              VitalsEntity(_tmpTimestamp,_tmpHeartRate,_tmpSpo2,_tmpBodyTemp,_tmpAccelX,_tmpAccelY,_tmpAccelZ,_tmpHeatIndex,_tmpAqi,_tmpRiskScore)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllAlerts(): Flow<List<AlertEntity>> {
    val _sql: String = "SELECT * FROM alerts_history ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("alerts_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfSeverity: Int = getColumnIndexOrThrow(_stmt, "severity")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfMessage: Int = getColumnIndexOrThrow(_stmt, "message")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfIsResolved: Int = getColumnIndexOrThrow(_stmt, "isResolved")
        val _result: MutableList<AlertEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AlertEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpSeverity: String
          _tmpSeverity = _stmt.getText(_columnIndexOfSeverity)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpMessage: String
          _tmpMessage = _stmt.getText(_columnIndexOfMessage)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpIsResolved: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsResolved).toInt()
          _tmpIsResolved = _tmp != 0
          _item =
              AlertEntity(_tmpId,_tmpTimestamp,_tmpSeverity,_tmpTitle,_tmpMessage,_tmpCategory,_tmpIsResolved)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearVitals() {
    val _sql: String = "DELETE FROM vitals_history"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearAlerts() {
    val _sql: String = "DELETE FROM alerts_history"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
