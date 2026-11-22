package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.HydrationLogEntity
import kotlinx.coroutines.flow.Flow

data class DailyHydrationSummary(
    val dateKey: String,
    val totalAmountMl: Int
)

@Dao
interface HydrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHydration(log: HydrationLogEntity): Long

    @Query("SELECT * FROM hydration_log WHERE dateKey = :dateKey ORDER BY loggedAt DESC")
    fun getLogsForDateFlow(dateKey: String): Flow<List<HydrationLogEntity>>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM hydration_log WHERE dateKey = :dateKey")
    fun getTotalForDateFlow(dateKey: String): Flow<Int>

    @Query("SELECT COALESCE(SUM(amountMl), 0) FROM hydration_log WHERE dateKey = :dateKey")
    suspend fun getTotalForDate(dateKey: String): Int

    @Query("DELETE FROM hydration_log WHERE id = :id")
    suspend fun deleteLogById(id: Long)

    @Query("""
        SELECT dateKey, SUM(amountMl) as totalAmountMl
        FROM hydration_log
        GROUP BY dateKey
        ORDER BY dateKey DESC
        LIMIT :limit
    """)
    fun getRecentDailySummariesFlow(limit: Int = 14): Flow<List<DailyHydrationSummary>>
}
