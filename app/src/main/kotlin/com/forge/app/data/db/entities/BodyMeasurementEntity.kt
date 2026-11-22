package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_measurement")
data class BodyMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordedAt: Long = System.currentTimeMillis(),
    val weightKg: Float? = null,
    val heightCm: Float? = null,
    val chestCm: Float? = null,
    val waistCm: Float? = null,
    val shoulderCm: Float? = null,
    val armCm: Float? = null,
    val forearmCm: Float? = null,
    val thighCm: Float? = null,
    val calfCm: Float? = null,
    val bodyFatPercent: Float? = null,
    val notes: String = ""
)
