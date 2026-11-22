package com.forge.app.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.ConsistencyEntity
import com.forge.app.data.db.entities.ProgrammeDayEntity
import com.forge.app.data.db.entities.UserEntity
import com.forge.app.data.db.entities.WorkoutSessionEntity
import com.forge.app.data.preferences.ChecklistPreferences
import com.forge.app.data.repository.ConsistencyRepository
import com.forge.app.data.repository.ProgrammeRepository
import com.forge.app.data.repository.UserRepository
import com.forge.app.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class TodayUiState(
    val user: UserEntity? = null,
    val todayProgrammeDay: ProgrammeDayEntity? = null,
    val activeSession: WorkoutSessionEntity? = null,
    val consistency: ConsistencyEntity? = null,
    val totalExercises: Int = 8,
    val totalWorkingSets: Int = 24,
    val estimatedMinutes: Int = 65,
    val completedTasks: Set<String> = emptySet(),
    val isLoading: Boolean = false
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val programmeRepository: ProgrammeRepository,
    private val workoutRepository: WorkoutRepository,
    private val consistencyRepository: ConsistencyRepository,
    private val checklistPreferences: ChecklistPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState(isLoading = true))
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            val dateKey = getTodayDateKey()
            val savedTasks = checklistPreferences.getCompletedTasks(dateKey)

            val calendar = Calendar.getInstance()
            val dayOfWeekJava = calendar.get(Calendar.DAY_OF_WEEK)
            val currentDayOfWeek = if (dayOfWeekJava == Calendar.SUNDAY) 7 else dayOfWeekJava - 1

            combine(
                userRepository.getUser(),
                programmeRepository.getActiveProgramme(),
                workoutRepository.getActiveSessionFlow(),
                consistencyRepository.getConsistency()
            ) { user, programme, activeSession, consistency ->
                val programmeId = programme?.id ?: 1L
                val todayDay = programmeRepository.getProgrammeDayForWeekday(programmeId, currentDayOfWeek)
                val exercises = todayDay?.let { programmeRepository.getExercisesForDay(it.id) } ?: emptyList()
                val totalSets = exercises.sumOf { it.targetSets }

                TodayUiState(
                    user = user,
                    todayProgrammeDay = todayDay,
                    activeSession = activeSession,
                    consistency = consistency,
                    totalExercises = if (exercises.isNotEmpty()) exercises.size else 8,
                    totalWorkingSets = if (totalSets > 0) totalSets else 24,
                    estimatedMinutes = todayDay?.estimatedMinutes ?: 65,
                    completedTasks = savedTasks,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun toggleTask(taskId: String) {
        val dateKey = getTodayDateKey()
        val updated = checklistPreferences.toggleTask(dateKey, taskId)
        _uiState.update { it.copy(completedTasks = updated) }
    }
}
