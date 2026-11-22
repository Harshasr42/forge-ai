package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "personal_record",
    foreignKeys = [ForeignKey(entity = ExerciseEntity::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("exerciseId")]
)
data class PersonalRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: Long,
    val exerciseName: String = "",
    val weightKg: Float = 0f,
    val reps: Int = 0,
    val estimatedOneRm: Float = 0f,   // Epley: weight*(1+reps/30), only for reps<=12
    val totalVolume: Float = 0f,       // weightKg * reps
    val recordType: String = "WEIGHT", // WEIGHT, REPS, ONE_RM, VOLUME
    val achievedAt: Long = System.currentTimeMillis(),
    val sessionId: Long = 0
)
