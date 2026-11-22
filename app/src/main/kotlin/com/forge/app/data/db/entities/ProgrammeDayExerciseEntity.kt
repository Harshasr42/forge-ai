package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "programme_day_exercise",
    foreignKeys = [
        ForeignKey(
            entity = ProgrammeDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["programmeDayId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("programmeDayId"), Index("exerciseId")]
)
data class ProgrammeDayExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val programmeDayId: Long,
    val exerciseId: Long,
    val orderInWorkout: Int,
    val targetSets: Int = 3,
    val targetRepsMin: Int = 8,
    val targetRepsMax: Int = 12,
    val restSeconds: Int = 90,
    val isWarmupIncluded: Boolean = true,
    val notes: String = ""
)
