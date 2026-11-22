package com.forge.app.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hydration_log",
    indices = [Index("dateKey")]
)
data class HydrationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateKey: String, // "yyyy-MM-dd"
    val amountMl: Int,
    val loggedAt: Long = System.currentTimeMillis()
)
