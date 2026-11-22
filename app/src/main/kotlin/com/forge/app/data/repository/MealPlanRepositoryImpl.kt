package com.forge.app.data.repository

import com.forge.app.data.db.dao.GroceryItem
import com.forge.app.data.db.dao.MealPlanDao
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.db.entities.MealPlanDayEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealPlanRepositoryImpl @Inject constructor(
    private val mealPlanDao: MealPlanDao
) : MealPlanRepository {

    override fun getPlanForDay(dayOfWeek: Int): Flow<List<MealPlanDayEntity>> =
        mealPlanDao.getPlanForDayFlow(dayOfWeek)

    override fun getFullWeeklyPlan(): Flow<List<MealPlanDayEntity>> =
        mealPlanDao.getFullWeeklyPlanFlow()

    override suspend fun addFoodToPlan(
        dayOfWeek: Int,
        mealType: String,
        food: FoodItemEntity,
        quantity: Float
    ): Long {
        return mealPlanDao.insertMealPlanItem(
            MealPlanDayEntity(
                dayOfWeek = dayOfWeek,
                mealType = mealType,
                foodItemId = food.id,
                foodName = food.name,
                servingQuantity = quantity,
                servingSize = food.servingSize,
                servingUnit = food.servingUnit,
                calories = food.calories * quantity,
                protein = food.protein * quantity,
                carbs = food.carbs * quantity,
                fat = food.fat * quantity
            )
        )
    }

    override suspend fun removeFoodFromPlan(id: Long) = mealPlanDao.deletePlanItem(id)

    override suspend fun clearDayPlan(dayOfWeek: Int) = mealPlanDao.clearDayPlan(dayOfWeek)

    override fun getAggregatedGroceryList(): Flow<List<GroceryItem>> =
        mealPlanDao.getAggregatedGroceryListFlow()
}
