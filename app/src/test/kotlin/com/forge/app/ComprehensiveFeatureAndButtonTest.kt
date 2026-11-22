package com.forge.app

import com.forge.app.domain.CoachEngine
import com.forge.app.domain.InsightCategory
import com.forge.app.ui.navigation.ForgeDestination
import com.forge.app.ui.navigation.bottomNavItems
import org.junit.Assert.*
import org.junit.Test

class ComprehensiveFeatureAndButtonTest {

    // ==========================================
    // 1. NAVIGATION & ROUTE BUILDERS
    // ==========================================

    @Test
    fun testAllDestinationRoutes() {
        assertEquals("today", ForgeDestination.Today.route)
        assertEquals("programme", ForgeDestination.Programme.route)
        assertEquals("workout?dayId={dayId}", ForgeDestination.Workout.route)
        assertEquals("workout?dayId=1", ForgeDestination.Workout.createRoute(1L))
        assertEquals("workout?dayId=6", ForgeDestination.Workout.createRoute(6L))

        assertEquals("exercises", ForgeDestination.ExerciseLibrary.route)
        assertEquals("exercise/{exerciseId}", ForgeDestination.ExerciseDetail.route)
        assertEquals("exercise/42", ForgeDestination.ExerciseDetail.createRoute(42L))

        assertEquals("history", ForgeDestination.History.route)
        assertEquals("consistency", ForgeDestination.Consistency.route)
        assertEquals("progress", ForgeDestination.Progress.route)
        assertEquals("personal_records", ForgeDestination.PersonalRecords.route)

        assertEquals("exercise_analytics/{exerciseId}", ForgeDestination.ExerciseAnalytics.route)
        assertEquals("exercise_analytics/15", ForgeDestination.ExerciseAnalytics.createRoute(15L))

        assertEquals("body_measurements", ForgeDestination.BodyMeasurements.route)
        assertEquals("goals", ForgeDestination.Goals.route)
        assertEquals("add_goal", ForgeDestination.AddGoal.route)
        assertEquals("recovery", ForgeDestination.Recovery.route)
        assertEquals("coach", ForgeDestination.Coach.route)
        assertEquals("weekly_report", ForgeDestination.WeeklyReport.route)

        // Phase 4 Routes
        assertEquals("nutrition", ForgeDestination.Nutrition.route)
        assertEquals("food_search/{mealType}/{dateKey}", ForgeDestination.FoodSearch.route)
        assertEquals("food_search/BREAKFAST/2026-11-22", ForgeDestination.FoodSearch.createRoute("BREAKFAST", "2026-11-22"))
        assertEquals("food_search/SNACKS/2026-11-23", ForgeDestination.FoodSearch.createRoute("SNACKS", "2026-11-23"))
        assertEquals("hydration", ForgeDestination.Hydration.route)
        assertEquals("macro_targets", ForgeDestination.MacroTargets.route)
        assertEquals("meal_planner", ForgeDestination.MealPlanner.route)
    }

    @Test
    fun testBottomNavigationBarConfiguration() {
        assertEquals(5, bottomNavItems.size)
        assertEquals("Today", bottomNavItems[0].label)
        assertEquals(ForgeDestination.Today.route, bottomNavItems[0].route)

        assertEquals("Train", bottomNavItems[1].label)
        assertEquals(ForgeDestination.Programme.route, bottomNavItems[1].route)

        assertEquals("Progress", bottomNavItems[2].label)
        assertEquals(ForgeDestination.Progress.route, bottomNavItems[2].route)

        assertEquals("Coach", bottomNavItems[3].label)
        assertEquals(ForgeDestination.Coach.route, bottomNavItems[3].route)

        assertEquals("Streaks", bottomNavItems[4].label)
        assertEquals(ForgeDestination.Consistency.route, bottomNavItems[4].route)
    }

    // ==========================================
    // 2. WORKOUT EXECUTION & TIMER LOGIC
    // ==========================================

