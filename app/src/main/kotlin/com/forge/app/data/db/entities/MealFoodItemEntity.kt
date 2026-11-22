package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_food_item",
    foreignKeys = [
        ForeignKey(
            entity = MealLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["mealLogId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mealLogId")]
)
data class MealFoodItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mealLogId: Long,
    val foodItemId: Long? = null,
    val foodName: String,
    val servingQuantity: Float = 1.0f, // multiplier of base servingSize
    val servingSize: Float = 100f,
    val servingUnit: String = "g",
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float = 0f
)
