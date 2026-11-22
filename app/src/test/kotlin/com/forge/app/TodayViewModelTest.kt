package com.forge.app

import com.forge.app.data.db.entities.*
import com.forge.app.ui.screens.today.TodayUiState
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tests for TodayViewModel data and state logic.
 * Note: Full ViewModel tests with Hilt dependencies require Android instrumentation tests.
 */
class TodayViewModelTest {

    @Test
    fun testDefaultUiState_values() {
        val defaultState = TodayUiState()
        assertEquals("Default totalExercises should be 8", 8, defaultState.totalExercises)
        assertEquals("Default totalWorkingSets should be 24", 24, defaultState.totalWorkingSets)
        assertEquals("Default estimatedMinutes should be 65", 65, defaultState.estimatedMinutes)
        assertTrue("Default completedTasks should be empty", defaultState.completedTasks.isEmpty())
    }

    @Test
    fun testUiState_copyPreservesValues() {
        val state1 = TodayUiState(
            user = UserEntity(name = "Test User", weightKg = 75f),
            totalExercises = 10,
            isLoading = false
        )

        val state2 = state1.copy(isLoading = true)
        assertEquals("user should be preserved", state1.user, state2.user)
        assertEquals("totalExercises should be preserved", 10, state2.totalExercises)
        assertTrue("isLoading should be updated to true", state2.isLoading)
    }

    @Test
    fun testUiState_copyPreservesAllFields() {
        val testSession = WorkoutSessionEntity(
            id = 1L,
            workoutName = "Test Workout",
            status = "ACTIVE"
        )
        val testConsistency = ConsistencyEntity(
            id = 1L,
            currentStreak = 5,
            totalWorkoutDays = 30,
            totalXp = 1500,
            level = 3
        )

        val state1 = TodayUiState(
            user = UserEntity(name = "Test User"),
            todayProgrammeDay = ProgrammeDayEntity(
                programmeId = 1L,
                dayOfWeek = 1,
                workoutName = "Workout",
                muscleGroups = "Chest, Biceps"
            ),
            activeSession = testSession,
            consistency = testConsistency,
            totalExercises = 12,
            totalWorkingSets = 48,
            estimatedMinutes = 75,
            completedTasks = setOf("task1", "task2"),
            isLoading = false
        )

        val state2 = state1.copy(isLoading = true, totalWorkingSets = 50)

        // Verify preserved values after copy
        assertEquals("user should be preserved", state1.user, state2.user)
        assertEquals("totalExercises should be preserved", 12, state2.totalExercises)
        assertEquals("completedTasks should be preserved", setOf("task1", "task2"), state2.completedTasks)
        assertEquals("activeSession should be preserved", testSession, state2.activeSession)
        assertEquals("totalWorkingSets should be updated to 50", 50, state2.totalWorkingSets)
        assertTrue("isLoading should be true", state2.isLoading)
    }

    @Test
    fun testTodayDateKey_format() {
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val parts = dateKey.split("-")
        assertEquals("Date key should have 3 parts", 3, parts.size)
        assertEquals("Year should be 4 digits", 4, parts[0].length)
    }

    @Test
    fun testDayOfWeekCalculation() {
        val calendar = Calendar.getInstance()
        val dayOfWeekJava = calendar.get(Calendar.DAY_OF_WEEK)
        val currentDayOfWeek = if (dayOfWeekJava == Calendar.SUNDAY) 7 else dayOfWeekJava - 1

        assertTrue("Day of week should be valid (1-7)", currentDayOfWeek in 1..7)
    }

    @Test
    fun testUserEntity_defaults() {
        val defaultUser = UserEntity()
        assertEquals("Default name should be Athlete", "Athlete", defaultUser.name)
        assertEquals("Default weight should be 70f", 70f, defaultUser.weightKg, 0.01f)
        assertEquals("Default id should be 1L", 1L, defaultUser.id)
    }

    @Test
    fun testUserEntity_customValues() {
        val customUser = UserEntity(
            id = 100L,
            name = "Custom Athlete",
            weightKg = 85f,
            heightCm = 180f,
            experienceLevel = "ADVANCED"
        )

        assertEquals("Custom id should be 100", 100L, customUser.id)
        assertEquals("Custom name should be Custom Athlete", "Custom Athlete", customUser.name)
        assertEquals("Custom weight should be 85f", 85f, customUser.weightKg, 0.01f)
        assertEquals("Custom height should be 180f", 180f, customUser.heightCm, 0.01f)
        assertEquals("Experience level should be ADVANCED", "ADVANCED", customUser.experienceLevel)
    }

