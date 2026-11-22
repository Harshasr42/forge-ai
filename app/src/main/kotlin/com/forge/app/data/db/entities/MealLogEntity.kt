package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_log",
    indices = [Index(value = ["dateKey", "mealType"], unique = true)]
)
data class MealLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String, // "yyyy-MM-dd"
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACKS
    val loggedAt: Long = System.currentTimeMillis()
)
