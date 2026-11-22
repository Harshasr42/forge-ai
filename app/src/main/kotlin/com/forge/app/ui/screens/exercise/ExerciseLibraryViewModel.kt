package com.forge.app.ui.screens.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseLibraryUiState(
    val exercises: List<ExerciseEntity> = emptyList(),
    val selectedCategory: String = "ALL",
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class ExerciseLibraryViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("ALL")
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ExerciseLibraryUiState> = combine(
        exerciseRepository.getAllExercises(),
        _selectedCategory,
        _searchQuery
    ) { allExercises, category, query ->
        val filtered = allExercises.filter { exercise ->
            // Category or Favourites filter
            val matchesCategory = when (category) {
                "ALL" -> true
                "FAVOURITES" -> exercise.isFavourite
                else -> exercise.category.equals(category, ignoreCase = true)
            }

            // Search query filter matching name, target muscle, or equipment
            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                exercise.name.contains(query, ignoreCase = true) ||
                exercise.muscleTarget.contains(query, ignoreCase = true) ||
                exercise.equipment.contains(query, ignoreCase = true)
            }

            matchesCategory && matchesQuery
        }

        ExerciseLibraryUiState(
            exercises = filtered,
            selectedCategory = category,
            searchQuery = query,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExerciseLibraryUiState(isLoading = true)
    )

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavourite(exerciseId: Long, currentFav: Boolean) {
        viewModelScope.launch {
            exerciseRepository.toggleFavourite(exerciseId, !currentFav)
        }
    }
}
