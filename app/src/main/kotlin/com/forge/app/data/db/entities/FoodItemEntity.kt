package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_item")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // INDIAN, PROTEIN, DAIRY, GRAINS, FRUITS, VEGETABLES, SNACKS, CUSTOM
    val servingSize: Float = 100f,
    val servingUnit: String = "g", // g, ml, piece, cup, roti
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float = 0f,
    val isCustom: Boolean = false,
    val isVerified: Boolean = true,
    val notes: String = ""
)
