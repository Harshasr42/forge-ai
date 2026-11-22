package com.forge.app.data.repository

import com.forge.app.data.db.dao.GroceryItem
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.db.entities.MealPlanDayEntity
import kotlinx.coroutines.flow.Flow

interface MealPlanRepository {
    fun getPlanForDay(dayOfWeek: Int): Flow<List<MealPlanDayEntity>>
    fun getFullWeeklyPlan(): Flow<List<MealPlanDayEntity>>
    suspend fun addFoodToPlan(
        dayOfWeek: Int,
        mealType: String,
        food: FoodItemEntity,
        quantity: Float
    ): Long
    suspend fun removeFoodFromPlan(id: Long)
    suspend fun clearDayPlan(dayOfWeek: Int)
    fun getAggregatedGroceryList(): Flow<List<GroceryItem>>
}
