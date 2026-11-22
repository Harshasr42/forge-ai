package com.forge.app.data.repository

import com.forge.app.data.db.dao.GoalDao
import com.forge.app.data.db.entities.GoalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<GoalEntity>> =
        goalDao.getAllGoals()

    override fun getActiveGoals(): Flow<List<GoalEntity>> =
        goalDao.getActiveGoals()

    override suspend fun insertGoal(goal: GoalEntity): Long =
        goalDao.insertGoal(goal)

    override suspend fun updateGoal(goal: GoalEntity) =
        goalDao.updateGoal(goal)

    override suspend fun deleteGoal(goal: GoalEntity) =
        goalDao.deleteGoal(goal)

    override suspend fun updateGoalProgress(id: Long, value: Float) =
        goalDao.updateGoalProgress(id, value)

    override suspend fun updateGoalStatus(id: Long, status: String) =
        goalDao.updateGoalStatus(id, status)

    override suspend fun getGoalById(id: Long): GoalEntity? =
        goalDao.getGoalById(id)
}
