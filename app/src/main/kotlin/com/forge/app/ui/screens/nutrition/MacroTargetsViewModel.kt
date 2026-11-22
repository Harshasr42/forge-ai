package com.forge.app.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.DailyNutritionTargetEntity
import com.forge.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MacroTargetsViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _target = MutableStateFlow(DailyNutritionTargetEntity())
    val target: StateFlow<DailyNutritionTargetEntity> = _target.asStateFlow()

    init {
        viewModelScope.launch {
            nutritionRepository.getTarget().collectLatest { loaded ->
                if (loaded != null) {
                    _target.value = loaded
                }
            }
        }
    }

    fun applyPreset(goalType: String) {
        val updated = when (goalType) {
            "BULK" -> _target.value.copy(
                targetCalories = 2600f,
                targetProtein = 160f,
                targetCarbs = 320f,
                targetFat = 75f,
                targetFiber = 35f,
                goalType = "BULK"
            )
            "CUT" -> _target.value.copy(
                targetCalories = 1850f,
                targetProtein = 150f,
                targetCarbs = 180f,
                targetFat = 55f,
                targetFiber = 30f,
                goalType = "CUT"
            )
            "MAINTENANCE" -> _target.value.copy(
                targetCalories = 2200f,
                targetProtein = 140f,
                targetCarbs = 240f,
                targetFat = 65f,
                targetFiber = 30f,
                goalType = "MAINTENANCE"
            )
            else -> _target.value.copy(goalType = "CUSTOM")
        }
        _target.value = updated
    }

    fun updateValues(
        calories: Float,
        protein: Float,
        carbs: Float,
        fat: Float,
        fiber: Float,
        waterMl: Int
    ) {
        _target.value = _target.value.copy(
            targetCalories = calories,
            targetProtein = protein,
            targetCarbs = carbs,
            targetFat = fat,
            targetFiber = fiber,
            waterTargetMl = waterMl,
            goalType = "CUSTOM"
        )
    }

    fun saveTarget(onSaved: () -> Unit) {
        viewModelScope.launch {
            nutritionRepository.updateTarget(_target.value)
            onSaved()
        }
    }
}
