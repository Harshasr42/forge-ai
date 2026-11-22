package com.forge.app.data.repository

import com.forge.app.data.db.dao.ExerciseHistoryPoint
import com.forge.app.data.db.entities.BodyMeasurementEntity
import com.forge.app.data.db.entities.PersonalRecordEntity
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    suspend fun getTotalVolumeInRange(startMillis: Long, endMillis: Long): Float?
    suspend fun getWorkoutCountInRange(startMillis: Long, endMillis: Long): Int
    suspend fun getTotalSetsInRange(startMillis: Long, endMillis: Long): Int
    suspend fun getTotalRepsInRange(startMillis: Long, endMillis: Long): Int?
    fun getTotalWorkoutsCompleted(): Flow<Int>
    fun getTotalVolumeLifted(): Flow<Float?>
    fun getAllPersonalRecords(): Flow<List<PersonalRecordEntity>>
    fun getPersonalRecordsForExercise(exerciseId: Long): Flow<List<PersonalRecordEntity>>
    fun getTopWeightRecords(): Flow<List<PersonalRecordEntity>>
    fun getRecentPersonalRecords(limit: Int): Flow<List<PersonalRecordEntity>>
    suspend fun detectAndSavePrsForSession(sessionId: Long)
    suspend fun getExerciseHistory(exerciseId: Long): List<ExerciseHistoryPoint>
    fun getAllBodyMeasurements(): Flow<List<BodyMeasurementEntity>>
    suspend fun getLatestBodyMeasurement(): BodyMeasurementEntity?
    suspend fun insertBodyMeasurement(measurement: BodyMeasurementEntity): Long
}
