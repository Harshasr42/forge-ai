package com.forge.app.data.repository

import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.ExerciseInstructionEntity
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<ExerciseEntity>>
    fun getExercisesByCategory(category: String): Flow<List<ExerciseEntity>>
    fun searchExercises(query: String): Flow<List<ExerciseEntity>>
    fun getFavouriteExercises(): Flow<List<ExerciseEntity>>
    suspend fun getExerciseById(exerciseId: Long): ExerciseEntity?
    fun getExerciseByIdFlow(exerciseId: Long): Flow<ExerciseEntity?>
    suspend fun toggleFavourite(exerciseId: Long, isFavourite: Boolean)
    suspend fun getInstructionsForExercise(exerciseId: Long): ExerciseInstructionEntity?
    suspend fun getAlternativesForExercise(exerciseId: Long): List<ExerciseEntity>
}
