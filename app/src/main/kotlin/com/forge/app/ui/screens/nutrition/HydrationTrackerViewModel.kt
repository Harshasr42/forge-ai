package com.forge.app.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.dao.DailyHydrationSummary
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.db.entities.HydrationLogEntity
import com.forge.app.data.repository.HydrationRepository
import com.forge.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class HydrationUiState(
    val todayKey: String = "",
    val todayTotalMl: Int = 0,
    val targetMl: Int = 3000,
    val todayLogs: List<HydrationLogEntity> = emptyList(),
    val weeklyHistory: List<DailyHydrationSummary> = emptyList()
)

@HiltViewModel
class HydrationTrackerViewModel @Inject constructor(
    private val hydrationRepository: HydrationRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayKey: String = dateFormat.format(Date())

    private val _uiState = MutableStateFlow(HydrationUiState(todayKey = todayKey))
    val uiState: StateFlow<HydrationUiState> = _uiState.asStateFlow()

    init {
        observeHydration()
    }

    private fun observeHydration() {
        viewModelScope.launch {
            combine(
                hydrationRepository.getTotalForDate(todayKey),
                nutritionRepository.getTarget(),
                hydrationRepository.getLogsForDate(todayKey),
                hydrationRepository.getRecentDailySummaries(7)
            ) { totalMl, target, logs, history ->
                HydrationUiState(
                    todayKey = todayKey,
                    todayTotalMl = totalMl,
                    targetMl = target?.waterTargetMl ?: 3000,
                    todayLogs = logs,
                    weeklyHistory = history
                )
            }.collectLatest { state ->
                _uiState.value = state
            }
        }
    }

    fun logWater(amountMl: Int) {
        viewModelScope.launch {
            hydrationRepository.logWater(todayKey, amountMl)
        }
    }

    fun deleteLog(id: Long) {
        viewModelScope.launch {
            hydrationRepository.deleteLog(id)
        }
    }

    fun updateTarget(newTargetMl: Int) {
        viewModelScope.launch {
            val current = nutritionRepository.getTargetOnce() ?: DailyNutritionTargetEntity()
            nutritionRepository.updateTarget(current.copy(waterTargetMl = newTargetMl))
        }
    }
}