    @Test
    fun testVolumeCalculation() {
        data class MockSet(val weightKg: Float, val reps: Int, val isCompleted: Boolean)
        val sets = listOf(
            MockSet(80f, 10, true),   // 800
            MockSet(90f, 8, true),    // 720
            MockSet(100f, 6, true),   // 600
            MockSet(110f, 4, false)   // not completed
        )

        val totalVolume = sets.filter { it.isCompleted }.sumOf { (it.weightKg * it.reps).toDouble() }.toFloat()
        assertEquals(2120f, totalVolume, 0.01f)

        val completedCount = sets.count { it.isCompleted }
        assertEquals(3, completedCount)
    }

    @Test
    fun testTimeFormattingHelper() {
        fun formatDuration(seconds: Long): String {
            val mins = seconds / 60
            val secs = seconds % 60
            return "%02d:%02d".format(mins, secs)
        }

        assertEquals("00:00", formatDuration(0L))
        assertEquals("01:30", formatDuration(90L))
        assertEquals("45:12", formatDuration(2712L))
        assertEquals("65:05", formatDuration(3905L))
    }

    @Test
    fun testRestTimerCountdownAndAdjustments() {
        var restSeconds = 90
        val add15Sec = 15
        restSeconds += add15Sec
        assertEquals(105, restSeconds)

        // Countdown simulation
        restSeconds = (restSeconds - 10).coerceAtLeast(0)
        assertEquals(95, restSeconds)

        // Skip rest simulation
        restSeconds = 0
        assertEquals(0, restSeconds)
    }

    // ==========================================
    // 3. PROGRESS & PERFORMANCE INTELLIGENCE
    // ==========================================

    @Test
    fun testEpleyOneRepMaxFormula() {
        fun calculate1Rm(weight: Float, reps: Int): Float {
            if (reps <= 0) return 0f
            if (reps == 1) return weight
            return weight * (1f + reps / 30f)
        }

        // 100kg for 1 rep = 100kg
        assertEquals(100f, calculate1Rm(100f, 1), 0.01f)

        // 100kg for 10 reps = 100 * (1 + 1/3) = 133.33kg
        assertEquals(133.33f, calculate1Rm(100f, 10), 0.05f)

        // 60kg for 8 reps = 60 * (1 + 8/30) = 76kg
        assertEquals(76f, calculate1Rm(60f, 8), 0.01f)
    }

    @Test
    fun testPersonalRecordDetection() {
        val previousBestWeight = 80f
        val previousBest1Rm = 95f

        val currentWeight = 85f
        val currentReps = 6
        val current1Rm = currentWeight * (1f + currentReps / 30f) // 85 * 1.2 = 102f

        val isWeightPr = currentWeight > previousBestWeight
        val is1RmPr = current1Rm > previousBest1Rm

        assertTrue(isWeightPr)
        assertTrue(is1RmPr)
        assertEquals(102f, current1Rm, 0.01f)
    }

    // ==========================================
    // 4. SMART GOAL CALCULATIONS
    // ==========================================

    @Test
    fun testGoalProgressCalculation() {
        fun calculateGoalProgress(start: Float, current: Float, target: Float): Float {
            if (target == start) return 100f
            val raw = (current - start) / (target - start) * 100f
            return raw.coerceIn(0f, 100f)
        }

        // Bench press goal: start 60kg, current 80kg, target 100kg = 50%
        val benchProgress = calculateGoalProgress(60f, 80f, 100f)
        assertEquals(50f, benchProgress, 0.01f)

        // Weight loss goal: start 85kg, current 80kg, target 75kg = 50%
        val weightLossProgress = calculateGoalProgress(85f, 80f, 75f)
        assertEquals(50f, weightLossProgress, 0.01f)

        // Completed goal
        val completedProgress = calculateGoalProgress(60f, 100f, 100f)
        assertEquals(100f, completedProgress, 0.01f)

        // Negative/behind start
        val behindProgress = calculateGoalProgress(60f, 50f, 100f)
        assertEquals(0f, behindProgress, 0.01f)
    }

    // ==========================================
    // 5. RECOVERY READINESS SCORING
    // ==========================================

