package com.forge.app

import org.junit.Assert.*
import org.junit.Test

class NutritionMacroCalculationTest {

    @Test
    fun testMacroToCalorieFormula() {
        // Protein = 4 kcal/g, Carbs = 4 kcal/g, Fat = 9 kcal/g
        val protein = 150f
        val carbs = 220f
        val fat = 60f

        val calculatedCalories = (protein * 4f) + (carbs * 4f) + (fat * 9f)
        // 600 + 880 + 540 = 2020 kcal
        assertEquals(2020f, calculatedCalories, 0.01f)
    }

    @Test
    fun testServingMultiplierScaling() {
        // Base: 100g Paneer -> 290 kcal, 18.3g P, 3.4g C, 22.0g F
        val baseCalories = 290f
        val baseProtein = 18.3f
        val baseCarbs = 3.4f
        val baseFat = 22.0f

        val multiplier = 1.5f // 150g serving

        val scaledCalories = baseCalories * multiplier
        val scaledProtein = baseProtein * multiplier
        val scaledCarbs = baseCarbs * multiplier
        val scaledFat = baseFat * multiplier

        assertEquals(435f, scaledCalories, 0.01f)
        assertEquals(27.45f, scaledProtein, 0.01f)
        assertEquals(5.1f, scaledCarbs, 0.01f)
        assertEquals(33.0f, scaledFat, 0.01f)
    }

    @Test
    fun testDailyCalorieRemainingMath() {
        val dailyTarget = 2400f
        val consumedBreakfast = 550f
        val consumedLunch = 750f
        val consumedSnacks = 300f

        val totalConsumed = consumedBreakfast + consumedLunch + consumedSnacks // 1600f
        val remaining = (dailyTarget - totalConsumed).coerceAtLeast(0f)

        assertEquals(800f, remaining, 0.01f)

        // Over-consumption scenario
        val heavyDinner = 1000f
        val totalOver = totalConsumed + heavyDinner // 2600f
        val remainingOver = (dailyTarget - totalOver).coerceAtLeast(0f)
        assertEquals(0f, remainingOver, 0.01f)
    }

    @Test
    fun testHydrationTargetCalculation() {
        val targetMl = 3500
        val logs = listOf(250, 500, 750, 500) // 2000 ml
        val totalMl = logs.sum()
        val percentage = (totalMl.toFloat() / targetMl).coerceIn(0f, 1f)

        assertEquals(2000, totalMl)
        assertEquals(0.5714f, percentage, 0.001f)
    }
}
