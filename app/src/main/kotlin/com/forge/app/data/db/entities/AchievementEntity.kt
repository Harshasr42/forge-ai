package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement")
data class AchievementEntity(
    @PrimaryKey val id: String, // e.g. "FIRST_WORKOUT", "STREAK_7"
    val title: String,
    val description: String,
    val iconName: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val category: String = "GENERAL" // GENERAL, STREAK, STRENGTH, VOLUME, CONSISTENCY
)
