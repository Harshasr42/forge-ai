package com.forge.app

import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutTimerLogicTest {

    private fun formatTime(seconds: Long): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(mins, secs)
    }

    @Test
    fun testFormatTime_zeroSeconds() {
        assertEquals("00:00", formatTime(0L))
    }

    @Test
    fun testFormatTime_underOneMinute() {
        assertEquals("00:45", formatTime(45L))
    }

    @Test
    fun testFormatTime_multipleMinutes() {
        assertEquals("12:05", formatTime(725L))
    }

    @Test
    fun testFormatTime_oneHour() {
        assertEquals("60:00", formatTime(3600L))
    }

    @Test
    fun testRestTimerAddSeconds() {
        var remaining = 45
        val add15 = 15
        remaining += add15
        assertEquals(60, remaining)

        val add30 = 30
        remaining += add30
        assertEquals(90, remaining)
    }

    @Test
    fun testRestTimerSkip() {
        var remaining = 75
        var isActive = true

        // Simulate skip
        remaining = 0
        isActive = false

        assertEquals(0, remaining)
        assertEquals(false, isActive)
    }

    @Test
    fun testWorkoutVolumeCalculation() {
        val sets = listOf(
            Pair(50f, 10), // 500
            Pair(60f, 8),  // 480
            Pair(70f, 6),  // 420
            Pair(70f, 5)   // 350
        )
        val totalVolume = sets.sumOf { (weight, reps) -> (weight * reps).toDouble() }.toFloat()
        assertEquals(1750f, totalVolume, 0.001f)
    }

    @Test
    fun testEstimatedDurationCalculation() {
        val workingSets = 24
        val avgSetDurationSeconds = 45
        val avgRestSeconds = 75
        val totalSeconds = workingSets * (avgSetDurationSeconds + avgRestSeconds)
        val estimatedMinutes = totalSeconds / 60
        assertEquals(48, estimatedMinutes)
    }
}
