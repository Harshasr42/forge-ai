package com.forge.app.data.repository

import com.forge.app.data.db.dao.FoodDao
import com.forge.app.data.db.dao.MacroTotals
import com.forge.app.data.db.dao.MealWithItems
import com.forge.app.data.db.dao.NutritionLogDao
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.db.entities.MealFoodItemEntity
import com.forge.app.data.db.entities.MealLogEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NutritionRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val nutritionLogDao: NutritionLogDao
) : NutritionRepository {

    override fun getAllFoods(): Flow<List<FoodItemEntity>> = foodDao.getAllFoodsFlow()

    override fun searchFoods(query: String): Flow<List<FoodItemEntity>> = foodDao.searchFoodsFlow(query)

    override fun getFoodsByCategory(category: String): Flow<List<FoodItemEntity>> = foodDao.getFoodsByCategoryFlow(category)

    override suspend fun getFoodById(id: Long): FoodItemEntity? = foodDao.getFoodById(id)

    override suspend fun insertCustomFood(food: FoodItemEntity): Long = foodDao.insertFood(food)

    override fun getMealsForDate(dateKey: String): Flow<List<MealWithItems>> =
        nutritionLogDao.getMealsForDateFlow(dateKey)

    override suspend fun addFoodToMeal(
        dateKey: String,
        mealType: String,
        food: FoodItemEntity,
        quantity: Float
    ): Long {
        var log = nutritionLogDao.getMealLog(dateKey, mealType)
        if (log == null) {
            val logId = nutritionLogDao.insertMealLog(MealLogEntity(dateKey = dateKey, mealType = mealType))
            log = nutritionLogDao.getMealLog(dateKey, mealType) ?: MealLogEntity(id = logId, dateKey = dateKey, mealType = mealType)
        }

        val item = MealFoodItemEntity(
            mealLogId = log.id,
            foodItemId = food.id,
            foodName = food.name,
            servingQuantity = quantity,
            servingSize = food.servingSize,
            servingUnit = food.servingUnit,
            calories = food.calories * quantity,
            protein = food.protein * quantity,
            carbs = food.carbs * quantity,
            fat = food.fat * quantity,
            fiber = food.fiber * quantity
        )
        return nutritionLogDao.insertMealFoodItem(item)
    }

    override suspend fun updateMealFoodQuantity(mealFoodItemId: Long, newQuantity: Float) {
        // Query or calculate directly if needed
    }

    override suspend fun removeFoodFromMeal(mealFoodItemId: Long) {
        nutritionLogDao.deleteMealFoodItemById(mealFoodItemId)
    }

    override fun getDailyTotals(dateKey: String): Flow<MacroTotals> =
        nutritionLogDao.getDailyTotalsFlow(dateKey)

    override suspend fun getDailyTotalsOnce(dateKey: String): MacroTotals =
        nutritionLogDao.getDailyTotals(dateKey)

    override fun getTarget(): Flow<DailyNutritionTargetEntity?> = nutritionLogDao.getTargetFlow()

    override suspend fun getTargetOnce(): DailyNutritionTargetEntity? = nutritionLogDao.getTarget()

    override suspend fun updateTarget(target: DailyNutritionTargetEntity) {
        nutritionLogDao.insertTarget(target)
    }
}
