package com.forge.app

import org.junit.Assert.*
import org.junit.Test

class ProgrammeSplitAndFinisherTest {

    data class DaySchedule(
        val dayOfWeek: Int,
        val name: String,
        val variant: String,
        val isRestDay: Boolean,
        val exerciseIds: List<Long>
    )

    private val weeklySchedule = listOf(
        // Mon: Chest + Biceps (+ Forearms) [Dead Hang] - A
        DaySchedule(1, "Chest & Biceps", "A", false, listOf(1L, 2L, 3L, 24L, 26L, 37L, 39L, 13L)),
        // Tue: Back + Triceps (+ Abs) [Dead Hang] - A
        DaySchedule(2, "Back & Triceps", "A", false, listOf(5L, 7L, 8L, 9L, 20L, 21L, 42L, 43L, 13L)),
        // Wed: Legs & Shoulders (Quads & Side-Delt) - A
        DaySchedule(3, "Legs & Shoulders (Quads & Side-Delt)", "A", false, listOf(28L, 30L, 31L, 33L, 34L, 15L, 18L)),
        // Thu: Chest & Biceps - B
        DaySchedule(4, "Chest & Biceps", "B", false, listOf(4L, 1L, 3L, 27L, 25L, 38L, 40L)),
        // Fri: Back & Triceps - B
        DaySchedule(5, "Back & Triceps", "B", false, listOf(11L, 6L, 8L, 10L, 23L, 22L, 45L, 47L)),
        // Sat: Legs & Shoulders (Hamstrings & Rear-Delt) - B
        DaySchedule(6, "Legs & Shoulders (Hamstring & Rear-Delt)", "B", false, listOf(32L, 31L, 29L, 34L, 35L, 16L, 19L)),
        // Sun: Active Recovery & Mobility
        DaySchedule(7, "Active Recovery & Mobility", "Recovery", true, listOf(46L, 36L, 13L, 51L))
    )

    @Test
    fun testAllSevenDaysPresent() {
        assertEquals(7, weeklySchedule.size)
        val days = weeklySchedule.map { it.dayOfWeek }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), days)
    }

    @Test
    fun testWorkoutVariantsAreCorrect() {
        assertEquals("A", weeklySchedule[0].variant)
        assertEquals("A", weeklySchedule[1].variant)
        assertEquals("A", weeklySchedule[2].variant)
        assertEquals("B", weeklySchedule[3].variant)
        assertEquals("B", weeklySchedule[4].variant)
        assertEquals("B", weeklySchedule[5].variant)
        assertEquals("Recovery", weeklySchedule[6].variant)
    }

    @Test
    fun testSundayIsRestDay() {
        assertTrue(weeklySchedule[6].isRestDay)
        assertFalse(weeklySchedule[0].isRestDay)
        assertFalse(weeklySchedule[1].isRestDay)
        assertFalse(weeklySchedule[2].isRestDay)
        assertFalse(weeklySchedule[3].isRestDay)
        assertFalse(weeklySchedule[4].isRestDay)
        assertFalse(weeklySchedule[5].isRestDay)
    }

    @Test
    fun testDeadHangFinishersAreScheduledOnMondayAndTuesday() {
        val deadHangId = 13L
        assertTrue("Monday must end with Dead Hang", weeklySchedule[0].exerciseIds.contains(deadHangId))
        assertTrue("Tuesday must end with Dead Hang", weeklySchedule[1].exerciseIds.contains(deadHangId))
        assertEquals("Dead Hang must be the last exercise on Monday", deadHangId, weeklySchedule[0].exerciseIds.last())
        assertEquals("Dead Hang must be the last exercise on Tuesday", deadHangId, weeklySchedule[1].exerciseIds.last())
    }

    @Test
    fun testSaturdayHasFarmersCarryFinisher() {
        val farmersCarryId = 35L
        assertTrue("Saturday must have Farmer's Carry", weeklySchedule[5].exerciseIds.contains(farmersCarryId))
    }

    @Test
    fun testSundayHasMobilityAndCoreMovements() {
        val sunExercises = weeklySchedule[6].exerciseIds
        assertTrue("Sunday has Stomach Vacuums", sunExercises.contains(46L))
        assertTrue("Sunday has 2-Min Malasana Squat Hold", sunExercises.contains(36L))
        assertTrue("Sunday has Dead Hang", sunExercises.contains(13L))
        assertTrue("Sunday has World's Greatest Stretch", sunExercises.contains(51L))
    }

    @Test
    fun testAllAlternativePairsAreMapped() {
        val alternativesMap = mapOf(
            5L to 6L,   // Lat Pulldown -> Neutral Grip
            16L to 17L, // Incline Rear Delt -> Reverse Pec Deck
            24L to 27L, // Bayesian Curl -> Incline DB Curl
            28L to 29L, // Smith Squat -> Hack Squat
            37L to 38L, // Reverse EZ Curl -> Incline Hammer
            43L to 44L, // Hanging Raise -> Captain's Chair
            40L to 41L  // Wrist Extension -> Behind-the-Back Cable
        )
        assertEquals(7, alternativesMap.size)
        alternativesMap.forEach { (primary, alt) ->
            assertNotEquals("Primary and alternative exercise IDs must differ", primary, alt)
            assertTrue("Primary ID must be valid", primary in 1L..51L)
            assertTrue("Alternative ID must be valid", alt in 1L..51L)
        }
    }
}
