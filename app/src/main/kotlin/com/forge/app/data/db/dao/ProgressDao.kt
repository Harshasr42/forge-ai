package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("""
        SELECT MAX(sr.weightKg) FROM set_record sr
        INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id
        WHERE se.exerciseId = :exerciseId AND sr.isCompleted = 1
    """)
    fun getMaxWeightFlow(exerciseId: Long): Flow<Float?>

    @Query("SELECT COUNT(*) FROM workout_session WHERE status = 'COMPLETED'")
    fun getTotalWorkoutsCompleted(): Flow<Int>

    @Query("SELECT SUM(totalVolume) FROM workout_session WHERE status = 'COMPLETED'")
    fun getTotalVolumeLifted(): Flow<Float?>

    @Query("SELECT * FROM workout_session WHERE status = 'COMPLETED' ORDER BY startedAt DESC LIMIT 20")
    fun getRecentWorkouts(): Flow<List<WorkoutSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurement(measurement: BodyMeasurementEntity): Long

    @Query("SELECT * FROM body_measurement ORDER BY recordedAt DESC")
    fun getAllBodyMeasurements(): Flow<List<BodyMeasurementEntity>>

    @Query("SELECT * FROM body_measurement ORDER BY recordedAt DESC LIMIT 1")
    suspend fun getLatestBodyMeasurement(): BodyMeasurementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity): Long

    @Query("SELECT * FROM goal WHERE status = 'ACTIVE' ORDER BY createdAt DESC")
    fun getActiveGoals(): Flow<List<GoalEntity>>

    // Personal Records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecord(pr: PersonalRecordEntity): Long

    @Query("SELECT * FROM personal_record ORDER BY achievedAt DESC")
    fun getAllPersonalRecords(): Flow<List<PersonalRecordEntity>>

    @Query("SELECT * FROM personal_record WHERE exerciseId = :exerciseId ORDER BY achievedAt DESC")
    fun getPersonalRecordsForExercise(exerciseId: Long): Flow<List<PersonalRecordEntity>>

    @Query("SELECT * FROM personal_record WHERE recordType = 'WEIGHT' ORDER BY weightKg DESC LIMIT 10")
    fun getTopWeightRecords(): Flow<List<PersonalRecordEntity>>

    @Query("SELECT * FROM personal_record WHERE exerciseId = :exerciseId AND recordType = 'ONE_RM' ORDER BY estimatedOneRm DESC LIMIT 1")
    suspend fun getBestOneRmForExercise(exerciseId: Long): PersonalRecordEntity?

    @Query("SELECT MAX(weightKg) FROM personal_record WHERE exerciseId = :exerciseId AND recordType = 'WEIGHT'")
    suspend fun getBestWeightForExercise(exerciseId: Long): Float?

    @Query("SELECT SUM(sr.weightKg * sr.reps) FROM set_record sr INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id INNER JOIN workout_session ws ON se.sessionId = ws.id WHERE ws.status = 'COMPLETED' AND ws.startedAt >= :startMillis AND ws.startedAt <= :endMillis AND sr.isCompleted = 1")
    suspend fun getTotalVolumeInRange(startMillis: Long, endMillis: Long): Float?

    @Query("SELECT COUNT(*) FROM workout_session WHERE status = 'COMPLETED' AND startedAt >= :startMillis AND startedAt <= :endMillis")
    suspend fun getWorkoutCountInRange(startMillis: Long, endMillis: Long): Int

    @Query("SELECT COUNT(*) FROM set_record sr INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id INNER JOIN workout_session ws ON se.sessionId = ws.id WHERE ws.status = 'COMPLETED' AND ws.startedAt >= :startMillis AND ws.startedAt <= :endMillis AND sr.isCompleted = 1")
    suspend fun getTotalSetsInRange(startMillis: Long, endMillis: Long): Int

    @Query("SELECT SUM(sr.reps) FROM set_record sr INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id INNER JOIN workout_session ws ON se.sessionId = ws.id WHERE ws.status = 'COMPLETED' AND ws.startedAt >= :startMillis AND ws.startedAt <= :endMillis AND sr.isCompleted = 1")
    suspend fun getTotalRepsInRange(startMillis: Long, endMillis: Long): Int?

    @Query("SELECT ws.startedAt, MAX(sr.weightKg) as maxWeight, MAX(sr.reps) as maxReps FROM set_record sr INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id INNER JOIN workout_session ws ON se.sessionId = ws.id WHERE se.exerciseId = :exerciseId AND ws.status = 'COMPLETED' AND sr.isCompleted = 1 GROUP BY ws.id ORDER BY ws.startedAt ASC LIMIT 30")
    suspend fun getExerciseHistoryRaw(exerciseId: Long): List<ExerciseHistoryPoint>

    @Query("SELECT se.exerciseId, e.name as exerciseName, MAX(sr.weightKg) as maxWeight, MAX(sr.reps) as maxReps FROM set_record sr INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id INNER JOIN exercise e ON se.exerciseId = e.id WHERE se.sessionId = :sessionId AND sr.isCompleted = 1 GROUP BY se.exerciseId")
    suspend fun getSessionBestSets(sessionId: Long): List<SessionBestSet>

    @Query("SELECT * FROM personal_record ORDER BY achievedAt DESC LIMIT :limit")
    fun getRecentPersonalRecords(limit: Int = 5): Flow<List<PersonalRecordEntity>>
}

data class ExerciseHistoryPoint(val startedAt: Long, val maxWeight: Float, val maxReps: Int)
data class SessionBestSet(val exerciseId: Long, val exerciseName: String, val maxWeight: Float, val maxReps: Int)
