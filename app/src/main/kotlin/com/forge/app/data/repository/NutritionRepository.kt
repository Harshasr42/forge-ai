package com.forge.app.data.repository

import com.forge.app.data.db.dao.MacroTotals
import com.forge.app.data.db.dao.MealWithItems
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.db.entities.MealFoodItemEntity
import kotlinx.coroutines.flow.Flow

interface NutritionRepository {
    // Food Catalog
    fun getAllFoods(): Flow<List<FoodItemEntity>>
    fun searchFoods(query: String): Flow<List<FoodItemEntity>>
    fun getFoodsByCategory(category: String): Flow<List<FoodItemEntity>>
    suspend fun getFoodById(id: Long): FoodItemEntity?
    suspend fun insertCustomFood(food: FoodItemEntity): Long

    // Meal Logging
    fun getMealsForDate(dateKey: String): Flow<List<MealWithItems>>
    suspend fun addFoodToMeal(
        dateKey: String,
        mealType: String,
        food: FoodItemEntity,
        quantity: Float
    ): Long
    suspend fun updateMealFoodQuantity(mealFoodItemId: Long, newQuantity: Float)
    suspend fun removeFoodFromMeal(mealFoodItemId: Long)

    // Daily Macro Totals
    fun getDailyTotals(dateKey: String): Flow<MacroTotals>
    suspend fun getDailyTotalsOnce(dateKey: String): MacroTotals

    // Target Management
    fun getTarget(): Flow<DailyNutritionTargetEntity?>
    suspend fun getTargetOnce(): DailyNutritionTargetEntity?
    suspend fun updateTarget(target: DailyNutritionTargetEntity)
}
