package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.db.entities.MealFoodItemEntity
import com.forge.app.data.db.entities.MealLogEntity
import kotlinx.coroutines.flow.Flow

data class MealWithItems(
    @Embedded val mealLog: MealLogEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealLogId"
    )
    val foodItems: List<MealFoodItemEntity>
)

data class MacroTotals(
    val totalCalories: Float,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val totalFiber: Float
)

@Dao
interface NutritionLogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMealLog(mealLog: MealLogEntity): Long

    @Query("SELECT * FROM meal_log WHERE dateKey = :dateKey AND mealType = :mealType LIMIT 1")
    suspend fun getMealLog(dateKey: String, mealType: String): MealLogEntity?

    @Transaction
    @Query("SELECT * FROM meal_log WHERE dateKey = :dateKey")
    fun getMealsForDateFlow(dateKey: String): Flow<List<MealWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFoodItem(item: MealFoodItemEntity): Long

    @Update
    suspend fun updateMealFoodItem(item: MealFoodItemEntity)

    @Delete
    suspend fun deleteMealFoodItem(item: MealFoodItemEntity)

    @Query("DELETE FROM meal_food_item WHERE id = :id")
    suspend fun deleteMealFoodItemById(id: Long)

    @Query("""
        SELECT 
            COALESCE(SUM(calories), 0.0) as totalCalories,
            COALESCE(SUM(protein), 0.0) as totalProtein,
            COALESCE(SUM(carbs), 0.0) as totalCarbs,
            COALESCE(SUM(fat), 0.0) as totalFat,
            COALESCE(SUM(fiber), 0.0) as totalFiber
        FROM meal_food_item mfi
        INNER JOIN meal_log ml ON mfi.mealLogId = ml.id
        WHERE ml.dateKey = :dateKey
    """)
    fun getDailyTotalsFlow(dateKey: String): Flow<MacroTotals>

    @Query("""
        SELECT 
            COALESCE(SUM(calories), 0.0) as totalCalories,
            COALESCE(SUM(protein), 0.0) as totalProtein,
            COALESCE(SUM(carbs), 0.0) as totalCarbs,
            COALESCE(SUM(fat), 0.0) as totalFat,
            COALESCE(SUM(fiber), 0.0) as totalFiber
        FROM meal_food_item mfi
        INNER JOIN meal_log ml ON mfi.mealLogId = ml.id
        WHERE ml.dateKey = :dateKey
    """)
    suspend fun getDailyTotals(dateKey: String): MacroTotals

    // Target Management
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTarget(target: DailyNutritionTargetEntity)

    @Query("SELECT * FROM daily_nutrition_target WHERE id = 1")
    fun getTargetFlow(): Flow<DailyNutritionTargetEntity?>

    @Query("SELECT * FROM daily_nutrition_target WHERE id = 1")
    suspend fun getTarget(): DailyNutritionTargetEntity?
}
