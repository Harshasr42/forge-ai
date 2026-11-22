package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_session",
    foreignKeys = [
        ForeignKey(
            entity = ProgrammeDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["programmeDayId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("programmeDayId")]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val programmeDayId: Long? = null,
    val workoutName: String,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val durationSeconds: Long = 0,
    val status: String = "ACTIVE", // ACTIVE, COMPLETED, ABANDONED
    val totalVolume: Float = 0f,
    val totalSets: Int = 0,
    val rating: Int = 0,
    val notes: String = "",
    val personalRecordsAchieved: Int = 0
)