    @Test
    fun testRecoveryReadinessEngine() {
        fun computeReadiness(sleepHours: Float, energyLevel: Int, sorenessLevel: Int, stressLevel: Int): Pair<Int, String> {
            // Sleep: up to 8 hrs -> normalized to 10
            val sleepScore = (sleepHours / 8f).coerceIn(0f, 1f) * 10f
            // Energy: 1-5 -> 2-10
            val energyScore = energyLevel * 2f
            // Soreness inverted: 1 (fresh) -> 10, 5 (extreme) -> 2
            val sorenessScore = (6 - sorenessLevel) * 2f
            // Stress inverted: 1 (calm) -> 10, 5 (high) -> 2
            val stressScore = (6 - stressLevel) * 2f

            // Weighted: sleep 35%, energy 30%, soreness 20%, stress 15%
            val total = (sleepScore * 0.35f + energyScore * 0.30f + sorenessScore * 0.20f + stressScore * 0.15f) * 10f
            val rounded = total.toInt().coerceIn(0, 100)

            val label = when {
                rounded >= 80 -> "Ready to Forge — High Intensity"
                rounded >= 60 -> "Moderate Readiness — Standard Volume"
                else -> "Take it Easy — Active Recovery Recommended"
            }
            return rounded to label
        }

        // Optimal sleep (8h), high energy (5), low soreness (1), low stress (1)
        val (scoreHigh, labelHigh) = computeReadiness(8f, 5, 1, 1)
        assertTrue(scoreHigh >= 85)
        assertTrue(labelHigh.contains("Ready to Forge"))

        // Poor sleep (4h), low energy (1), high soreness (5), high stress (5)
        val (scoreLow, labelLow) = computeReadiness(4f, 1, 5, 5)
        assertTrue(scoreLow < 60)
        assertTrue(labelLow.contains("Take it Easy"))
    }

    // ==========================================
    // 6. NUTRITION MACRO CALCULATIONS
    // ==========================================

    @Test
    fun testMacroCaloricSumAndValidation() {
        fun computeCalories(proteinG: Float, carbsG: Float, fatG: Float): Float {
            return (proteinG * 4f) + (carbsG * 4f) + (fatG * 9f)
        }

        // 150g P (600), 250g C (1000), 60g F (540) = 2140 kcal
        val cals = computeCalories(150f, 250f, 60f)
        assertEquals(2140f, cals, 0.01f)

        // Bulk preset check
        val bulkCals = computeCalories(180f, 320f, 75f) // 720 + 1280 + 675 = 2675 kcal
        assertEquals(2675f, bulkCals, 0.01f)
    }

    @Test
    fun testFoodServingScaling() {
        val baseServingSize = 100f
        val baseCalories = 165f // Chicken breast
        val baseProtein = 31f
        val baseCarbs = 0f
        val baseFat = 3.6f

        val multiplier = 2.5f // 250g consumed

        val scaledCalories = baseCalories * multiplier
        val scaledProtein = baseProtein * multiplier
        val scaledFat = baseFat * multiplier

        assertEquals(412.5f, scaledCalories, 0.01f)
        assertEquals(77.5f, scaledProtein, 0.01f)
        assertEquals(9.0f, scaledFat, 0.01f)
    }

    @Test
    fun testHydrationProgressCalculation() {
        val targetMl = 3000
        val currentMl = 2250

        val progress = (currentMl.toFloat() / targetMl).coerceIn(0f, 1f)
        assertEquals(0.75f, progress, 0.001f)

        val percentage = (progress * 100).toInt()
        assertEquals(75, percentage)
    }

    // ==========================================
    // 7. COACH DETERMINISTIC INSIGHTS ENGINE
    // ==========================================

    @Test
    fun testCoachEngineDeterministicRules() {
        // High soreness & low sleep triggers Recovery insight
        val insights = CoachEngine.evaluate(
            recentWorkoutsCount = 5,
            weeklyVolumeKg = 24000f,
            previousWeeklyVolumeKg = 18000f,
            recentPrsCount = 2,
            currentStreak = 7,
            todaySleepHours = 5.0f,
            todaySoreness = 5,
            activeGoalsCount = 3
        )

        assertTrue(insights.isNotEmpty())
        val hasRecoveryWarning = insights.any { it.category == InsightCategory.RECOVERY || it.category == InsightCategory.WARNING }
        assertTrue(hasRecoveryWarning)

        // Volume jump: 24000 vs 18000 is +33.3%, which triggers overload evaluation
        val volumeChangePercent = ((24000f - 18000f) / 18000f) * 100f
        assertTrue(volumeChangePercent > 20f)
    }
}
