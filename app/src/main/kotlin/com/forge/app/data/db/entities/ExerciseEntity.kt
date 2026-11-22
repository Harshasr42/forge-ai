package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // CHEST, BACK, SHOULDERS, BICEPS, TRICEPS, FOREARMS, LEGS, ABS, OTHER
    val equipment: String, // BARBELL, DUMBBELL, CABLE, MACHINE, BODYWEIGHT, SMITH_MACHINE
    val muscleTarget: String,
    val muscleSecondary: String = "",
    val difficultyLevel: String = "INTERMEDIATE", // BEGINNER, INTERMEDIATE, ADVANCED
    val exerciseType: String = "ISOLATION", // COMPOUND, ISOLATION
    val isFavourite: Boolean = false,
    val isCustom: Boolean = false,
    val isHidden: Boolean = false
)
