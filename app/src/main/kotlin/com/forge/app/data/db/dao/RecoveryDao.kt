package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.RecoveryCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecoveryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: RecoveryCheckInEntity): Long

    @Query("SELECT * FROM recovery_checkin ORDER BY recordedAt DESC")
    fun getAllCheckIns(): Flow<List<RecoveryCheckInEntity>>

    @Query("SELECT * FROM recovery_checkin ORDER BY recordedAt DESC LIMIT :limit")
    fun getRecentCheckIns(limit: Int = 14): Flow<List<RecoveryCheckInEntity>>

    @Query("SELECT * FROM recovery_checkin WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getCheckInForDate(dateKey: String): RecoveryCheckInEntity?

    @Query("SELECT * FROM recovery_checkin ORDER BY recordedAt DESC LIMIT 1")
    suspend fun getLatestCheckIn(): RecoveryCheckInEntity?

    @Query("SELECT AVG(energyLevel) FROM recovery_checkin WHERE recordedAt >= :since")
    suspend fun getAvgEnergyLevel(since: Long): Float?

    @Query("SELECT AVG(sleepHours) FROM recovery_checkin WHERE recordedAt >= :since")
    suspend fun getAvgSleepHours(since: Long): Float?
}
