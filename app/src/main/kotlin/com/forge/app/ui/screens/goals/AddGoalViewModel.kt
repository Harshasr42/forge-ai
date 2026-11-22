package com.forge.app.ui.screens.goals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.GoalEntity
import com.forge.app.data.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    var title by mutableStateOf("")
    var category by mutableStateOf("STRENGTH")
    var targetValue by mutableStateOf("")
    var startValue by mutableStateOf("")
    var unit by mutableStateOf("kg")
    var targetDateMillis by mutableStateOf<Long?>(null)

    val categories = listOf("STRENGTH", "WEIGHT", "CONSISTENCY", "MEASUREMENT", "CUSTOM")
    val units = listOf("kg", "lbs", "cm", "reps", "sessions", "%")

    fun isValid(): Boolean = title.isNotBlank() && targetValue.toFloatOrNull() != null

    fun saveGoal(onDone: () -> Unit) {
        if (!isValid()) return
        viewModelScope.launch {
            goalRepository.insertGoal(
                GoalEntity(
                    title = title,
                    category = category,
                    startValue = startValue.toFloatOrNull() ?: 0f,
                    targetValue = targetValue.toFloat(),
                    currentValue = startValue.toFloatOrNull() ?: 0f,
                    unit = unit,
                    targetDate = targetDateMillis,
                    status = "ACTIVE"
                )
            )
            onDone()
        }
    }
}
