package com.forge.app.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.dao.MacroTotals
import com.forge.app.data.db.dao.MealWithItems
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.repository.HydrationRepository
import com.forge.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class NutritionDashboardUiState(
    val selectedDateKey: String = "",
    val displayDate: String = "Today",
    val target: DailyNutritionTargetEntity = DailyNutritionTargetEntity(),
    val totals: MacroTotals = MacroTotals(0f, 0f, 0f, 0f, 0f),
    val meals: List<MealWithItems> = emptyList(),
    val waterIntakeMl: Int = 0,
    val isLoading: Boolean = true
)

@HiltViewModel
class NutritionDashboardViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    private val hydrationRepository: HydrationRepository
) : ViewModel() {

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    private val _selectedDateKey = MutableStateFlow(dateFormat.format(Date()))
    val selectedDateKey: StateFlow<String> = _selectedDateKey.asStateFlow()

    private val _uiState = MutableStateFlow(NutritionDashboardUiState(selectedDateKey = _selectedDateKey.value))
    val uiState: StateFlow<NutritionDashboardUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            _selectedDateKey.collectLatest { dateKey ->
                _uiState.update { it.copy(isLoading = true, selectedDateKey = dateKey, displayDate = formatDisplayDate(dateKey)) }

                combine(
                    nutritionRepository.getTarget(),
                    nutritionRepository.getDailyTotals(dateKey),
                    nutritionRepository.getMealsForDate(dateKey),
                    hydrationRepository.getTotalForDate(dateKey)
                ) { target, totals, meals, waterMl ->
                    val defaultTarget = target ?: DailyNutritionTargetEntity()
                    NutritionDashboardUiState(
                        selectedDateKey = dateKey,
                        displayDate = formatDisplayDate(dateKey),
                        target = defaultTarget,
                        totals = totals,
                        meals = meals,
                        waterIntakeMl = waterMl,
                        isLoading = false
                    )
                }.collectLatest { state ->
                    _uiState.value = state
                }
            }
        }
    }

    fun changeDate(offsetDays: Int) {
        calendar.add(Calendar.DAY_OF_YEAR, offsetDays)
        _selectedDateKey.value = dateFormat.format(calendar.time)
    }

    fun setDateToday() {
        calendar.time = Date()
        _selectedDateKey.value = dateFormat.format(calendar.time)
    }

    fun quickLogWater(amountMl: Int) {
        viewModelScope.launch {
            hydrationRepository.logWater(_selectedDateKey.value, amountMl)
        }
    }

    fun removeFoodItem(mealFoodItemId: Long) {
        viewModelScope.launch {
            nutritionRepository.removeFoodFromMeal(mealFoodItemId)
        }
    }

    private fun formatDisplayDate(dateKey: String): String {
        val today = dateFormat.format(Date())
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = dateFormat.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, 2)
        val tomorrow = dateFormat.format(cal.time)

        return when (dateKey) {
            today -> "Today"
            yesterday -> "Yesterday"
            tomorrow -> "Tomorrow"
            else -> {
                try {
                    val date = dateFormat.parse(dateKey)
                    if (date != null) displayFormat.format(date) else dateKey
                } catch (e: Exception) {
                    dateKey
                }
            }
        }
    }
}
