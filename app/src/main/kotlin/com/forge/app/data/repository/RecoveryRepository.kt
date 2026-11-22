package com.forge.app.data.repository

import com.forge.app.data.db.entities.RecoveryCheckInEntity
import kotlinx.coroutines.flow.Flow

interface RecoveryRepository {
    fun getAllCheckIns(): Flow<List<RecoveryCheckInEntity>>
    fun getRecentCheckIns(limit: Int): Flow<List<RecoveryCheckInEntity>>
    suspend fun insertCheckIn(checkIn: RecoveryCheckInEntity): Long
    suspend fun getTodayCheckIn(): RecoveryCheckInEntity?
    suspend fun getLatestCheckIn(): RecoveryCheckInEntity?
    suspend fun getAvgEnergyLevel(sinceDays: Int): Float?
    suspend fun getAvgSleepHours(sinceDays: Int): Float?
}
