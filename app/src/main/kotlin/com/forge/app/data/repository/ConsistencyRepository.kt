package com.forge.app.data.repository

import com.forge.app.data.db.entities.AchievementEntity
import com.forge.app.data.db.entities.ConsistencyEntity
import kotlinx.coroutines.flow.Flow

interface ConsistencyRepository {
    fun getConsistency(): Flow<ConsistencyEntity?>
    suspend fun getConsistencyOnce(): ConsistencyEntity?
    suspend fun updateConsistency(consistency: ConsistencyEntity)
    suspend fun recordWorkoutCompleted()
    fun getAllAchievements(): Flow<List<AchievementEntity>>
    suspend fun unlockAchievement(achievementId: String)
}
