package com.forge.app.data.repository

import com.forge.app.data.db.dao.WorkoutDao
import com.forge.app.data.db.entities.SessionExerciseEntity
import com.forge.app.data.db.entities.SetRecordEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {
    override suspend fun startSession(session: WorkoutSessionEntity): Long {
        workoutDao.markActiveSessionsAbandoned()
        return workoutDao.insertSession(session)
    }
    override suspend fun updateSession(session: WorkoutSessionEntity) = workoutDao.updateSession(session)
    override suspend fun getActiveSession(): WorkoutSessionEntity? = workoutDao.getActiveSession()
    override fun getActiveSessionFlow(): Flow<WorkoutSessionEntity?> = workoutDao.getActiveSessionFlow()
    override fun getAllCompletedSessions(): Flow<List<WorkoutSessionEntity>> = workoutDao.getAllCompletedSessions()
    override fun getCompletedSessionsInRange(startMillis: Long, endMillis: Long): Flow<List<WorkoutSessionEntity>> =
        workoutDao.getCompletedSessionsInRange(startMillis, endMillis)
    override suspend fun getSessionById(sessionId: Long): WorkoutSessionEntity? = workoutDao.getSessionById(sessionId)
    override suspend fun getSessionExercises(sessionId: Long): List<SessionExerciseEntity> = workoutDao.getSessionExercises(sessionId)
    override fun getSessionExercisesFlow(sessionId: Long): Flow<List<SessionExerciseEntity>> = workoutDao.getSessionExercisesFlow(sessionId)
    override suspend fun addSessionExercise(exercise: SessionExerciseEntity): Long = workoutDao.insertSessionExercise(exercise)
    override suspend fun updateSessionExercise(exercise: SessionExerciseEntity) = workoutDao.updateSessionExercise(exercise)
    override suspend fun logSet(set: SetRecordEntity): Long = workoutDao.insertSetRecord(set)
    override suspend fun updateSet(set: SetRecordEntity) = workoutDao.updateSetRecord(set)
    override suspend fun deleteSet(set: SetRecordEntity) = workoutDao.deleteSetRecord(set)
    override suspend fun getSetsForExercise(sessionExerciseId: Long): List<SetRecordEntity> = workoutDao.getSetsForExercise(sessionExerciseId)
    override fun getSetsForExerciseFlow(sessionExerciseId: Long): Flow<List<SetRecordEntity>> = workoutDao.getSetsForExerciseFlow(sessionExerciseId)
    override suspend fun getPreviousSessionSets(exerciseId: Long): List<SetRecordEntity> = workoutDao.getPreviousSessionSets(exerciseId)
    override suspend fun getMaxWeightForExercise(exerciseId: Long): Float? = workoutDao.getMaxWeightForExercise(exerciseId)
    override fun getRecentSessions(limit: Int): Flow<List<WorkoutSessionEntity>> = workoutDao.getRecentSessions(limit)
    override suspend fun getTotalWorkoutsCount(): Int = workoutDao.getTotalWorkoutsCount()
}
