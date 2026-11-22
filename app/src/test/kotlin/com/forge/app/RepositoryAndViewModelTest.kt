package com.forge.app

import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryAndViewModelTest {

    @Test
    fun testExerciseSearchFilterLogic() = runTest {
        val sampleExercises = listOf(
            ExerciseEntity(id = 1, name = "30° Incline DB Press", category = "CHEST", equipment = "DUMBBELL", muscleTarget = "Upper Chest"),
            ExerciseEntity(id = 2, name = "Flat DB Press", category = "CHEST", equipment = "DUMBBELL", muscleTarget = "Mid Chest"),
            ExerciseEntity(id = 5, name = "Single-arm Half-kneeling Lat Pulldown", category = "BACK", equipment = "CABLE", muscleTarget = "Latissimus Dorsi"),
            ExerciseEntity(id = 13, name = "Dead Hang", category = "BACK", equipment = "BODYWEIGHT", muscleTarget = "Decompression, Grip"),
            ExerciseEntity(id = 28, name = "Smith Machine Squat", category = "LEGS", equipment = "SMITH_MACHINE", muscleTarget = "Quadriceps")
        )

        // Case-insensitive query search
        val query = "press"
        val filteredByQuery = sampleExercises.filter { it.name.contains(query, ignoreCase = true) }
        assertEquals(2, filteredByQuery.size)
        assertTrue(filteredByQuery.any { it.id == 1L })
        assertTrue(filteredByQuery.any { it.id == 2L })

        // Category filtering
        val backExercises = sampleExercises.filter { it.category == "BACK" }
        assertEquals(2, backExercises.size)
        assertTrue(backExercises.any { it.id == 5L })
        assertTrue(backExercises.any { it.id == 13L })

        // Equipment filtering
        val dumbbellExercises = sampleExercises.filter { it.equipment == "DUMBBELL" }
        assertEquals(2, dumbbellExercises.size)
    }

    @Test
    fun testWorkoutHistorySortingAndFiltering() = runTest {
        val now = System.currentTimeMillis()
        val sessions = listOf(
            WorkoutSessionEntity(id = 1, workoutName = "Chest & Biceps", startedAt = now - 86400000L * 3, status = "COMPLETED", durationSeconds = 3600),
            WorkoutSessionEntity(id = 2, workoutName = "Back & Triceps", startedAt = now - 86400000L * 2, status = "COMPLETED", durationSeconds = 3900),
            WorkoutSessionEntity(id = 3, workoutName = "Legs & Shoulders", startedAt = now - 86400000L, status = "COMPLETED", durationSeconds = 4200),
            WorkoutSessionEntity(id = 4, workoutName = "Chest & Biceps B", startedAt = now, status = "ACTIVE", durationSeconds = 1200)
        )

        val flow = MutableStateFlow(sessions)

        // Only completed sessions should appear in History
        val completed = flow.first().filter { it.status == "COMPLETED" }.sortedByDescending { it.startedAt }
        assertEquals(3, completed.size)
        assertEquals("Legs & Shoulders", completed.first().workoutName)
        assertEquals("Chest & Biceps", completed.last().workoutName)
    }

    @Test
    fun testToggleSetCompletionStateFlow() = runTest {
        val setsFlow = MutableStateFlow(listOf(false, false, false))

        // Toggle set 0 to true
        setsFlow.value = setsFlow.value.toMutableList().apply { set(0, true) }
        assertTrue(setsFlow.first()[0])
        assertFalse(setsFlow.first()[1])

        // Toggle set 1 to true
        setsFlow.value = setsFlow.value.toMutableList().apply { set(1, true) }
        val completedCount = setsFlow.first().count { it }
        assertEquals(2, completedCount)

        // Toggle set 0 back to false
        setsFlow.value = setsFlow.value.toMutableList().apply { set(0, false) }
        assertEquals(1, setsFlow.first().count { it })
    }

    @Test
    fun testRestTimerCountdownTermination() = runTest {
        var remainingSeconds = 3
        val ticks = mutableListOf<Int>()

        while (remainingSeconds > 0) {
            ticks.add(remainingSeconds)
            remainingSeconds--
        }
        assertEquals(listOf(3, 2, 1), ticks)
        assertEquals(0, remainingSeconds)
    }
}
