package com.forge.app.data.repository

import com.forge.app.data.db.dao.ProgrammeDao
import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.ProgrammeDayEntity
import com.forge.app.data.db.entities.ProgrammeDayExerciseEntity
import com.forge.app.data.db.entities.ProgrammeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgrammeRepositoryImpl @Inject constructor(
    private val programmeDao: ProgrammeDao
) : ProgrammeRepository {
    override fun getActiveProgramme(): Flow<ProgrammeEntity?> = programmeDao.getActiveProgramme()
    override suspend fun getActiveProgrammeOnce(): ProgrammeEntity? = programmeDao.getActiveProgrammeOnce()
    override fun getProgrammeDays(programmeId: Long): Flow<List<ProgrammeDayEntity>> = programmeDao.getProgrammeDays(programmeId)
    override suspend fun getProgrammeDaysOnce(programmeId: Long): List<ProgrammeDayEntity> = programmeDao.getProgrammeDaysOnce(programmeId)
    override suspend fun getProgrammeDayById(dayId: Long): ProgrammeDayEntity? = programmeDao.getProgrammeDayById(dayId)
    override suspend fun getProgrammeDayForWeekday(programmeId: Long, dayOfWeek: Int): ProgrammeDayEntity? =
        programmeDao.getProgrammeDayForWeekday(programmeId, dayOfWeek)
    override suspend fun getExercisesForDay(dayId: Long): List<ProgrammeDayExerciseEntity> = programmeDao.getExercisesForDay(dayId)
    override fun getExercisesForDayFlow(dayId: Long): Flow<List<ProgrammeDayExerciseEntity>> = programmeDao.getExercisesForDayFlow(dayId)
    override suspend fun getExerciseEntitiesForDay(dayId: Long): List<ExerciseEntity> = programmeDao.getExerciseEntitiesForDay(dayId)
}
