package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consistency")
data class ConsistencyEntity(
    @PrimaryKey val id: Long = 1L,
    val currentStreak: Int = 1,
    val longestStreak: Int = 1,
    val totalWorkoutDays: Int = 1,
    val totalXp: Int = 120,
    val level: Int = 1,
    val levelTitle: String = "Apprentice",
    val lastWorkoutDate: Long? = null,
    val lastStreakDate: Long? = null
)
