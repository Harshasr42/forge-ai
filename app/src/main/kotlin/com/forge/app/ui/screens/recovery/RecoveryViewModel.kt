package com.forge.app.ui.screens.recovery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.RecoveryCheckInEntity
import com.forge.app.data.repository.RecoveryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val recoveryRepository: RecoveryRepository
) : ViewModel() {

    // Form state for today's check-in
    var sleepHours by mutableStateOf(7f)
    var sleepQuality by mutableStateOf(3)
    var energyLevel by mutableStateOf(3)
    var muscleSoreness by mutableStateOf(2)
    var motivation by mutableStateOf(3)
    var fatigue by mutableStateOf(2)
    var selectedMuscleGroups by mutableStateOf(setOf<String>())
    var notes by mutableStateOf("")

    val recentCheckIns: StateFlow<List<RecoveryCheckInEntity>> = recoveryRepository.getRecentCheckIns(14)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _todayCheckIn = MutableStateFlow<RecoveryCheckInEntity?>(null)
    val todayCheckIn = _todayCheckIn.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    // Simple readiness label (NOT a medical score) based on input
    val readinessLabel: String
        get() = when {
            fatigue >= 4 || muscleSoreness >= 4 -> "Take it easy today"
            energyLevel >= 4 && fatigue <= 2 -> "Ready to train"
            sleepHours < 6f -> "Consider lighter training"
            else -> "Normal training day"
        }

    init {
        viewModelScope.launch {
            val existing = recoveryRepository.getTodayCheckIn()
            if (existing != null) {
                _todayCheckIn.value = existing
                _isSaved.value = true
                // pre-fill form
                sleepHours = existing.sleepHours
                sleepQuality = existing.sleepQuality
                energyLevel = existing.energyLevel
                muscleSoreness = existing.muscleSoreness
                motivation = existing.motivation
                fatigue = existing.fatigue
                selectedMuscleGroups = existing.soreMuscleGroups.split(",").filter { it.isNotBlank() }.toSet()
                notes = existing.notes
            }
        }
    }

    fun saveCheckIn() {
        viewModelScope.launch {
            val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date())
            recoveryRepository.insertCheckIn(
                RecoveryCheckInEntity(
                    dateKey = today,
                    sleepHours = sleepHours,
                    sleepQuality = sleepQuality,
                    energyLevel = energyLevel,
                    muscleSoreness = muscleSoreness,
                    motivation = motivation,
                    fatigue = fatigue,
                    soreMuscleGroups = selectedMuscleGroups.joinToString(","),
                    notes = notes,
                    recordedAt = System.currentTimeMillis()
                )
            )
            _isSaved.value = true
        }
    }

    fun allowUpdate() {
        _isSaved.value = false
    }
}
