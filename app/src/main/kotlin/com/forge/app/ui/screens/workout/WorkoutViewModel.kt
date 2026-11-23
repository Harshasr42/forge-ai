package com.forge.app.ui.screens.workout

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.*
import com.forge.app.data.repository.*
import com.forge.app.service.WorkoutTimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseWithSets(
    val exercise: ExerciseEntity,
    val sessionExerciseId: Long,
    val sets: List<SetRecordEntity>,
    val previousPerformance: List<SetRecordEntity> = emptyList(),
    val instructions: ExerciseInstructionEntity? = null,
    val alternatives: List<ExerciseEntity> = emptyList()
)

data class WorkoutExecutionUiState(
    val sessionId: Long = 0L,
    val workoutName: String = "Workout Session",
    val currentExerciseIndex: Int = 0,
    val exercises: List<ExerciseWithSets> = emptyList(),
    val durationSeconds: Long = 0L,
    val isRestTimerActive: Boolean = false,
    val restSecondsRemaining: Int = 0,
    val isFinished: Boolean = false,
    val totalVolume: Float = 0f,
    val totalCompletedSets: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    application: Application,
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val programmeRepository: ProgrammeRepository,
    private val consistencyRepository: ConsistencyRepository,
    private val progressRepository: ProgressRepository,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(WorkoutExecutionUiState())
    val uiState: StateFlow<WorkoutExecutionUiState> = _uiState.asStateFlow()

    private val requestedDayId: Long = savedStateHandle.get<String>("dayId")?.toLongOrNull() ?: 1L

    init {
        observeTimerService()
        initializeWorkout()
    }

    private fun observeTimerService() {
        viewModelScope.launch {
            WorkoutTimerService.workoutDurationSeconds.collect { duration ->
                _uiState.update { it.copy(durationSeconds = duration) }
            }
        }
        viewModelScope.launch {
            WorkoutTimerService.restSecondsRemaining.collect { remaining ->
                _uiState.update { it.copy(restSecondsRemaining = remaining) }
            }
        }
        viewModelScope.launch {
            WorkoutTimerService.isRestActive.collect { isActive ->
                _uiState.update { it.copy(isRestTimerActive = isActive) }
            }
        }
    }

    private fun initializeWorkout() {
        viewModelScope.launch {
            // Check for active session first (persistence rule)
            var session = workoutRepository.getActiveSession()

            if (session == null) {
                // Start a new session from programme day
                val day = programmeRepository.getProgrammeDayById(requestedDayId)
                    ?: programmeRepository.getProgrammeDaysOnce(1L).firstOrNull()

                val workoutName = day?.workoutName ?: "Workout"
                val newSession = WorkoutSessionEntity(
                    programmeDayId = day?.id,
                    workoutName = workoutName,
                    startedAt = System.currentTimeMillis(),
                    status = "ACTIVE"
                )
                val newSessionId = workoutRepository.startSession(newSession)
                session = newSession.copy(id = newSessionId)

                // Start Foreground Timer Service
                val startIntent = Intent(getApplication(), WorkoutTimerService::class.java).apply {
                    action = WorkoutTimerService.ACTION_START_WORKOUT
                    putExtra(WorkoutTimerService.EXTRA_WORKOUT_NAME, workoutName)
                }
                safeStartWorkoutService(startIntent)

                // Populate session exercises from programme day
                val programmeExercises = day?.let { programmeRepository.getExercisesForDay(it.id) } ?: emptyList()
                programmeExercises.forEachIndexed { index, pde ->
                    val sessionExerciseId = workoutRepository.addSessionExercise(
                        SessionExerciseEntity(
                            sessionId = newSessionId,
                            exerciseId = pde.exerciseId,
                            orderInSession = index + 1
                        )
                    )
                    // Create default sets for this exercise
                    for (setNum in 1..pde.targetSets) {
                        workoutRepository.logSet(
                            SetRecordEntity(
                                sessionExerciseId = sessionExerciseId,
                                setNumber = setNum,
                                weightKg = 30f,
                                reps = pde.targetRepsMin,
                                restAfterSeconds = pde.restSeconds
                            )
                        )
                    }
                }
            } else {
                // Resume existing session
                val elapsedSeconds = maxOf(0L, (System.currentTimeMillis() - session.startedAt) / 1000)
                _uiState.update { it.copy(durationSeconds = elapsedSeconds) }

                if (!WorkoutTimerService.isWorkoutActive.value) {
                    val resumeIntent = Intent(getApplication(), WorkoutTimerService::class.java).apply {
                        action = WorkoutTimerService.ACTION_START_WORKOUT
                        putExtra(WorkoutTimerService.EXTRA_WORKOUT_NAME, session.workoutName)
                        putExtra(WorkoutTimerService.EXTRA_INITIAL_DURATION_SECONDS, elapsedSeconds)
                    }
                    safeStartWorkoutService(resumeIntent)
                }
            }

            loadSessionData(session.id, session.workoutName)
        }
    }

    private suspend fun loadSessionData(sessionId: Long, workoutName: String) {
        val sessionExercises = workoutRepository.getSessionExercises(sessionId)
        val exerciseList = mutableListOf<ExerciseWithSets>()

        for (se in sessionExercises) {
            val exerciseEntity = exerciseRepository.getExerciseById(se.exerciseId) ?: continue
            val sets = workoutRepository.getSetsForExercise(se.id)
            val previousSets = workoutRepository.getPreviousSessionSets(se.exerciseId)
            val instructions = exerciseRepository.getInstructionsForExercise(se.exerciseId)
            val alternatives = exerciseRepository.getAlternativesForExercise(se.exerciseId)

            exerciseList.add(
                ExerciseWithSets(
                    exercise = exerciseEntity,
                    sessionExerciseId = se.id,
                    sets = sets,
                    previousPerformance = previousSets,
                    instructions = instructions,
                    alternatives = alternatives
                )
            )
        }

        val completedCount = exerciseList.sumOf { it.sets.count { set -> set.isCompleted } }
        val volume = exerciseList.sumOf {
            it.sets.filter { s -> s.isCompleted }.sumOf { s -> (s.weightKg * s.reps).toDouble() }
        }.toFloat()

        _uiState.update {
            it.copy(
                sessionId = sessionId,
                workoutName = workoutName,
                exercises = exerciseList,
                totalCompletedSets = completedCount,
                totalVolume = volume,
                isLoading = false
            )
        }
    }

    fun selectExercise(index: Int) {
        if (index in 0 until _uiState.value.exercises.size) {
            _uiState.update { it.copy(currentExerciseIndex = index) }
        }
    }

    fun nextExercise() {
        val next = _uiState.value.currentExerciseIndex + 1
        if (next < _uiState.value.exercises.size) {
            _uiState.update { it.copy(currentExerciseIndex = next) }
        }
    }

    fun previousExercise() {
        val prev = _uiState.value.currentExerciseIndex - 1
        if (prev >= 0) {
            _uiState.update { it.copy(currentExerciseIndex = prev) }
        }
    }

    fun swapExercise(oldExerciseId: Long, newExerciseId: Long) {
        viewModelScope.launch {
            val sessionExercises = workoutRepository.getSessionExercises(_uiState.value.sessionId)
            val target = sessionExercises.find { it.exerciseId == oldExerciseId } ?: return@launch
            workoutRepository.updateSessionExercise(target.copy(exerciseId = newExerciseId))
            reloadCurrentSession()
        }
    }

    fun updateSet(setRecord: SetRecordEntity, newWeight: Float, newReps: Int) {
        viewModelScope.launch {
            val updated = setRecord.copy(weightKg = newWeight, reps = newReps)
            workoutRepository.updateSet(updated)
            reloadCurrentSession()
        }
    }

    fun toggleSetCompletion(setRecord: SetRecordEntity) {
        viewModelScope.launch {
            val isNowCompleted = !setRecord.isCompleted
            val updated = setRecord.copy(
                isCompleted = isNowCompleted,
                completedAt = if (isNowCompleted) System.currentTimeMillis() else null
            )
            workoutRepository.updateSet(updated)

            if (isNowCompleted) {
                // Trigger Rest Timer
                val restTime = if (setRecord.restAfterSeconds > 0) setRecord.restAfterSeconds else 90
                val restIntent = Intent(getApplication(), WorkoutTimerService::class.java).apply {
                    action = WorkoutTimerService.ACTION_START_REST
                    putExtra(WorkoutTimerService.EXTRA_REST_SECONDS, restTime)
                }
                safeStartService(restIntent)
            }

            reloadCurrentSession()
        }
    }

    fun addSetToCurrentExercise() {
        viewModelScope.launch {
            val currentExercise = _uiState.value.exercises.getOrNull(_uiState.value.currentExerciseIndex) ?: return@launch
            val nextSetNumber = (currentExercise.sets.maxOfOrNull { it.setNumber } ?: 0) + 1
            val lastSet = currentExercise.sets.lastOrNull()
            val newWeight = lastSet?.weightKg ?: 30f
            val newReps = lastSet?.reps ?: 10

            workoutRepository.logSet(
                SetRecordEntity(
                    sessionExerciseId = currentExercise.sessionExerciseId,
                    setNumber = nextSetNumber,
                    weightKg = newWeight,
                    reps = newReps,
                    restAfterSeconds = 90
                )
            )
            reloadCurrentSession()
        }
    }

    fun addRestSeconds(seconds: Int) {
        val intent = Intent(getApplication(), WorkoutTimerService::class.java).apply {
            action = WorkoutTimerService.ACTION_ADD_REST_TIME
            putExtra(WorkoutTimerService.EXTRA_ADD_SECONDS, seconds)
        }
        safeStartService(intent)
    }

    fun skipRest() {
        val intent = Intent(getApplication(), WorkoutTimerService::class.java).apply {
            action = WorkoutTimerService.ACTION_SKIP_REST
        }
        safeStartService(intent)
    }

    fun finishWorkout() {
        viewModelScope.launch {
            val session = workoutRepository.getSessionById(_uiState.value.sessionId) ?: return@launch
            val completedSession = session.copy(
                status = "COMPLETED",
                completedAt = System.currentTimeMillis(),
                durationSeconds = _uiState.value.durationSeconds,
                totalVolume = _uiState.value.totalVolume,
                totalSets = _uiState.value.totalCompletedSets
            )
            workoutRepository.updateSession(completedSession)

            // Stop service
            val stopIntent = Intent(getApplication(), WorkoutTimerService::class.java).apply {
                action = WorkoutTimerService.ACTION_STOP_WORKOUT
            }
            safeStartService(stopIntent)

            // Record streak & award XP
            consistencyRepository.recordWorkoutCompleted()

            // Detect and record new Personal Records
            try {
                progressRepository.detectAndSavePrsForSession(_uiState.value.sessionId)
            } catch (e: Exception) {
                // Non-fatal if PR detection fails
            }

            _uiState.update { it.copy(isFinished = true) }
        }
    }

    private fun safeStartWorkoutService(intent: Intent) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplication<Application>().startForegroundService(intent)
            } else {
                getApplication<Application>().startService(intent)
            }
        } catch (_: Exception) {
            // Guard against background start restrictions or missing notification permissions on Android 12-15
        }
    }

    private fun safeStartService(intent: Intent) {
        try {
            getApplication<Application>().startService(intent)
        } catch (_: Exception) {
            // Guard against ServiceStartNotAllowedException
        }
    }

    private suspend fun reloadCurrentSession() {
        loadSessionData(_uiState.value.sessionId, _uiState.value.workoutName)
    }
}
