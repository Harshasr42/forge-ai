package com.forge.app.data.repository

import com.forge.app.data.db.dao.DailyHydrationSummary
import com.forge.app.data.db.entities.HydrationLogEntity
import kotlinx.coroutines.flow.Flow

interface HydrationRepository {
    suspend fun logWater(dateKey: String, amountMl: Int): Long
    fun getLogsForDate(dateKey: String): Flow<List<HydrationLogEntity>>
    fun getTotalForDate(dateKey: String): Flow<Int>
    suspend fun getTotalForDateOnce(dateKey: String): Int
    suspend fun deleteLog(id: Long)
    fun getRecentDailySummaries(limit: Int = 14): Flow<List<DailyHydrationSummary>>
}
