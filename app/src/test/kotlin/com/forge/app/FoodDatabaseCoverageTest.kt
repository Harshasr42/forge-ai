package com.forge.app

import com.forge.app.data.seed.FoodDatabaseSeeder
import org.junit.Assert.*
import org.junit.Test

class FoodDatabaseCoverageTest {

    @Test
    fun testSeedFoodDatabaseCompleteness() {
        val foods = FoodDatabaseSeeder.getSeedFoods()

        // Must have at least 50 foods
        assertTrue("Database should have 50+ foods", foods.size >= 50)

        // Essential Indian staples verification
        val indianFoods = foods.filter { it.category == "INDIAN" }
        assertTrue("Should have extensive Indian food coverage", indianFoods.size >= 15)

        val foodNames = foods.map { it.name.lowercase() }
        val requiredFoods = listOf(
            "idli",
            "plain dosa",
            "roti / phulka",
            "dal tadka",
            "chana masala",
            "rajma curry",
            "paneer",
            "chicken breast",
            "whole boiled egg",
            "rolled oats",
            "plain curd / dahi"
        )

        requiredFoods.forEach { required ->
            assertTrue(
                "Food database must contain $required",
                foodNames.any { it.contains(required) }
            )
        }
    }

    @Test
    fun testNutritionalValuesSanity() {
        val foods = FoodDatabaseSeeder.getSeedFoods()

        foods.forEach { food ->
            assertTrue("${food.name} calories must be positive", food.calories >= 0f)
            assertTrue("${food.name} protein must be >= 0", food.protein >= 0f)
            assertTrue("${food.name} carbs must be >= 0", food.carbs >= 0f)
            assertTrue("${food.name} fat must be >= 0", food.fat >= 0f)
            assertTrue("${food.name} serving size must be > 0", food.servingSize > 0f)
            assertTrue("${food.name} serving unit must not be blank", food.servingUnit.isNotBlank())
        }
    }
}
