package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Long = 1L,
    val name: String = "Athlete",
    val age: Int = 22,
    val heightCm: Float = 175f,
    val weightKg: Float = 70f,
    val experienceLevel: String = "INTERMEDIATE", // BEGINNER, INTERMEDIATE, ADVANCED
    val primaryGoal: String = "BUILD_STRENGTH",
    val preferredWorkoutTime: String = "MORNING",
    val useKg: Boolean = true,
    val onboardingComplete: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
