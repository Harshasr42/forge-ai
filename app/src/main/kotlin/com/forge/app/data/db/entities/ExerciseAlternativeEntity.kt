package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_alternative",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["primaryExerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["alternativeExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("primaryExerciseId"), Index("alternativeExerciseId")]
)
data class ExerciseAlternativeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val primaryExerciseId: Long,
    val alternativeExerciseId: Long
)
