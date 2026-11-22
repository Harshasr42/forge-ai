package com.forge.app.ui.screens.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.repository.GoalRepository
import com.forge.app.data.repository.ProgressRepository
import com.forge.app.data.repository.RecoveryRepository
import com.forge.app.data.repository.WorkoutRepository
import com.forge.app.domain.CoachEngine
import com.forge.app.domain.WeeklyReportData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyReportViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val recoveryRepository: RecoveryRepository,
    private val goalRepository: GoalRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _report = MutableStateFlow<WeeklyReportData?>(null)
    val report: StateFlow<WeeklyReportData?> = _report.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadReport()
    }

    fun refresh() {
        loadReport()
    }

    private fun loadReport() {
        viewModelScope.launch {
            _isLoading.value = true
            val now = System.currentTimeMillis()
            val weekStart = now - 7L * 86_400_000L
            val prevWeekStart = weekStart - 7L * 86_400_000L

            val thisWeek = workoutRepository.getCompletedSessionsInRange(weekStart, now).first()
            val lastWeek = workoutRepository.getCompletedSessionsInRange(prevWeekStart, weekStart).first()
            val prs = progressRepository.getAllPersonalRecords().first()
            val goals = goalRepository.getActiveGoals().first()
            val checkIns = recoveryRepository.getRecentCheckIns(7).first()

            _report.value = CoachEngine.generateWeeklyReport(
                sessionsThisWeek = thisWeek,
                sessionsLastWeek = lastWeek,
                personalRecords = prs,
                activeGoals = goals,
                checkInsThisWeek = checkIns
            )
            _isLoading.value = false
        }
    }
}
