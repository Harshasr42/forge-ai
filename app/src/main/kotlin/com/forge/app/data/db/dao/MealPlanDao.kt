package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.MealPlanDayEntity
import kotlinx.coroutines.flow.Flow

data class GroceryItem(
    val foodName: String,
    val totalQuantity: Float,
    val servingUnit: String
)

@Dao
interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlanItem(item: MealPlanDayEntity): Long

    @Query("SELECT * FROM meal_plan_day WHERE dayOfWeek = :dayOfWeek ORDER BY id ASC")
    fun getPlanForDayFlow(dayOfWeek: Int): Flow<List<MealPlanDayEntity>>

    @Query("SELECT * FROM meal_plan_day ORDER BY dayOfWeek ASC, id ASC")
    fun getFullWeeklyPlanFlow(): Flow<List<MealPlanDayEntity>>

    @Query("DELETE FROM meal_plan_day WHERE id = :id")
    suspend fun deletePlanItem(id: Long)

    @Query("DELETE FROM meal_plan_day WHERE dayOfWeek = :dayOfWeek")
    suspend fun clearDayPlan(dayOfWeek: Int)

    @Query("""
        SELECT foodName, SUM(servingQuantity * servingSize) as totalQuantity, servingUnit
        FROM meal_plan_day
        GROUP BY foodName, servingUnit
        ORDER BY foodName ASC
    """)
    fun getAggregatedGroceryListFlow(): Flow<List<GroceryItem>>
}
