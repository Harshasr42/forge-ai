package com.forge.app.ui.screens.progress

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.dao.ExerciseHistoryPoint
import com.forge.app.data.repository.ExerciseRepository
import com.forge.app.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseAnalyticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val progressRepository: ProgressRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    val exerciseId: Long = savedStateHandle.get<Long>("exerciseId")
        ?: savedStateHandle.get<String>("exerciseId")?.toLongOrNull()
        ?: 1L

    data class AnalyticsState(
        val exerciseName: String = "",
        val historyPoints: List<ExerciseHistoryPoint> = emptyList(),
        val oneRmPoints: List<Pair<Long, Float>> = emptyList(), // timestamp → estimated 1RM
        val bestOneRm: Float = 0f,
        val bestWeight: Float = 0f,
        val isLoading: Boolean = true
    )

    private val _state = MutableStateFlow(AnalyticsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseById(exerciseId)
            val history = progressRepository.getExerciseHistory(exerciseId)

            // Epley formula: 1RM = weight * (1 + reps / 30)
            val oneRmPoints = history
                .filter { it.maxReps in 1..12 && it.maxWeight > 0f }
                .map { it.startedAt to it.maxWeight * (1 + it.maxReps / 30f) }

            _state.value = AnalyticsState(
                exerciseName = exercise?.name ?: "Exercise",
                historyPoints = history,
                oneRmPoints = oneRmPoints,
                bestOneRm = oneRmPoints.maxOfOrNull { it.second } ?: 0f,
                bestWeight = history.maxOfOrNull { it.maxWeight } ?: 0f,
                isLoading = false
            )
        }
    }
}
