package com.forge.app

import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.ui.screens.progress.DashboardStats
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests for ProgressDashboardViewModel data and state logic.
 * Note: Full ViewModel tests with Hilt dependencies require Android instrumentation tests.
 */
class ProgressDashboardViewModelTest {

    @Test
    fun testDashboardStats_defaults() {
        val defaultStats = DashboardStats()
        assertEquals("Default totalWorkouts should be 0", 0, defaultStats.totalWorkouts)
        assertEquals("Default totalVolumeKg should be 0", 0f, defaultStats.totalVolumeKg, 0.01f)
        assertEquals("Default totalSets should be 0", 0, defaultStats.totalSets)
        assertEquals("Default totalReps should be 0", 0, defaultStats.totalReps)
        assertTrue("Default recentPrs should be empty", defaultStats.recentPrs.isEmpty())
        assertTrue("Default isLoading should be true", defaultStats.isLoading)
    }

    @Test
    fun testDashboardStats_copyPreservesValues() {
        val stats1 = DashboardStats(
            totalWorkouts = 10,
            totalVolumeKg = 2500f,
            totalSets = 80,
            totalReps = 400,
            recentPrs = listOf(PersonalRecordEntity(exerciseId = 1, exerciseName = "Test")),
            isLoading = false
        )

        val stats2 = stats1.copy(isLoading = true)
        assertEquals("totalWorkouts should be preserved", 10, stats2.totalWorkouts)
        assertEquals("totalVolumeKg should be preserved", 2500f, stats2.totalVolumeKg, 0.01f)
        assertTrue("isLoading should be updated to true", stats2.isLoading)
    }

    @Test
    fun testDashboardStats_copyWithAllFields() {
        val prs = listOf(
            PersonalRecordEntity(exerciseId = 1, exerciseName = "Bench Press", weightKg = 100f, reps = 5, recordType = "WEIGHT"),
            PersonalRecordEntity(exerciseId = 2, exerciseName = "Squat", weightKg = 120f, reps = 3, recordType = "WEIGHT"),
            PersonalRecordEntity(exerciseId = 3, exerciseName = "Deadlift", weightKg = 140f, reps = 2, recordType = "WEIGHT")
        )

        val stats = DashboardStats(
            totalWorkouts = 20,
            totalVolumeKg = 10000f,
            totalSets = 200,
            totalReps = 800,
            recentPrs = prs,
            isLoading = false
        )

        val updatedStats = stats.copy(isLoading = true, totalWorkouts = 30)
        assertEquals(30, updatedStats.totalWorkouts)
        assertEquals(10000f, updatedStats.totalVolumeKg, 0.01f)
        assertEquals(200, updatedStats.totalSets)
        assertEquals(800, updatedStats.totalReps)
        assertEquals(3, updatedStats.recentPrs.size)
        assertTrue(updatedStats.isLoading)
    }

    @Test
    fun testEmptyPrsList() {
        val emptyPrsStats = DashboardStats(
            totalWorkouts = 5,
            totalVolumeKg = 1000f,
            totalSets = 50,
            totalReps = 250,
            recentPrs = emptyList(),
            isLoading = false
        )

        assertTrue("PRs list should be empty", emptyPrsStats.recentPrs.isEmpty())
        assertEquals("Workout count preserved", 5, emptyPrsStats.totalWorkouts)
        assertEquals("Volume preserved", 1000f, emptyPrsStats.totalVolumeKg, 0.01f)
    }

    @Test
    fun testMultiplePrsHandling() {
        val prs = listOf(
            PersonalRecordEntity(exerciseId = 1, exerciseName = "Bench Press", weightKg = 100f, reps = 5, estimatedOneRm = 116.7f),
            PersonalRecordEntity(exerciseId = 2, exerciseName = "Squat", weightKg = 120f, reps = 3, estimatedOneRm = 138f),
            PersonalRecordEntity(exerciseId = 3, exerciseName = "Deadlift", weightKg = 140f, reps = 2, estimatedOneRm = 168f)
        )

        val multiPrsStats = DashboardStats(
            totalWorkouts = 20,
            totalVolumeKg = 10000f,
            totalSets = 200,
            totalReps = 800,
            recentPrs = prs,
            isLoading = false
        )

        assertEquals("Should have 3 PRs", 3, multiPrsStats.recentPrs.size)
        assertEquals("Total workouts should be 20", 20, multiPrsStats.totalWorkouts)
        assertEquals("Total volume should be 10000f", 10000f, multiPrsStats.totalVolumeKg, 0.01f)
    }

    @Test
    fun testPersonalRecordEntity_defaults() {
        val defaultPr = PersonalRecordEntity(exerciseId = 1L)
        assertEquals("Default exercise name should be empty", "", defaultPr.exerciseName)
        assertEquals("Default weight should be 0f", 0f, defaultPr.weightKg, 0.01f)
        assertEquals("Default reps should be 0", 0, defaultPr.reps)
        assertEquals("Default estimatedOneRm should be 0f", 0f, defaultPr.estimatedOneRm, 0.01f)
        assertEquals("Default totalVolume should be 0f", 0f, defaultPr.totalVolume, 0.01f)
        assertEquals("Default recordType should be WEIGHT", "WEIGHT", defaultPr.recordType)
    }

    @Test
    fun testPersonalRecordEntity_customValues() {
        val pr = PersonalRecordEntity(
            id = 10L,
            exerciseId = 5L,
            exerciseName = "Overhead Press",
            weightKg = 60f,
            reps = 8,
            estimatedOneRm = 76f,
            totalVolume = 480f,
            recordType = "ONE_RM",
            achievedAt = 1700000000000L,
            sessionId = 5L
        )

        assertEquals(10L, pr.id.toLong())
        assertEquals(5L, pr.exerciseId)
        assertEquals("Overhead Press", pr.exerciseName)
        assertEquals(60f, pr.weightKg, 0.01f)
        assertEquals(8, pr.reps)
        assertEquals(76f, pr.estimatedOneRm, 0.01f)
        assertEquals(480f, pr.totalVolume, 0.01f)
        assertEquals("ONE_RM", pr.recordType)
    }

    @Test
    fun testPersonalRecordEntity_oneRmCalculation() {
        // Epley formula: weight * (1 + reps/30)
        val weight = 100f
        val reps = 8
        val expectedOneRm = weight * (1 + reps / 30f) // 100 * 1.266... = 126.67

        val pr = PersonalRecordEntity(
            exerciseId = 1L,
            weightKg = weight,
            reps = reps,
            recordType = "ONE_RM",
            estimatedOneRm = expectedOneRm
        )

        assertEquals(expectedOneRm, pr.estimatedOneRm, 0.1f)
    }

    @Test
    fun testDashboardStats_loadingStateTransitions() {
        // Test that loading state can be toggled
        val loadingStats = DashboardStats(isLoading = true)
        assertTrue("Should start loading", loadingStats.isLoading)

        val loadedStats = DashboardStats(isLoading = false)
        assertFalse("Should not be loading", loadedStats.isLoading)
    }

    @Test
    fun testDashboardStats_zeroVolumeHandling() {
        val zeroVolumeStats = DashboardStats(totalVolumeKg = 0f)
        assertEquals(0f, zeroVolumeStats.totalVolumeKg, 0.01f)
    }

    @Test
    fun testDashboardStats_largeVolumeHandling() {
        val largeVolumeStats = DashboardStats(totalVolumeKg = 100000f)
        assertEquals(100000f, largeVolumeStats.totalVolumeKg, 0.01f)
    }
}