package com.forge.app.data.repository

import com.forge.app.data.db.dao.DailyHydrationSummary
import com.forge.app.data.db.dao.HydrationDao
import com.forge.app.data.db.entities.HydrationLogEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HydrationRepositoryImpl @Inject constructor(
    private val hydrationDao: HydrationDao
) : HydrationRepository {

    override suspend fun logWater(dateKey: String, amountMl: Int): Long {
        return hydrationDao.insertHydration(
            HydrationLogEntity(
                dateKey = dateKey,
                amountMl = amountMl,
                loggedAt = System.currentTimeMillis()
            )
        )
    }

    override fun getLogsForDate(dateKey: String): Flow<List<HydrationLogEntity>> =
        hydrationDao.getLogsForDateFlow(dateKey)

    override fun getTotalForDate(dateKey: String): Flow<Int> =
        hydrationDao.getTotalForDateFlow(dateKey)

    override suspend fun getTotalForDateOnce(dateKey: String): Int =
        hydrationDao.getTotalForDate(dateKey)

    override suspend fun deleteLog(id: Long) = hydrationDao.deleteLogById(id)

    override fun getRecentDailySummaries(limit: Int): Flow<List<DailyHydrationSummary>> =
        hydrationDao.getRecentDailySummariesFlow(limit)
}
