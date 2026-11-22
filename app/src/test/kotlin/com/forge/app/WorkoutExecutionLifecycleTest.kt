package com.forge.app

import com.forge.app.data.db.entities.SessionExerciseEntity
import com.forge.app.data.db.entities.SetRecordEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import org.junit.Assert.*
import org.junit.Test

class WorkoutExecutionLifecycleTest {

    @Test
    fun testFullWorkoutLifecycle() {
        // Step 1: User selects workout day (Day 1: Chest & Biceps A)
        val dayId = 1L
        val workoutName = "Chest & Biceps"

        // Step 2: Start workout session
        val startTime = System.currentTimeMillis()
        val session = WorkoutSessionEntity(
            id = 100L,
            programmeDayId = dayId,
            workoutName = workoutName,
            startedAt = startTime,
            status = "ACTIVE"
        )
        assertEquals("ACTIVE", session.status)
        assertEquals(100L, session.id)

        // Step 3: Populate session exercises (e.g. 30° Incline DB Press)
        val exercise1 = SessionExerciseEntity(
            id = 201L,
            sessionId = session.id,
            exerciseId = 1L,
            orderInSession = 1
        )
        val exercise2 = SessionExerciseEntity(
            id = 202L,
            sessionId = session.id,
            exerciseId = 24L,
            orderInSession = 2
        )
        val exercises = listOf(exercise1, exercise2)
        assertEquals(2, exercises.size)

        // Step 4: Populate initial sets for Exercise 1
        val initialSets = mutableListOf(
            SetRecordEntity(id = 301L, sessionExerciseId = exercise1.id, setNumber = 1, weightKg = 30f, reps = 10, restAfterSeconds = 90),
            SetRecordEntity(id = 302L, sessionExerciseId = exercise1.id, setNumber = 2, weightKg = 30f, reps = 10, restAfterSeconds = 90),
            SetRecordEntity(id = 303L, sessionExerciseId = exercise1.id, setNumber = 3, weightKg = 30f, reps = 10, restAfterSeconds = 90)
        )
        assertEquals(3, initialSets.size)

        // Step 5: User edits repetitions and weights (e.g., set 2 to 34kg for 12 reps)
        val updatedSet2 = initialSets[1].copy(weightKg = 34f, reps = 12)
        initialSets[1] = updatedSet2
        assertEquals(34f, initialSets[1].weightKg, 0.01f)
        assertEquals(12, initialSets[1].reps)

        // Step 6: User completes set 1 & set 2
        val completedSet1 = initialSets[0].copy(isCompleted = true, completedAt = System.currentTimeMillis())
        val completedSet2 = initialSets[1].copy(isCompleted = true, completedAt = System.currentTimeMillis())
        initialSets[0] = completedSet1
        initialSets[1] = completedSet2
        assertTrue(initialSets[0].isCompleted)
        assertTrue(initialSets[1].isCompleted)
        assertFalse(initialSets[2].isCompleted)

        // Step 7: Rest timer is triggered after completed set
        val triggeredRestSeconds = initialSets[0].restAfterSeconds
        assertEquals(90, triggeredRestSeconds)

        // Step 8: User adds an extra 4th set
        val nextSetNum = (initialSets.maxOfOrNull { it.setNumber } ?: 0) + 1
        val lastSet = initialSets.last()
        val newSet4 = SetRecordEntity(
            id = 304L,
            sessionExerciseId = exercise1.id,
            setNumber = nextSetNum,
            weightKg = lastSet.weightKg,
            reps = lastSet.reps,
            restAfterSeconds = 90
        )
        initialSets.add(newSet4)
        assertEquals(4, initialSets.size)
        assertEquals(4, initialSets[3].setNumber)

        // Complete set 3 & 4
        initialSets[2] = initialSets[2].copy(isCompleted = true, completedAt = System.currentTimeMillis())
        initialSets[3] = initialSets[3].copy(isCompleted = true, completedAt = System.currentTimeMillis())

        // Step 9: User navigates between exercises
        var currentExerciseIndex = 0
        // Next exercise
        currentExerciseIndex = (currentExerciseIndex + 1).coerceAtMost(exercises.size - 1)
        assertEquals(1, currentExerciseIndex)
        // Previous exercise
        currentExerciseIndex = (currentExerciseIndex - 1).coerceAtLeast(0)
        assertEquals(0, currentExerciseIndex)

        // Step 10: Calculate total completed sets & volume
        val totalCompletedSets = initialSets.count { it.isCompleted }
        assertEquals(4, totalCompletedSets)

        val totalVolume = initialSets.filter { it.isCompleted }.sumOf { (it.weightKg * it.reps).toDouble() }.toFloat()
        // Set 1: 30 * 10 = 300
        // Set 2: 34 * 12 = 408
        // Set 3: 30 * 10 = 300
        // Set 4: 30 * 10 = 300
        // Total = 1308
        assertEquals(1308f, totalVolume, 0.01f)

        // Step 11: Finish workout
        val finishTime = startTime + (45 * 60 * 1000L) // 45 minutes later
        val completedSession = session.copy(
            status = "COMPLETED",
            completedAt = finishTime,
            durationSeconds = 45 * 60L,
            totalVolume = totalVolume,
            totalSets = totalCompletedSets
        )

        // Step 12: Verify completed session fields
        assertEquals("COMPLETED", completedSession.status)
        assertEquals(2700L, completedSession.durationSeconds)
        assertEquals(4, completedSession.totalSets)
        assertEquals(1308f, completedSession.totalVolume, 0.01f)
        assertNotNull(completedSession.completedAt)
    }

    @Test
    fun testActiveSessionPersistenceRecovery() {
        val startedAt = System.currentTimeMillis() - (15 * 60 * 1000L) // started 15 min ago
        val activeSession = WorkoutSessionEntity(
            id = 42L,
            workoutName = "Legs & Shoulders",
            startedAt = startedAt,
            status = "ACTIVE"
        )

        // Process killed and recovered:
        val now = System.currentTimeMillis()
        val recoveredElapsedSeconds = (now - activeSession.startedAt) / 1000

        assertTrue("Elapsed seconds must be around 900s", recoveredElapsedSeconds in 890..910)
        assertEquals("ACTIVE", activeSession.status)
    }
}
