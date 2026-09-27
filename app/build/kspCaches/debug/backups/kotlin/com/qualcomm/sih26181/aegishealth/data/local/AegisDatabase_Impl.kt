package com.qualcomm.sih26181.aegishealth.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AegisDatabase_Impl : AegisDatabase() {
  private val _vitalsDao: Lazy<VitalsDao> = lazy {
    VitalsDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "c0b2cc1125680121aa9e1dc4d6978814", "2ccc7eab02bb1fc8504a15eec245da05") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `vitals_history` (`timestamp` INTEGER NOT NULL, `heartRate` REAL NOT NULL, `spo2` REAL NOT NULL, `bodyTemp` REAL NOT NULL, `accelX` REAL NOT NULL, `accelY` REAL NOT NULL, `accelZ` REAL NOT NULL, `heatIndex` REAL NOT NULL, `aqi` REAL NOT NULL, `riskScore` INTEGER NOT NULL, PRIMARY KEY(`timestamp`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `alerts_history` (`id` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `severity` TEXT NOT NULL, `title` TEXT NOT NULL, `message` TEXT NOT NULL, `category` TEXT NOT NULL, `isResolved` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c0b2cc1125680121aa9e1dc4d6978814')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `vitals_history`")
        connection.execSQL("DROP TABLE IF EXISTS `alerts_history`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsVitalsHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsVitalsHistory.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("heartRate", TableInfo.Column("heartRate", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("spo2", TableInfo.Column("spo2", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("bodyTemp", TableInfo.Column("bodyTemp", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("accelX", TableInfo.Column("accelX", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("accelY", TableInfo.Column("accelY", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("accelZ", TableInfo.Column("accelZ", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("heatIndex", TableInfo.Column("heatIndex", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("aqi", TableInfo.Column("aqi", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVitalsHistory.put("riskScore", TableInfo.Column("riskScore", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysVitalsHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesVitalsHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoVitalsHistory: TableInfo = TableInfo("vitals_history", _columnsVitalsHistory,
            _foreignKeysVitalsHistory, _indicesVitalsHistory)
        val _existingVitalsHistory: TableInfo = read(connection, "vitals_history")
        if (!_infoVitalsHistory.equals(_existingVitalsHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |vitals_history(com.qualcomm.sih26181.aegishealth.data.local.VitalsEntity).
              | Expected:
              |""".trimMargin() + _infoVitalsHistory + """
              |
              | Found:
              |""".trimMargin() + _existingVitalsHistory)
        }
        val _columnsAlertsHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAlertsHistory.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAlertsHistory.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlertsHistory.put("severity", TableInfo.Column("severity", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAlertsHistory.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAlertsHistory.put("message", TableInfo.Column("message", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAlertsHistory.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAlertsHistory.put("isResolved", TableInfo.Column("isResolved", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAlertsHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAlertsHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAlertsHistory: TableInfo = TableInfo("alerts_history", _columnsAlertsHistory,
            _foreignKeysAlertsHistory, _indicesAlertsHistory)
        val _existingAlertsHistory: TableInfo = read(connection, "alerts_history")
        if (!_infoAlertsHistory.equals(_existingAlertsHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |alerts_history(com.qualcomm.sih26181.aegishealth.data.local.AlertEntity).
              | Expected:
              |""".trimMargin() + _infoAlertsHistory + """
              |
              | Found:
              |""".trimMargin() + _existingAlertsHistory)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "vitals_history",
        "alerts_history")
  }

  public override fun clearAllTables() {
    super.performClear(false, "vitals_history", "alerts_history")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(VitalsDao::class, VitalsDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun vitalsDao(): VitalsDao = _vitalsDao.value
}
