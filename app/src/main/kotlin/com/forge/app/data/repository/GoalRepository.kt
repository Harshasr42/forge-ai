package com.forge.app.data.repository

import com.forge.app.data.db.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getAllGoals(): Flow<List<GoalEntity>>
    fun getActiveGoals(): Flow<List<GoalEntity>>
    suspend fun insertGoal(goal: GoalEntity): Long
    suspend fun updateGoal(goal: GoalEntity)
    suspend fun deleteGoal(goal: GoalEntity)
    suspend fun updateGoalProgress(id: Long, value: Float)
    suspend fun updateGoalStatus(id: Long, status: String)
    suspend fun getGoalById(id: Long): GoalEntity?
}
