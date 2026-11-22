package com.forge.app.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardStats(
    val totalWorkouts: Int = 0,
    val totalVolumeKg: Float = 0f,
    val totalSets: Int = 0,
    val totalReps: Int = 0,
    val recentPrs: List<PersonalRecordEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProgressDashboardViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _selectedRange = MutableStateFlow(30)
    val selectedRange = _selectedRange.asStateFlow()

    private val _stats = MutableStateFlow(DashboardStats())
    val stats = _stats.asStateFlow()

    init { loadStats() }

    fun setRange(days: Int) {
        _selectedRange.value = days
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _stats.value = _stats.value.copy(isLoading = true)
            val end = System.currentTimeMillis()
            val start = end - (_selectedRange.value * 86_400_000L)
            val workouts = progressRepository.getWorkoutCountInRange(start, end)
            val volume = progressRepository.getTotalVolumeInRange(start, end)
            val sets = progressRepository.getTotalSetsInRange(start, end)
            val reps = progressRepository.getTotalRepsInRange(start, end)
            progressRepository.getRecentPersonalRecords(5).collect { prs ->
                _stats.value = DashboardStats(
                    totalWorkouts = workouts,
                    totalVolumeKg = volume ?: 0f,
                    totalSets = sets,
                    totalReps = reps ?: 0,
                    recentPrs = prs,
                    isLoading = false
                )
            }
        }
    }
}
