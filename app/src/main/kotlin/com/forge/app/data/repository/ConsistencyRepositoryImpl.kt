package com.forge.app.data.repository

import com.forge.app.data.db.dao.ConsistencyDao
import com.forge.app.data.db.entities.AchievementEntity
import com.forge.app.data.db.entities.ConsistencyEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsistencyRepositoryImpl @Inject constructor(
    private val consistencyDao: ConsistencyDao
) : ConsistencyRepository {
    override fun getConsistency(): Flow<ConsistencyEntity?> = consistencyDao.getConsistency()
    override suspend fun getConsistencyOnce(): ConsistencyEntity? = consistencyDao.getConsistencyOnce()
    override suspend fun updateConsistency(consistency: ConsistencyEntity) = consistencyDao.upsertConsistency(consistency)

    override suspend fun recordWorkoutCompleted() {
        val current = consistencyDao.getConsistencyOnce() ?: ConsistencyEntity(id = 1L)
        val now = System.currentTimeMillis()

        val lastCal = current.lastStreakDate?.let {
            Calendar.getInstance().apply { timeInMillis = it }
        }
        val nowCal = Calendar.getInstance().apply { timeInMillis = now }

        val isSameDay = lastCal != null &&
                nowCal.get(Calendar.YEAR) == lastCal.get(Calendar.YEAR) &&
                nowCal.get(Calendar.DAY_OF_YEAR) == lastCal.get(Calendar.DAY_OF_YEAR)

        val isConsecutiveDay = lastCal != null && run {
            val daysDiff = if (nowCal.get(Calendar.YEAR) == lastCal.get(Calendar.YEAR)) {
                nowCal.get(Calendar.DAY_OF_YEAR) - lastCal.get(Calendar.DAY_OF_YEAR)
            } else if (nowCal.get(Calendar.YEAR) - lastCal.get(Calendar.YEAR) == 1) {
                nowCal.get(Calendar.DAY_OF_YEAR) + (lastCal.getActualMaximum(Calendar.DAY_OF_YEAR) - lastCal.get(Calendar.DAY_OF_YEAR))
            } else {
                999
            }
            daysDiff == 1
        }

        val newStreak = when {
            isSameDay -> current.currentStreak
            isConsecutiveDay -> current.currentStreak + 1
            else -> 1
        }

        val newLongest = maxOf(newStreak, current.longestStreak)
        val newXp = current.totalXp + 100 // 100 XP per workout
        val newLevel = calculateLevel(newXp)
        val newTitle = calculateLevelTitle(newLevel)

        val updated = current.copy(
            currentStreak = newStreak,
            longestStreak = newLongest,
            totalWorkoutDays = if (isSameDay) current.totalWorkoutDays else current.totalWorkoutDays + 1,
            totalXp = newXp,
            level = newLevel,
            levelTitle = newTitle,
            lastWorkoutDate = now,
            lastStreakDate = now
        )
        consistencyDao.upsertConsistency(updated)

        // Check achievements
        if (updated.totalWorkoutDays >= 1) {
            consistencyDao.unlockAchievement("FIRST_WORKOUT")
        }
        if (updated.currentStreak >= 3) {
            consistencyDao.unlockAchievement("STREAK_3")
        }
        if (updated.currentStreak >= 7) {
            consistencyDao.unlockAchievement("STREAK_7")
        }
    }

    override fun getAllAchievements(): Flow<List<AchievementEntity>> = consistencyDao.getAllAchievements()
    override suspend fun unlockAchievement(achievementId: String) = consistencyDao.unlockAchievement(achievementId)

    private fun calculateLevel(xp: Int): Int = when {
        xp >= 25000 -> 7 // Forged
        xp >= 13000 -> 6 // Elite
        xp >= 7000  -> 5 // Athlete
        xp >= 3500  -> 4 // Warrior
        xp >= 1500  -> 3 // Disciplined
        xp >= 500   -> 2 // Apprentice
        else        -> 1 // Beginner
    }

    private fun calculateLevelTitle(level: Int): String = when (level) {
        7 -> "Forged"
        6 -> "Elite"
        5 -> "Athlete"
        4 -> "Warrior"
        3 -> "Disciplined"
        2 -> "Apprentice"
        else -> "Beginner"
    }
}
