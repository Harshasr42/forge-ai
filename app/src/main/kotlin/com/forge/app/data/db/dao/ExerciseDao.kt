package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Query("SELECT * FROM exercise WHERE isHidden = 0 ORDER BY name ASC")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercise WHERE category = :category AND isHidden = 0 ORDER BY name ASC")
    fun getExercisesByCategory(category: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercise WHERE name LIKE '%' || :query || '%' AND isHidden = 0 ORDER BY name ASC")
    fun searchExercises(query: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercise WHERE isFavourite = 1 AND isHidden = 0 ORDER BY name ASC")
    fun getFavouriteExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercise WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): ExerciseEntity?

    @Query("SELECT * FROM exercise WHERE id = :exerciseId")
    fun getExerciseByIdFlow(exerciseId: Long): Flow<ExerciseEntity?>

    @Query("UPDATE exercise SET isFavourite = :isFavourite WHERE id = :exerciseId")
    suspend fun toggleFavourite(exerciseId: Long, isFavourite: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInstructions(instructions: List<ExerciseInstructionEntity>)

    @Query("SELECT * FROM exercise_instruction WHERE exerciseId = :exerciseId LIMIT 1")
    suspend fun getInstructionsForExercise(exerciseId: Long): ExerciseInstructionEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlternatives(alternatives: List<ExerciseAlternativeEntity>)

    @Query("""
        SELECT e.* FROM exercise e
        INNER JOIN exercise_alternative ea ON e.id = ea.alternativeExerciseId
        WHERE ea.primaryExerciseId = :exerciseId
    """)
    suspend fun getAlternativesForExercise(exerciseId: Long): List<ExerciseEntity>

    @Query("SELECT COUNT(*) FROM exercise")
    suspend fun getExerciseCount(): Int
}
