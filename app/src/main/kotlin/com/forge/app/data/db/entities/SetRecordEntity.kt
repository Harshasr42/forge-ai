package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "set_record",
    foreignKeys = [
        ForeignKey(
            entity = SessionExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionExerciseId")]
)
data class SetRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionExerciseId: Long,
    val setNumber: Int,
    val weightKg: Float = 0f,
    val reps: Int = 0,
    val setType: String = "WORKING", // WARMUP, WORKING, DROP, FAILURE
    val isCompleted: Boolean = false,
    val isPr: Boolean = false,
    val restAfterSeconds: Int = 90,
    val completedAt: Long? = null,
    val notes: String = ""
)
