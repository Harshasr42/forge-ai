package com.forge.app.data.repository

import com.forge.app.data.db.dao.ExerciseDao
import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.ExerciseInstructionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ExerciseRepository {
    override fun getAllExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getAllExercises()
    override fun getExercisesByCategory(category: String): Flow<List<ExerciseEntity>> = exerciseDao.getExercisesByCategory(category)
    override fun searchExercises(query: String): Flow<List<ExerciseEntity>> = exerciseDao.searchExercises(query)
    override fun getFavouriteExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getFavouriteExercises()
    override suspend fun getExerciseById(exerciseId: Long): ExerciseEntity? = exerciseDao.getExerciseById(exerciseId)
    override fun getExerciseByIdFlow(exerciseId: Long): Flow<ExerciseEntity?> = exerciseDao.getExerciseByIdFlow(exerciseId)
    override suspend fun toggleFavourite(exerciseId: Long, isFavourite: Boolean) = exerciseDao.toggleFavourite(exerciseId, isFavourite)
    override suspend fun getInstructionsForExercise(exerciseId: Long): ExerciseInstructionEntity? = exerciseDao.getInstructionsForExercise(exerciseId)
    override suspend fun getAlternativesForExercise(exerciseId: Long): List<ExerciseEntity> = exerciseDao.getAlternativesForExercise(exerciseId)
}
