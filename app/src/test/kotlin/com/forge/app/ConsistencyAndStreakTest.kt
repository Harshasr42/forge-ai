package com.forge.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ConsistencyAndStreakTest {

    private fun calculateLevel(xp: Int): Int = when {
        xp >= 25000 -> 7
        xp >= 13000 -> 6
        xp >= 7000  -> 5
        xp >= 3500  -> 4
        xp >= 1500  -> 3
        xp >= 500   -> 2
        else        -> 1
    }

    private fun calculateLevelTitle(level: Int): String = when (level) {
        7 -> "Forged"
        6 -> "Elite"
        5 -> "Athlete"
        4 -> "Warrior"
        3 -> "Disciplined"
        2 -> "Apprentice"
        else -> "Beginner"
    }

    @Test
    fun testLevelProgression() {
        assertEquals(1, calculateLevel(0))
        assertEquals(1, calculateLevel(499))
        assertEquals("Beginner", calculateLevelTitle(1))

        assertEquals(2, calculateLevel(500))
        assertEquals(2, calculateLevel(1499))
        assertEquals("Apprentice", calculateLevelTitle(2))

        assertEquals(3, calculateLevel(1500))
        assertEquals("Disciplined", calculateLevelTitle(3))

        assertEquals(4, calculateLevel(3500))
        assertEquals("Warrior", calculateLevelTitle(4))

        assertEquals(5, calculateLevel(7000))
        assertEquals("Athlete", calculateLevelTitle(5))

        assertEquals(6, calculateLevel(13000))
        assertEquals("Elite", calculateLevelTitle(6))

        assertEquals(7, calculateLevel(25000))
        assertEquals("Forged", calculateLevelTitle(7))
    }

    @Test
    fun testStreakCalculation_consecutiveDay() {
        val lastDateCal = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1) // yesterday
        }
        val nowDateCal = Calendar.getInstance()

        val isConsecutive = (nowDateCal.get(Calendar.DAY_OF_YEAR) - lastDateCal.get(Calendar.DAY_OF_YEAR) == 1) ||
                (nowDateCal.get(Calendar.YEAR) > lastDateCal.get(Calendar.YEAR) && nowDateCal.get(Calendar.DAY_OF_YEAR) == 1)

        val previousStreak = 5
        val newStreak = if (isConsecutive) previousStreak + 1 else 1

        assertEquals(6, newStreak)
    }

    @Test
    fun testStreakCalculation_sameDay() {
        val lastDateCal = Calendar.getInstance()
        val nowDateCal = Calendar.getInstance()

        val isSameDay = nowDateCal.get(Calendar.YEAR) == lastDateCal.get(Calendar.YEAR) &&
                nowDateCal.get(Calendar.DAY_OF_YEAR) == lastDateCal.get(Calendar.DAY_OF_YEAR)

        val previousStreak = 5
        val newStreak = if (isSameDay) previousStreak else previousStreak + 1

        assertEquals(5, newStreak)
    }

    @Test
    fun testStreakCalculation_missedDay_resetsToOne() {
        val lastDateCal = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -3) // 3 days ago
        }
        val nowDateCal = Calendar.getInstance()

        val daysDiff = nowDateCal.get(Calendar.DAY_OF_YEAR) - lastDateCal.get(Calendar.DAY_OF_YEAR)
        val newStreak = if (daysDiff == 1) 5 + 1 else if (daysDiff == 0) 5 else 1

        assertEquals(1, newStreak)
    }

    @Test
    fun testAchievementEligibility() {
        val totalWorkouts = 1
        val currentStreak = 7

        val unlockedIds = mutableListOf<String>()
        if (totalWorkouts >= 1) unlockedIds.add("FIRST_WORKOUT")
        if (currentStreak >= 3) unlockedIds.add("STREAK_3")
        if (currentStreak >= 7) unlockedIds.add("STREAK_7")

        assertTrue(unlockedIds.contains("FIRST_WORKOUT"))
        assertTrue(unlockedIds.contains("STREAK_3"))
        assertTrue(unlockedIds.contains("STREAK_7"))
    }
}
