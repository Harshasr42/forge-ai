package com.forge.app.ui.screens.programme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.ProgrammeDayEntity
import com.forge.app.data.db.entities.ProgrammeEntity
import com.forge.app.data.repository.ProgrammeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProgrammeUiState(
    val programme: ProgrammeEntity? = null,
    val days: List<ProgrammeDayEntity> = emptyList(),
    val selectedDayId: Long? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProgrammeViewModel @Inject constructor(
    private val programmeRepository: ProgrammeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgrammeUiState())
    val uiState: StateFlow<ProgrammeUiState> = _uiState.asStateFlow()

    init {
        loadProgramme()
    }

    private fun loadProgramme() {
        viewModelScope.launch {
            val prog = programmeRepository.getActiveProgrammeOnce() ?: return@launch
            val daysList = programmeRepository.getProgrammeDaysOnce(prog.id)
            _uiState.value = ProgrammeUiState(
                programme = prog,
                days = daysList,
                selectedDayId = daysList.firstOrNull()?.id,
                isLoading = false
            )
        }
    }

    fun selectDay(dayId: Long) {
        _uiState.value = _uiState.value.copy(selectedDayId = dayId)
    }
}
