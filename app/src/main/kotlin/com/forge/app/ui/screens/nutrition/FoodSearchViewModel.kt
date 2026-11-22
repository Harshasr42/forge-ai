package com.forge.app.ui.screens.nutrition

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val mealType: String = savedStateHandle.get<String>("mealType") ?: "BREAKFAST"
    val dateKey: String = savedStateHandle.get<String>("dateKey") ?: ""

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("ALL")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf("ALL", "INDIAN", "PROTEIN", "DAIRY", "GRAINS", "FRUITS", "VEGETABLES", "SNACKS")

    val foods: StateFlow<List<FoodItemEntity>> = combine(
        nutritionRepository.getAllFoods(),
        _searchQuery,
        _selectedCategory
    ) { allFoods, query, category ->
        var filtered = allFoods
        if (category != "ALL") {
            filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
            }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected food for quantity modal
    private val _selectedFood = MutableStateFlow<FoodItemEntity?>(null)
    val selectedFood: StateFlow<FoodItemEntity?> = _selectedFood.asStateFlow()

    private val _servingMultiplier = MutableStateFlow(1.0f)
    val servingMultiplier: StateFlow<Float> = _servingMultiplier.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun openFoodQuantity(food: FoodItemEntity) {
        _selectedFood.value = food
        _servingMultiplier.value = 1.0f
    }

    fun closeFoodQuantity() {
        _selectedFood.value = null
    }

    fun setMultiplier(multiplier: Float) {
        _servingMultiplier.value = multiplier.coerceAtLeast(0.1f)
    }

    fun logSelectedFood(onSuccess: () -> Unit) {
        val food = _selectedFood.value ?: return
        val multiplier = _servingMultiplier.value
        viewModelScope.launch {
            nutritionRepository.addFoodToMeal(dateKey, mealType, food, multiplier)
            _selectedFood.value = null
            onSuccess()
        }
    }

    fun createCustomFood(
        name: String,
        category: String,
        servingSize: Float,
        servingUnit: String,
        calories: Float,
        protein: Float,
        carbs: Float,
        fat: Float,
        fiber: Float
    ) {
        viewModelScope.launch {
            val customFood = FoodItemEntity(
                name = name,
                category = category,
                servingSize = servingSize,
                servingUnit = servingUnit,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                fiber = fiber,
                isCustom = true,
                isVerified = false
            )
            nutritionRepository.insertCustomFood(customFood)
        }
    }
}
