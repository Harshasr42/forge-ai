package com.forge.app

import com.forge.app.data.db.entities.GoalEntity
import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.data.db.entities.RecoveryCheckInEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import com.forge.app.domain.CoachEngine
import com.forge.app.domain.InsightCategory
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class CoachEngineTest {

    @Test
    fun testNoWorkoutsInPastWeekTriggersWarning() {
        val insights = CoachEngine.generateInsights(
            recentSessions = emptyList(),
            personalRecords = emptyList(),
            activeGoals = emptyList(),
            recentCheckIns = emptyList(),
            currentStreakDays = 0
        )

        val warning = insights.find { it.id == "no_workouts_7d" }
        assertNotNull("Should generate warning for no workouts in past 7 days", warning)
        assertEquals(InsightCategory.WARNING, warning?.category)
    }

    @Test
    fun testHighFrequencyWorkoutTriggersPositiveInsight() {
        val now = System.currentTimeMillis()
        val sessions = (1..6).map { i ->
            WorkoutSessionEntity(
                id = i.toLong(),
                workoutName = "Session $i",
                startedAt = now - TimeUnit.DAYS.toMillis(i.toLong()),
                completedAt = now - TimeUnit.DAYS.toMillis(i.toLong()) + 3600000,
                status = "COMPLETED",
                totalVolume = 8000f,
                totalSets = 18
            )
        }

        val insights = CoachEngine.generateInsights(
            recentSessions = sessions,
            personalRecords = emptyList(),
            activeGoals = emptyList(),
            recentCheckIns = emptyList(),
            currentStreakDays = 6
        )

        val positive = insights.find { it.id == "great_frequency" }
        assertNotNull("Should recognize high training frequency", positive)
        assertEquals(InsightCategory.POSITIVE, positive?.category)
    }

    @Test
    fun testHighFatigueCheckInTriggersRecoveryObservation() {
        val checkIns = (1..5).map {
            RecoveryCheckInEntity(
                id = it.toLong(),
                sleepHours = 6f,
                sleepQuality = 2,
                energyLevel = 2,
                muscleSoreness = 4,
                fatigue = 4, // high fatigue
                motivation = 2
            )
        }

        val insights = CoachEngine.generateInsights(
            recentSessions = emptyList(),
            personalRecords = emptyList(),
            activeGoals = emptyList(),
            recentCheckIns = checkIns,
            currentStreakDays = 2
        )

        val fatigueInsight = insights.find { it.id == "high_fatigue" }
        assertNotNull("Should flag elevated fatigue", fatigueInsight)
        assertEquals(InsightCategory.RECOVERY, fatigueInsight?.category)
    }

    @Test
    fun testRecentPersonalRecordGeneratesProgressInsight() {
        val now = System.currentTimeMillis()
        val prs = listOf(
            PersonalRecordEntity(
                id = 1L,
                exerciseId = 1L,
                exerciseName = "DB Overhead Press",
                weightKg = 32f,
                reps = 8,
                achievedAt = now - TimeUnit.DAYS.toMillis(2)
            )
        )

        val insights = CoachEngine.generateInsights(
            recentSessions = emptyList(),
            personalRecords = prs,
            activeGoals = emptyList(),
            recentCheckIns = emptyList(),
            currentStreakDays = 1
        )

        val prInsight = insights.find { it.id == "recent_prs" }
        assertNotNull("Should generate PR insight", prInsight)
        assertTrue(prInsight!!.body.contains("DB Overhead Press"))
    }

    @Test
    fun testWeeklyReportCalculatesVolumeChangeAndAverages() {
        val now = System.currentTimeMillis()
        val thisWeekSessions = listOf(
            WorkoutSessionEntity(id = 1L, workoutName = "A", totalVolume = 10000f, startedAt = now - 100000),
            WorkoutSessionEntity(id = 2L, workoutName = "B", totalVolume = 12000f, startedAt = now - 200000)
        )
        val lastWeekSessions = listOf(
            WorkoutSessionEntity(id = 3L, workoutName = "A", totalVolume = 10000f, startedAt = now - 800000000)
        )
        val checkIns = listOf(
            RecoveryCheckInEntity(id = 1L, sleepHours = 8f, energyLevel = 4),
            RecoveryCheckInEntity(id = 2L, sleepHours = 7f, energyLevel = 4)
        )

        val report = CoachEngine.generateWeeklyReport(
            sessionsThisWeek = thisWeekSessions,
            sessionsLastWeek = lastWeekSessions,
            personalRecords = emptyList(),
            activeGoals = emptyList(),
            checkInsThisWeek = checkIns,
            targetSessionsPerWeek = 6
        )

        assertEquals(2, report.sessionsCompleted)
        assertEquals(22000f, report.totalVolumeKg, 0.01f)
        assertEquals(120f, report.volumeChangePercent, 0.1f) // (22000 - 10000) / 10000 * 100 = 120%
        assertEquals(7.5f, report.avgSleepHours!!, 0.01f)
        assertEquals(4.0f, report.avgEnergyLevel!!, 0.01f)
        assertNotNull(report.focusSuggestion)
    }
}
