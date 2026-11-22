package com.forge.app.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.BodyMeasurementEntity
import com.forge.app.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyMeasurementsViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val measurements: StateFlow<List<BodyMeasurementEntity>> = progressRepository.getAllBodyMeasurements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog = _showAddDialog.asStateFlow()

    fun openAddDialog() { _showAddDialog.value = true }
    fun closeAddDialog() { _showAddDialog.value = false }

    fun saveMeasurement(
        weightKg: Float?,
        chestCm: Float?,
        waistCm: Float?,
        armCm: Float?,
        shoulderCm: Float?,
        bodyFatPercent: Float?,
        notes: String
    ) {
        viewModelScope.launch {
            progressRepository.insertBodyMeasurement(
                BodyMeasurementEntity(
                    weightKg = weightKg,
                    chestCm = chestCm,
                    waistCm = waistCm,
                    armCm = armCm,
                    shoulderCm = shoulderCm,
                    bodyFatPercent = bodyFatPercent,
                    notes = notes,
                    recordedAt = System.currentTimeMillis()
                )
            )
            _showAddDialog.value = false
        }
    }
}
