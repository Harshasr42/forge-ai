package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsistencyDao {
    @Query("SELECT * FROM consistency WHERE id = 1")
    fun getConsistency(): Flow<ConsistencyEntity?>

    @Query("SELECT * FROM consistency WHERE id = 1")
    suspend fun getConsistencyOnce(): ConsistencyEntity?

    @Upsert
    suspend fun upsertConsistency(consistency: ConsistencyEntity)

    @Query("SELECT * FROM achievement ORDER BY isUnlocked DESC, xpReward DESC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Query("UPDATE achievement SET isUnlocked = 1, unlockedAt = :unlockedAt WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: String, unlockedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM achievement WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedAchievements(): Flow<List<AchievementEntity>>
}
