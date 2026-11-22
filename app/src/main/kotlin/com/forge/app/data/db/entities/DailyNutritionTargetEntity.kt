package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_nutrition_target")
data class DailyNutritionTargetEntity(
    @PrimaryKey val id: Long = 1L,
    val targetCalories: Float = 2200f,
    val targetProtein: Float = 140f,
    val targetCarbs: Float = 240f,
    val targetFat: Float = 65f,
    val targetFiber: Float = 30f,
    val waterTargetMl: Int = 3000,
    val goalType: String = "MAINTENANCE", // MAINTENANCE, BULK, CUT, CUSTOM
    val updatedAt: Long = System.currentTimeMillis()
)
