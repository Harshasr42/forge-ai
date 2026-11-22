package com.forge.app.ui.screens.consistency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.AchievementEntity
import com.forge.app.data.db.entities.ConsistencyEntity
import com.forge.app.data.repository.ConsistencyRepository
import com.forge.app.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class WeekDayActivity(
    val dayOfWeek: Int, // 1=Mon..7=Sun
    val dayLabel: String,
    val dayNumber: Int,
    val isToday: Boolean,
    val isCompleted: Boolean,
    val isFuture: Boolean
)

data class ConsistencyUiState(
    val consistency: ConsistencyEntity? = null,
    val achievements: List<AchievementEntity> = emptyList(),
    val weeklyActivity: List<WeekDayActivity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ConsistencyViewModel @Inject constructor(
    private val consistencyRepository: ConsistencyRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsistencyUiState())
    val uiState: StateFlow<ConsistencyUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Determine the start and end of the current week (Monday 00:00 to Sunday 23:59:59)
            val now = Calendar.getInstance()
            val todayDayOfYear = now.get(Calendar.DAY_OF_YEAR)
            val todayYear = now.get(Calendar.YEAR)

            val mondayCal = Calendar.getInstance().apply {
                firstDayOfWeek = Calendar.MONDAY
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val sundayCal = (mondayCal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, 6)
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }

            combine(
                consistencyRepository.getConsistency(),
                consistencyRepository.getAllAchievements(),
                workoutRepository.getCompletedSessionsInRange(mondayCal.timeInMillis, sundayCal.timeInMillis)
            ) { consistency, achievements, completedSessions ->
                val dayLabels = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
                val activityList = (0..6).map { i ->
                    val dayCal = (mondayCal.clone() as Calendar).apply {
                        add(Calendar.DAY_OF_YEAR, i)
                    }
                    val dayYear = dayCal.get(Calendar.YEAR)
                    val dayOfYear = dayCal.get(Calendar.DAY_OF_YEAR)
                    val isToday = dayYear == todayYear && dayOfYear == todayDayOfYear
                    val isFuture = dayCal.after(now) && !isToday

                    val hasCompletedWorkout = completedSessions.any { session ->
                        val sessionTimestamp = session.completedAt ?: session.startedAt
                        val sessionCal = Calendar.getInstance().apply { timeInMillis = sessionTimestamp }
                        sessionCal.get(Calendar.YEAR) == dayYear && sessionCal.get(Calendar.DAY_OF_YEAR) == dayOfYear
                    }

                    WeekDayActivity(
                        dayOfWeek = i + 1,
                        dayLabel = dayLabels[i],
                        dayNumber = dayCal.get(Calendar.DAY_OF_MONTH),
                        isToday = isToday,
                        isCompleted = hasCompletedWorkout,
                        isFuture = isFuture
                    )
                }

                ConsistencyUiState(
                    consistency = consistency,
                    achievements = achievements,
                    weeklyActivity = activityList,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}
