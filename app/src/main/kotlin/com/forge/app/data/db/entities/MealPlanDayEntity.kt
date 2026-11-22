package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan_day")
data class MealPlanDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: Int, // 1 = Monday ... 7 = Sunday
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACKS
    val foodItemId: Long? = null,
    val foodName: String,
    val servingQuantity: Float = 1.0f,
    val servingSize: Float = 100f,
    val servingUnit: String = "g",
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float
)
