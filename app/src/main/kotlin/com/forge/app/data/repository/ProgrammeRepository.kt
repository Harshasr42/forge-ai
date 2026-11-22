package com.forge.app.data.repository

import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.ProgrammeDayEntity
import com.forge.app.data.db.entities.ProgrammeDayExerciseEntity
import com.forge.app.data.db.entities.ProgrammeEntity
import kotlinx.coroutines.flow.Flow

interface ProgrammeRepository {
    fun getActiveProgramme(): Flow<ProgrammeEntity?>
    suspend fun getActiveProgrammeOnce(): ProgrammeEntity?
    fun getProgrammeDays(programmeId: Long): Flow<List<ProgrammeDayEntity>>
    suspend fun getProgrammeDaysOnce(programmeId: Long): List<ProgrammeDayEntity>
    suspend fun getProgrammeDayById(dayId: Long): ProgrammeDayEntity?
    suspend fun getProgrammeDayForWeekday(programmeId: Long, dayOfWeek: Int): ProgrammeDayEntity?
    suspend fun getExercisesForDay(dayId: Long): List<ProgrammeDayExerciseEntity>
    fun getExercisesForDayFlow(dayId: Long): Flow<List<ProgrammeDayExerciseEntity>>
    suspend fun getExerciseEntitiesForDay(dayId: Long): List<ExerciseEntity>
}
