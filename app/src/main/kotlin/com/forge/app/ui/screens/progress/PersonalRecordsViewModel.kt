package com.forge.app.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PersonalRecordsViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    enum class RecordTab { ALL, WEIGHT, ONE_RM }

    private val _selectedTab = MutableStateFlow(RecordTab.ALL)
    val selectedTab = _selectedTab.asStateFlow()

    val allRecords: StateFlow<List<PersonalRecordEntity>> = progressRepository.getAllPersonalRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredRecords: StateFlow<List<PersonalRecordEntity>> = combine(allRecords, _selectedTab) { records, tab ->
        when (tab) {
            RecordTab.ALL -> records
            RecordTab.WEIGHT -> records.filter { it.recordType == "WEIGHT" }
            RecordTab.ONE_RM -> records.filter { it.recordType == "ONE_RM" }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: RecordTab) {
        _selectedTab.value = tab
    }
}
