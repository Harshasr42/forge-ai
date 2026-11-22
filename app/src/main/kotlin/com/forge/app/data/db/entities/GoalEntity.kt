package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // STRENGTH, WEIGHT, CONSISTENCY, ENDURANCE, CUSTOM
    val startValue: Float = 0f,
    val targetValue: Float,
    val currentValue: Float = 0f,
    val unit: String = "kg",
    val targetDate: Long? = null,
    val status: String = "ACTIVE", // ACTIVE, COMPLETED, ABANDONED
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
