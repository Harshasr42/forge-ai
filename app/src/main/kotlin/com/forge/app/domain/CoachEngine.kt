package com.forge.app.domain

import com.forge.app.data.db.entities.GoalEntity
import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.data.db.entities.RecoveryCheckInEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import java.util.concurrent.TimeUnit

data class CoachInsight(
    val id: String,
    val category: InsightCategory,
    val title: String,
    val body: String,
    val priority: Int = 5,
    val actionLabel: String? = null,
    val actionRoute: String? = null
)

enum class InsightCategory {
    PROGRESS, CONSISTENCY, RECOVERY, GOALS, OVERLOAD, WARNING, POSITIVE
}

object CoachEngine {

    fun generateInsights(
        recentSessions: List<WorkoutSessionEntity>,
        personalRecords: List<PersonalRecordEntity>,
        activeGoals: List<GoalEntity>,
        recentCheckIns: List<RecoveryCheckInEntity>,
        currentStreakDays: Int
    ): List<CoachInsight> {
        val insights = mutableListOf<CoachInsight>()

        // Rule 1: Consistency — workout frequency
        val sessionsLast7 = recentSessions.count {
            it.startedAt > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        }
        when {
            sessionsLast7 == 0 -> insights += CoachInsight(
                id = "no_workouts_7d", category = InsightCategory.WARNING,
                title = "No workouts this week",
                body = "You haven't logged any workouts in the last 7 days. Getting back on track with even one session helps maintain your progress.",
                priority = 2, actionLabel = "View Today", actionRoute = "today"
            )
            sessionsLast7 >= 5 -> insights += CoachInsight(
                id = "great_frequency", category = InsightCategory.POSITIVE,
                title = "Excellent training frequency",
                body = "You've completed $sessionsLast7 workouts this week. Consistent training is the most reliable path to improvement.",
                priority = 7
            )
            sessionsLast7 in 1..2 -> insights += CoachInsight(
                id = "low_frequency", category = InsightCategory.CONSISTENCY,
                title = "Below target training frequency",
                body = "You've completed $sessionsLast7 workout(s) this week. Your programme targets 6 sessions. Even 3–4 sessions maintains momentum.",
                priority = 4
            )
        }

        // Rule 2: Streak recognition
        if (currentStreakDays >= 7) {
            insights += CoachInsight(
                id = "streak_$currentStreakDays", category = InsightCategory.POSITIVE,
                title = "$currentStreakDays-day training streak",
                body = "You've maintained a $currentStreakDays-day active streak. Consistency compounds — keep going.",
                priority = 8
            )
        }

        // Rule 3: Personal records
        val recentPrs = personalRecords.filter {
            it.achievedAt > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14)
        }
        if (recentPrs.isNotEmpty()) {
            val prNames = recentPrs.take(3).joinToString(", ") { it.exerciseName }
            insights += CoachInsight(
                id = "recent_prs", category = InsightCategory.PROGRESS,
                title = "${recentPrs.size} new personal record(s)",
                body = "You've set new records in the last 14 days: $prNames. This signals real strength progression.",
                priority = 3, actionLabel = "View Records", actionRoute = "personal_records"
            )
        }

        // Rule 4: Recovery warning
        if (recentCheckIns.isNotEmpty()) {
            val avgFatigue = recentCheckIns.take(5).map { it.fatigue }.average()
            val avgSoreness = recentCheckIns.take(5).map { it.muscleSoreness }.average()
            val avgSleep = recentCheckIns.take(5).map { it.sleepHours }.average()

            if (avgFatigue >= 3.5) {
                insights += CoachInsight(
                    id = "high_fatigue", category = InsightCategory.RECOVERY,
                    title = "Elevated fatigue detected",
                    body = "Your last 5 check-ins show an average fatigue of ${"%.1f".format(avgFatigue)}/5. Consider including a lighter training day or additional rest. This is based on your self-reported data.",
                    priority = 2, actionLabel = "Log Recovery", actionRoute = "recovery"
                )
            }
            if (avgSleep < 6.5) {
                insights += CoachInsight(
                    id = "low_sleep", category = InsightCategory.RECOVERY,
                    title = "Sleep below optimal",
                    body = "Your average sleep over the last 5 check-ins is ${"%.1f".format(avgSleep)} hours. Research links sleep duration to recovery and performance. This observation is based on your logged data only.",
                    priority = 3
                )
            }
        }

