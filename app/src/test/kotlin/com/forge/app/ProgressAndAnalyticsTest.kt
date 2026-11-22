package com.forge.app

import org.junit.Assert.*
import org.junit.Test

class ProgressAndAnalyticsTest {

    // Epley Formula: 1RM = Weight * (1 + Reps / 30)
    private fun calculateEpleyOneRm(weightKg: Float, reps: Int): Float {
        if (reps <= 0 || weightKg <= 0f) return 0f
        if (reps == 1) return weightKg
        if (reps > 12) return 0f // Exclude high reps from 1RM estimation as per specification
        return weightKg * (1f + reps / 30f)
    }

    @Test
    fun testEpleyFormulaAccuracy() {
        // Bench 100kg for 10 reps -> 100 * (1 + 10/30) = 133.33 kg
        val oneRm = calculateEpleyOneRm(100f, 10)
        assertEquals(133.33f, oneRm, 0.05f)

        // 80kg for 6 reps -> 80 * (1 + 6/30) = 80 * 1.20 = 96 kg
        val oneRm2 = calculateEpleyOneRm(80f, 6)
        assertEquals(96.0f, oneRm2, 0.01f)

        // 1 rep should equal working weight
        val singleRep = calculateEpleyOneRm(120f, 1)
        assertEquals(120f, singleRep, 0.01f)
    }

    @Test
    fun testHighRepsExcludedFromOneRmCalculation() {
        // Sets with > 12 reps should NOT be used for 1RM estimation
        val highRepSet = calculateEpleyOneRm(50f, 15)
        assertEquals(0f, highRepSet, 0.001f)

        val enduranceSet = calculateEpleyOneRm(30f, 25)
        assertEquals(0f, enduranceSet, 0.001f)
    }

    @Test
    fun testVolumeCalculationPerExercise() {
        val sets = listOf(
            Pair(60f, 10), // 600
            Pair(70f, 8),  // 560
            Pair(80f, 6)   // 480
        )
        val totalVolume = sets.sumOf { (weight, reps) -> (weight * reps).toDouble() }.toFloat()
        assertEquals(1640f, totalVolume, 0.01f)
    }

    @Test
    fun testGoalProgressPercentage() {
        val startWeight = 85f
        val targetWeight = 75f
        val currentWeight = 78f

        // For weight loss: progress = (start - current) / (start - target)
        val progress = ((startWeight - currentWeight) / (startWeight - targetWeight)).coerceIn(0f, 1f)
        assertEquals(0.7f, progress, 0.01f) // 70% towards 75kg
    }
}