    @Test
    fun testWorkoutSessionEntity_defaults() {
        val defaultSession = WorkoutSessionEntity(workoutName = "Test Workout")
        assertEquals("Default status should be ACTIVE", "ACTIVE", defaultSession.status)
        assertEquals("Default duration should be 0", 0L, defaultSession.durationSeconds)
        assertEquals("Default totalVolume should be 0f", 0f, defaultSession.totalVolume, 0.01f)
        assertNull("Default completedAt should be null for active session", defaultSession.completedAt)
    }

    @Test
    fun testWorkoutSessionEntity_completed() {
        val completedSession = WorkoutSessionEntity(
            id = 1L,
            workoutName = "Finished Workout",
            status = "COMPLETED",
            durationSeconds = 3600,
            completedAt = System.currentTimeMillis(),
            totalVolume = 1500f
        )

        assertEquals("Status should be COMPLETED", "COMPLETED", completedSession.status)
        assertEquals("Duration should be 3600 seconds", 3600L, completedSession.durationSeconds)
        assertNotNull("completedAt should be set for completed session", completedSession.completedAt)
        assertEquals("Total volume should be 1500f", 1500f, completedSession.totalVolume, 0.01f)
    }

    @Test
    fun testConsistencyEntity_defaults() {
        val defaultConsistency = ConsistencyEntity()
        assertEquals("Default currentStreak should be 1", 1, defaultConsistency.currentStreak)
        assertEquals("Default totalWorkoutDays should be 1", 1, defaultConsistency.totalWorkoutDays)
        assertEquals("Default totalXp should be 120", 120, defaultConsistency.totalXp)
        assertEquals("Default level should be 1", 1, defaultConsistency.level)
        assertEquals("Default levelTitle should be Apprentice", "Apprentice", defaultConsistency.levelTitle)
        assertNull("Default lastWorkoutDate should be null", defaultConsistency.lastWorkoutDate)
        assertNull("Default lastStreakDate should be null", defaultConsistency.lastStreakDate)
    }

    @Test
    fun testConsistencyEntity_customValues() {
        val customConsistency = ConsistencyEntity(
            id = 1L,
            currentStreak = 10,
            longestStreak = 15,
            totalWorkoutDays = 100,
            totalXp = 5000,
            level = 5
        )

        assertEquals(1L, customConsistency.id)
        assertEquals(10, customConsistency.currentStreak)
        assertEquals(15, customConsistency.longestStreak)
        assertEquals(100, customConsistency.totalWorkoutDays)
        assertEquals(5000, customConsistency.totalXp)
        assertEquals(5, customConsistency.level)
    }

    @Test
    fun testProgrammeDayEntity_requiredFieldsOnly() {
        val day = ProgrammeDayEntity(
            programmeId = 1L,
            dayOfWeek = 1,
            workoutName = "Monday Workout",
            muscleGroups = "Chest, Triceps"
        )

        assertEquals(1L, day.programmeId)
        assertEquals(1, day.dayOfWeek)
        assertEquals("Monday Workout", day.workoutName)
        assertEquals("Chest, Triceps", day.muscleGroups)
        assertEquals("A", day.workoutVariant) // default
        assertEquals(65, day.estimatedMinutes) // default
        assertFalse(day.isRestDay) // default
    }

    @Test
    fun testProgrammeDayEntity_restDay() {
        val restDay = ProgrammeDayEntity(
            programmeId = 1L,
            dayOfWeek = 7,
            workoutName = "Recovery",
            muscleGroups = "Mobility",
            isRestDay = true,
            estimatedMinutes = 30
        )

        assertTrue("Should be a rest day", restDay.isRestDay)
        assertEquals("Recovery", restDay.workoutName)
        assertEquals(30, restDay.estimatedMinutes)
    }

    @Test
    fun testProgrammeEntity_defaults() {
        val defaultProgramme = ProgrammeEntity(name = "Test Programme")
        assertEquals("Test Programme", defaultProgramme.name)
        assertTrue("Should be active by default", defaultProgramme.isActive)
    }

    @Test
    fun testExerciseEntity_defaults() {
        val customExercise = ExerciseEntity(
            id = 1L,
            name = "Custom Squat",
            category = "LEGS",
            equipment = "BARBELL",
            muscleTarget = "Quadriceps"
        )

        assertEquals("Custom Squat", customExercise.name)
        assertEquals("LEGS", customExercise.category)
        assertEquals("BARBELL", customExercise.equipment)
        assertEquals("Quadriceps", customExercise.muscleTarget)
        assertFalse("Should not be favourite by default", customExercise.isFavourite)
    }
}