package com.forge.app.data.repository

import com.forge.app.data.db.entities.SessionExerciseEntity
import com.forge.app.data.db.entities.SetRecordEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun startSession(session: WorkoutSessionEntity): Long
    suspend fun updateSession(session: WorkoutSessionEntity)
    suspend fun getActiveSession(): WorkoutSessionEntity?
    fun getActiveSessionFlow(): Flow<WorkoutSessionEntity?>
    fun getAllCompletedSessions(): Flow<List<WorkoutSessionEntity>>
    fun getCompletedSessionsInRange(startMillis: Long, endMillis: Long): Flow<List<WorkoutSessionEntity>>
    suspend fun getSessionById(sessionId: Long): WorkoutSessionEntity?
    suspend fun getSessionExercises(sessionId: Long): List<SessionExerciseEntity>
    fun getSessionExercisesFlow(sessionId: Long): Flow<List<SessionExerciseEntity>>
    suspend fun addSessionExercise(exercise: SessionExerciseEntity): Long
    suspend fun updateSessionExercise(exercise: SessionExerciseEntity)
    suspend fun logSet(set: SetRecordEntity): Long
    suspend fun updateSet(set: SetRecordEntity)
    suspend fun deleteSet(set: SetRecordEntity)
    suspend fun getSetsForExercise(sessionExerciseId: Long): List<SetRecordEntity>
    fun getSetsForExerciseFlow(sessionExerciseId: Long): Flow<List<SetRecordEntity>>
    suspend fun getPreviousSessionSets(exerciseId: Long): List<SetRecordEntity>
    suspend fun getMaxWeightForExercise(exerciseId: Long): Float?
    fun getRecentSessions(limit: Int = 30): Flow<List<WorkoutSessionEntity>>
    suspend fun getTotalWorkoutsCount(): Int
}
