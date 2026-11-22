package com.forge.app.ui.screens.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.GoalEntity
import com.forge.app.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    sealed class GoalFilter {
        object All : GoalFilter()
        object Active : GoalFilter()
        object Completed : GoalFilter()
    }

    private val _filter = MutableStateFlow<GoalFilter>(GoalFilter.Active)
    val filter = _filter.asStateFlow()

    val allGoals: StateFlow<List<GoalEntity>> = goalRepository.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val displayedGoals: StateFlow<List<GoalEntity>> = combine(allGoals, _filter) { goals, f ->
        when (f) {
            is GoalFilter.All -> goals
            is GoalFilter.Active -> goals.filter { it.status == "ACTIVE" }
            is GoalFilter.Completed -> goals.filter { it.status == "COMPLETED" }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(f: GoalFilter) {
        _filter.value = f
    }

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch { goalRepository.deleteGoal(goal) }
    }

    fun archiveGoal(goal: GoalEntity) {
        viewModelScope.launch { goalRepository.updateGoalStatus(goal.id, "ABANDONED") }
    }

    fun markComplete(goal: GoalEntity) {
        viewModelScope.launch { goalRepository.updateGoalStatus(goal.id, "COMPLETED") }
    }
}
