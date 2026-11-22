package com.forge.app.ui.screens.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.repository.*
import com.forge.app.domain.CoachEngine
import com.forge.app.domain.CoachInsight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoachState(
    val insights: List<CoachInsight> = emptyList(),
    val streakDays: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val recoveryRepository: RecoveryRepository,
    private val goalRepository: GoalRepository,
    private val workoutRepository: WorkoutRepository,
    private val consistencyRepository: ConsistencyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoachState())
    val state: StateFlow<CoachState> = _state.asStateFlow()

    init {
        loadInsights()
    }

    fun refresh() {
        loadInsights()
    }

    private fun loadInsights() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val end = System.currentTimeMillis()
            val start30d = end - 30L * 86_400_000L

            val prs = progressRepository.getAllPersonalRecords().first()
            val goals = goalRepository.getActiveGoals().first()
            val checkIns = recoveryRepository.getRecentCheckIns(14).first()
            val sessions = workoutRepository.getCompletedSessionsInRange(start30d, end).first()
            val consistency = consistencyRepository.getConsistency().first()
            val streak = consistency?.currentStreak ?: 0

            val insights = CoachEngine.generateInsights(
                recentSessions = sessions,
                personalRecords = prs,
                activeGoals = goals,
                recentCheckIns = checkIns,
                currentStreakDays = streak
            )

            _state.value = CoachState(
                insights = insights,
                streakDays = streak,
                isLoading = false
            )
        }
    }
}
