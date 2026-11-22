package com.forge.app.data.db.dao

import androidx.room.*
import com.forge.app.data.db.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgrammeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramme(programme: ProgrammeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgrammeDay(day: ProgrammeDayEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgrammeDayExercise(pdExercise: ProgrammeDayExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgrammeDayExercises(list: List<ProgrammeDayExerciseEntity>)

    @Query("SELECT * FROM programme WHERE isActive = 1 ORDER BY createdAt DESC LIMIT 1")
    fun getActiveProgramme(): Flow<ProgrammeEntity?>

    @Query("SELECT * FROM programme WHERE isActive = 1 ORDER BY createdAt DESC LIMIT 1")
    suspend fun getActiveProgrammeOnce(): ProgrammeEntity?

    @Query("SELECT * FROM programme_day WHERE programmeId = :programmeId ORDER BY dayOfWeek ASC")
    fun getProgrammeDays(programmeId: Long): Flow<List<ProgrammeDayEntity>>

    @Query("SELECT * FROM programme_day WHERE programmeId = :programmeId ORDER BY dayOfWeek ASC")
    suspend fun getProgrammeDaysOnce(programmeId: Long): List<ProgrammeDayEntity>

    @Query("SELECT * FROM programme_day WHERE id = :dayId")
    suspend fun getProgrammeDayById(dayId: Long): ProgrammeDayEntity?

    @Query("SELECT * FROM programme_day WHERE programmeId = :programmeId AND dayOfWeek = :dayOfWeek LIMIT 1")
    suspend fun getProgrammeDayForWeekday(programmeId: Long, dayOfWeek: Int): ProgrammeDayEntity?

    @Query("SELECT * FROM programme_day_exercise WHERE programmeDayId = :dayId ORDER BY orderInWorkout ASC")
    suspend fun getExercisesForDay(dayId: Long): List<ProgrammeDayExerciseEntity>

    @Query("SELECT * FROM programme_day_exercise WHERE programmeDayId = :dayId ORDER BY orderInWorkout ASC")
    fun getExercisesForDayFlow(dayId: Long): Flow<List<ProgrammeDayExerciseEntity>>

    @Query("""
        SELECT e.* FROM exercise e
        INNER JOIN programme_day_exercise pde ON e.id = pde.exerciseId
        WHERE pde.programmeDayId = :dayId
        ORDER BY pde.orderInWorkout ASC
    """)
    suspend fun getExerciseEntitiesForDay(dayId: Long): List<ExerciseEntity>
}
