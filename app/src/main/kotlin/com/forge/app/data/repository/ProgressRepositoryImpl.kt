package com.forge.app.data.repository

import com.forge.app.data.db.dao.ExerciseHistoryPoint
import com.forge.app.data.db.dao.ProgressDao
import com.forge.app.data.db.entities.BodyMeasurementEntity
import com.forge.app.data.db.entities.PersonalRecordEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao
) : ProgressRepository {

    override suspend fun getTotalVolumeInRange(startMillis: Long, endMillis: Long): Float? =
        progressDao.getTotalVolumeInRange(startMillis, endMillis)

    override suspend fun getWorkoutCountInRange(startMillis: Long, endMillis: Long): Int =
        progressDao.getWorkoutCountInRange(startMillis, endMillis)

    override suspend fun getTotalSetsInRange(startMillis: Long, endMillis: Long): Int =
        progressDao.getTotalSetsInRange(startMillis, endMillis)

    override suspend fun getTotalRepsInRange(startMillis: Long, endMillis: Long): Int? =
        progressDao.getTotalRepsInRange(startMillis, endMillis)

    override fun getTotalWorkoutsCompleted(): Flow<Int> =
        progressDao.getTotalWorkoutsCompleted()

    override fun getTotalVolumeLifted(): Flow<Float?> =
        progressDao.getTotalVolumeLifted()

    override fun getAllPersonalRecords(): Flow<List<PersonalRecordEntity>> =
        progressDao.getAllPersonalRecords()

    override fun getPersonalRecordsForExercise(exerciseId: Long): Flow<List<PersonalRecordEntity>> =
        progressDao.getPersonalRecordsForExercise(exerciseId)

    override fun getTopWeightRecords(): Flow<List<PersonalRecordEntity>> =
        progressDao.getTopWeightRecords()

    override fun getRecentPersonalRecords(limit: Int): Flow<List<PersonalRecordEntity>> =
        progressDao.getRecentPersonalRecords(limit)

    override suspend fun detectAndSavePrsForSession(sessionId: Long) {
        val bestSets = progressDao.getSessionBestSets(sessionId)
        for (best in bestSets) {
            val weight = best.maxWeight
            val reps = best.maxReps
            val existingBest = progressDao.getBestWeightForExercise(best.exerciseId) ?: 0f
            if (weight > existingBest) {
                progressDao.insertPersonalRecord(PersonalRecordEntity(
                    exerciseId = best.exerciseId, exerciseName = best.exerciseName,
                    weightKg = weight, reps = reps,
                    estimatedOneRm = if (reps in 1..12) weight * (1 + reps / 30f) else 0f,
                    totalVolume = weight * reps, recordType = "WEIGHT",
                    achievedAt = System.currentTimeMillis(), sessionId = sessionId
                ))
            }
            if (reps in 1..12 && weight > 0f) {
                val newOneRm = weight * (1 + reps / 30f)
                val existingOneRm = progressDao.getBestOneRmForExercise(best.exerciseId)?.estimatedOneRm ?: 0f
                if (newOneRm > existingOneRm) {
                    progressDao.insertPersonalRecord(PersonalRecordEntity(
                        exerciseId = best.exerciseId, exerciseName = best.exerciseName,
                        weightKg = weight, reps = reps, estimatedOneRm = newOneRm,
                        totalVolume = weight * reps, recordType = "ONE_RM",
                        achievedAt = System.currentTimeMillis(), sessionId = sessionId
                    ))
                }
            }
        }
    }

    override suspend fun getExerciseHistory(exerciseId: Long): List<ExerciseHistoryPoint> =
        progressDao.getExerciseHistoryRaw(exerciseId)

    override fun getAllBodyMeasurements(): Flow<List<BodyMeasurementEntity>> =
        progressDao.getAllBodyMeasurements()

    override suspend fun getLatestBodyMeasurement(): BodyMeasurementEntity? =
        progressDao.getLatestBodyMeasurement()

    override suspend fun insertBodyMeasurement(measurement: BodyMeasurementEntity): Long =
        progressDao.insertBodyMeasurement(measurement)
}
