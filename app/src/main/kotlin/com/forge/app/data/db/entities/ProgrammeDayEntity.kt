package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "programme_day",
    foreignKeys = [
        ForeignKey(
            entity = ProgrammeEntity::class,
            parentColumns = ["id"],
            childColumns = ["programmeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("programmeId")]
)
data class ProgrammeDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val programmeId: Long,
    val dayOfWeek: Int, // 1 = Mon, 2 = Tue, ..., 7 = Sun
    val workoutName: String,
    val muscleGroups: String, // comma-separated e.g. "Chest, Biceps, Forearms"
    val workoutVariant: String = "A", // "A", "B", "Recovery"
    val estimatedMinutes: Int = 65,
    val isRestDay: Boolean = false,
    val order: Int = 0,
    val notes: String = ""
)
