package com.forge.app.data.repository

import com.forge.app.data.db.dao.RecoveryDao
import com.forge.app.data.db.entities.RecoveryCheckInEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecoveryRepositoryImpl @Inject constructor(
    private val recoveryDao: RecoveryDao
) : RecoveryRepository {

    override fun getAllCheckIns(): Flow<List<RecoveryCheckInEntity>> =
        recoveryDao.getAllCheckIns()

    override fun getRecentCheckIns(limit: Int): Flow<List<RecoveryCheckInEntity>> =
        recoveryDao.getRecentCheckIns(limit)

    override suspend fun insertCheckIn(checkIn: RecoveryCheckInEntity): Long =
        recoveryDao.insertCheckIn(checkIn)

    override suspend fun getTodayCheckIn(): RecoveryCheckInEntity? {
        val dateKey = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return recoveryDao.getCheckInForDate(dateKey)
    }

    override suspend fun getLatestCheckIn(): RecoveryCheckInEntity? =
        recoveryDao.getLatestCheckIn()

    override suspend fun getAvgEnergyLevel(sinceDays: Int): Float? {
        val since = System.currentTimeMillis() - (sinceDays * 86_400_000L)
        return recoveryDao.getAvgEnergyLevel(since)
    }

    override suspend fun getAvgSleepHours(sinceDays: Int): Float? {
        val since = System.currentTimeMillis() - (sinceDays * 86_400_000L)
        return recoveryDao.getAvgSleepHours(since)
    }
}
