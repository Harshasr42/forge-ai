package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recovery_checkin")
data class RecoveryCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordedAt: Long = System.currentTimeMillis(),
    val dateKey: String = "",           // "yyyy-MM-dd" for deduplication
    val sleepHours: Float = 7f,         // 4–12
    val sleepQuality: Int = 3,          // 1–5
    val energyLevel: Int = 3,           // 1–5
    val muscleSoreness: Int = 2,        // 1–5 (5 = very sore)
    val motivation: Int = 3,            // 1–5
    val fatigue: Int = 2,               // 1–5 (5 = very fatigued)
    val soreMuscleGroups: String = "",   // comma-separated: "chest,shoulders,legs"
    val notes: String = ""
)