        // Rule 5: Goal progress
        for (goal in activeGoals) {
            val progress = if (goal.targetValue > goal.startValue) {
                ((goal.currentValue - goal.startValue) / (goal.targetValue - goal.startValue)).coerceIn(0f, 1f)
            } else 0f
            if (progress > 0.8f) {
                insights += CoachInsight(
                    id = "goal_near_${goal.id}", category = InsightCategory.GOALS,
                    title = "Goal nearly complete: ${goal.title}",
                    body = "You're ${"%.0f".format(progress * 100)}% of the way to your goal (${goal.currentValue} ${goal.unit} of ${goal.targetValue} ${goal.unit}). Keep pushing.",
                    priority = 3, actionLabel = "View Goals", actionRoute = "goals"
                )
            }
            if (goal.targetDate != null && goal.targetDate < System.currentTimeMillis() && goal.status == "ACTIVE") {
                insights += CoachInsight(
                    id = "goal_overdue_${goal.id}", category = InsightCategory.WARNING,
                    title = "Goal target date passed: ${goal.title}",
                    body = "The target date for '${goal.title}' has passed at ${"%.0f".format(progress * 100)}% completion. Consider updating the target date or adjusting the goal.",
                    priority = 2, actionLabel = "View Goals", actionRoute = "goals"
                )
            }
        }

        // Rule 6: Volume trend (deload suggestion)
        val last4WeeksVolume = recentSessions.sumOf { it.totalVolume.toDouble() }.toFloat()
        if (recentSessions.size >= 12 && sessionsLast7 >= 5 && last4WeeksVolume > 50000f) {
            insights += CoachInsight(
                id = "high_volume", category = InsightCategory.OVERLOAD,
                title = "High training volume this month",
                body = "You've lifted ${"%.0f".format(last4WeeksVolume / 1000f)} tonnes this period across ${recentSessions.size} sessions. If performance or recovery is suffering, a planned lighter week can improve long-term progression. This is an observation, not a prescription.",
                priority = 6
            )
        }

        // Rule 7: No check-ins recently
        if (recentCheckIns.isEmpty()) {
            insights += CoachInsight(
                id = "no_checkins", category = InsightCategory.RECOVERY,
                title = "No recovery check-ins logged",
                body = "Track your sleep and energy levels daily to help identify patterns between recovery and performance.",
                priority = 7, actionLabel = "Log Recovery", actionRoute = "recovery"
            )
        }

        return insights.sortedBy { it.priority }
    }

    fun generateWeeklyReport(
        sessionsThisWeek: List<WorkoutSessionEntity>,
        sessionsLastWeek: List<WorkoutSessionEntity>,
        personalRecords: List<PersonalRecordEntity>,
        activeGoals: List<GoalEntity>,
        checkInsThisWeek: List<RecoveryCheckInEntity>,
        targetSessionsPerWeek: Int = 6
    ): WeeklyReportData {
        val volumeThisWeek = sessionsThisWeek.sumOf { it.totalVolume.toDouble() }.toFloat()
        val volumeLastWeek = sessionsLastWeek.sumOf { it.totalVolume.toDouble() }.toFloat()
        val volumeChange = if (volumeLastWeek > 0f) {
            ((volumeThisWeek - volumeLastWeek) / volumeLastWeek * 100f)
        } else 0f

        val newPrs = personalRecords.filter {
            it.achievedAt > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        }

        val avgSleep = if (checkInsThisWeek.isNotEmpty()) {
            checkInsThisWeek.map { it.sleepHours }.average().toFloat()
        } else null
        val avgEnergy = if (checkInsThisWeek.isNotEmpty()) {
            checkInsThisWeek.map { it.energyLevel }.average().toFloat()
        } else null

        val focusSuggestion = when {
            sessionsThisWeek.size < targetSessionsPerWeek / 2 ->
                "Focus on rebuilding training frequency next week. Even shorter sessions count."
            newPrs.isNotEmpty() ->
                "Capitalise on your new records — push progression in the same exercises next week."
            volumeChange < -20f ->
                "Volume dropped significantly. If intentional, great. If not, review your schedule."
            avgEnergy != null && avgEnergy < 2.5f ->
                "Your energy was low this week. Prioritise sleep and nutrition before next week."
            else ->
                "Solid week. Maintain the standard and look for one exercise to push harder."
        }

        return WeeklyReportData(
            sessionsCompleted = sessionsThisWeek.size,
            sessionsTargeted = targetSessionsPerWeek,
            totalVolumeKg = volumeThisWeek,
            volumeChangePercent = volumeChange,
            newPrs = newPrs,
            activeGoals = activeGoals,
            avgSleepHours = avgSleep,
            avgEnergyLevel = avgEnergy,
            focusSuggestion = focusSuggestion
        )
    }
}

data class WeeklyReportData(
    val sessionsCompleted: Int,
    val sessionsTargeted: Int,
    val totalVolumeKg: Float,
    val volumeChangePercent: Float,
    val newPrs: List<PersonalRecordEntity>,
    val activeGoals: List<GoalEntity>,
    val avgSleepHours: Float?,
    val avgEnergyLevel: Float?,
    val focusSuggestion: String
)
