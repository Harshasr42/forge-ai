package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Session management
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Query("UPDATE workout_session SET status = 'ABANDONED' WHERE status = 'ACTIVE'")
    suspend fun markActiveSessionsAbandoned()

    @Update
    suspend fun updateSession(session: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_session WHERE status = 'ACTIVE' ORDER BY startedAt DESC LIMIT 1")
    suspend fun getActiveSession(): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_session WHERE status = 'ACTIVE' ORDER BY startedAt DESC LIMIT 1")
    fun getActiveSessionFlow(): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_session WHERE status = 'COMPLETED' ORDER BY startedAt DESC")
    fun getAllCompletedSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_session WHERE status = 'COMPLETED' AND (completedAt BETWEEN :startMillis AND :endMillis OR startedAt BETWEEN :startMillis AND :endMillis) ORDER BY startedAt ASC")
    fun getCompletedSessionsInRange(startMillis: Long, endMillis: Long): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_session WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): WorkoutSessionEntity?

    // Session exercises
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionExercise(exercise: SessionExerciseEntity): Long

    @Update
    suspend fun updateSessionExercise(exercise: SessionExerciseEntity)

    @Query("SELECT * FROM session_exercise WHERE sessionId = :sessionId ORDER BY orderInSession ASC")
    suspend fun getSessionExercises(sessionId: Long): List<SessionExerciseEntity>

    @Query("SELECT * FROM session_exercise WHERE sessionId = :sessionId ORDER BY orderInSession ASC")
    fun getSessionExercisesFlow(sessionId: Long): Flow<List<SessionExerciseEntity>>

    // Set records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetRecord(set: SetRecordEntity): Long

    @Update
    suspend fun updateSetRecord(set: SetRecordEntity)

    @Delete
    suspend fun deleteSetRecord(set: SetRecordEntity)

    @Query("SELECT * FROM set_record WHERE sessionExerciseId = :sessionExerciseId ORDER BY setNumber ASC")
    suspend fun getSetsForExercise(sessionExerciseId: Long): List<SetRecordEntity>

    @Query("SELECT * FROM set_record WHERE sessionExerciseId = :sessionExerciseId ORDER BY setNumber ASC")
    fun getSetsForExerciseFlow(sessionExerciseId: Long): Flow<List<SetRecordEntity>>

    @Query("""
        SELECT sr.* FROM set_record sr
        INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id
        INNER JOIN workout_session ws ON se.sessionId = ws.id
        WHERE se.exerciseId = :exerciseId AND ws.status = 'COMPLETED' AND sr.isCompleted = 1
        ORDER BY ws.startedAt DESC, sr.setNumber ASC
        LIMIT 10
    """)
    suspend fun getPreviousSessionSets(exerciseId: Long): List<SetRecordEntity>

    @Query("""
        SELECT MAX(sr.weightKg) FROM set_record sr
        INNER JOIN session_exercise se ON sr.sessionExerciseId = se.id
        INNER JOIN workout_session ws ON se.sessionId = ws.id
        WHERE se.exerciseId = :exerciseId AND sr.isCompleted = 1
    """)
    suspend fun getMaxWeightForExercise(exerciseId: Long): Float?

    @Query("SELECT * FROM workout_session ORDER BY startedAt DESC LIMIT :limit")
    fun getRecentSessions(limit: Int = 30): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT COUNT(*) FROM workout_session WHERE status = 'COMPLETED'")
    suspend fun getTotalWorkoutsCount(): Int
}
