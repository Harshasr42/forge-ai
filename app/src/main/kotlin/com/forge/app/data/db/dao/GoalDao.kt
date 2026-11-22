package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("SELECT * FROM goal ORDER BY createdAt DESC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goal WHERE status = 'ACTIVE' ORDER BY createdAt DESC")
    fun getActiveGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goal WHERE id = :id")
    suspend fun getGoalById(id: Long): GoalEntity?

    @Query("UPDATE goal SET currentValue = :value, status = CASE WHEN :value >= targetValue THEN 'COMPLETED' ELSE status END, completedAt = CASE WHEN :value >= targetValue AND completedAt IS NULL THEN :now ELSE completedAt END WHERE id = :id")
    suspend fun updateGoalProgress(id: Long, value: Float, now: Long = System.currentTimeMillis())

    @Query("UPDATE goal SET status = :status WHERE id = :id")
    suspend fun updateGoalStatus(id: Long, status: String)
}
