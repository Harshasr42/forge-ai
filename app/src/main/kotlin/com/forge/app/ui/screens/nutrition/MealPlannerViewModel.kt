package com.forge.app.ui.screens.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.dao.GroceryItem
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.db.entities.MealPlanDayEntity
import com.forge.app.data.repository.MealPlanRepository
import com.forge.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MealPlannerUiState(
    val selectedDayOfWeek: Int = 1, // 1 = Monday
    val plannedItems: List<MealPlanDayEntity> = emptyList(),
    val groceryList: List<GroceryItem> = emptyList(),
    val availableFoods: List<FoodItemEntity> = emptyList()
)

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _selectedDayOfWeek = MutableStateFlow(1)
    val selectedDayOfWeek: StateFlow<Int> = _selectedDayOfWeek.asStateFlow()

    private val _uiState = MutableStateFlow(MealPlannerUiState())
    val uiState: StateFlow<MealPlannerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                _selectedDayOfWeek,
                mealPlanRepository.getFullWeeklyPlan(),
                mealPlanRepository.getAggregatedGroceryList(),
                nutritionRepository.getAllFoods()
            ) { day, fullPlan, grocery, foods ->
                val dayItems = fullPlan.filter { it.dayOfWeek == day }
                MealPlannerUiState(
                    selectedDayOfWeek = day,
                    plannedItems = dayItems,
                    groceryList = grocery,
                    availableFoods = foods
                )
            }.collectLatest { state ->
                _uiState.value = state
            }
        }
    }

    fun selectDay(day: Int) {
        _selectedDayOfWeek.value = day
    }

    fun addFoodToPlan(mealType: String, food: FoodItemEntity, quantity: Float) {
        viewModelScope.launch {
            mealPlanRepository.addFoodToPlan(_selectedDayOfWeek.value, mealType, food, quantity)
        }
    }

    fun removePlanItem(id: Long) {
        viewModelScope.launch {
            mealPlanRepository.removeFoodFromPlan(id)
        }
    }

    fun clearDay() {
        viewModelScope.launch {
            mealPlanRepository.clearDayPlan(_selectedDayOfWeek.value)
        }
    }
}
