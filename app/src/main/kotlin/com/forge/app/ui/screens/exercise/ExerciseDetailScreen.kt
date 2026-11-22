package com.forge.app.ui.screens.exercise

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forge.app.data.db.entities.ExerciseAlternativeEntity
import com.forge.app.data.db.entities.ExerciseEntity
import com.forge.app.data.db.entities.ExerciseInstructionEntity
import com.forge.app.data.repository.ExerciseRepository
import com.forge.app.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseDetailUiState(
    val exercise: ExerciseEntity? = null,
    val instructions: ExerciseInstructionEntity? = null,
    val alternatives: List<ExerciseEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseId: Long = savedStateHandle.get<Long>("exerciseId")
        ?: savedStateHandle.get<String>("exerciseId")?.toLongOrNull()
        ?: 1L

    private val _uiState = MutableStateFlow(ExerciseDetailUiState())
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            val exercise = exerciseRepository.getExerciseById(exerciseId)
            val inst = exerciseRepository.getInstructionsForExercise(exerciseId)
            val alts = exerciseRepository.getAlternativesForExercise(exerciseId)

            _uiState.value = ExerciseDetailUiState(
                exercise = exercise,
                instructions = inst,
                alternatives = alts,
                isLoading = false
            )
        }
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            val ex = _uiState.value.exercise ?: return@launch
            exerciseRepository.toggleFavourite(ex.id, !ex.isFavourite)
            _uiState.value = _uiState.value.copy(exercise = ex.copy(isFavourite = !ex.isFavourite))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExerciseDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val exercise = state.exercise
    val inst = state.instructions

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = exercise?.name ?: "Exercise Details", style = ForgeTypography.titleMedium, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                actions = {
                    if (exercise != null) {
                        IconButton(onClick = { viewModel.toggleFavourite() }) {
                            Icon(
                                imageVector = if (exercise.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Fav",
                                tint = if (exercise.isFavourite) DangerRed else TextMuted
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        if (state.isLoading || exercise == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Exercise Tags Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = exercise.name, style = ForgeTypography.displaySmall, color = TextPrimary)
                        Text(text = "Target: ${exercise.muscleTarget}", style = ForgeTypography.bodyMedium, color = Accent)
                        if (exercise.muscleSecondary.isNotEmpty()) {
                            Text(text = "Secondary: ${exercise.muscleSecondary}", style = ForgeTypography.bodySmall, color = TextSecondary)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            DetailChip(text = exercise.category, color = FireAmber)
                            DetailChip(text = exercise.equipment, color = TextSecondary)
                            DetailChip(text = exercise.exerciseType, color = BlueSky)
                        }
                    }
                }

                // Instructions Section
                if (inst != null) {
                    DetailSection(title = "STARTING POSITION", content = inst.startingPosition)
                    DetailSection(title = "EXECUTION & FORM", content = inst.executionSteps)
                    if (inst.breathingTips.isNotEmpty()) {
                        DetailSection(title = "BREATHING CUES", content = inst.breathingTips)
                    }
                    if (inst.commonMistakes.isNotEmpty()) {
                        DetailSection(title = "COMMON MISTAKES", content = inst.commonMistakes)
                    }
                    if (inst.safetyTips.isNotEmpty()) {
                        DetailSection(title = "SAFETY CONSIDERATIONS", content = inst.safetyTips)
                    }
                }

                // Alternatives Section
                if (state.alternatives.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "SUGGESTED ALTERNATIVES", style = ForgeTypography.labelLarge, color = Accent)
                        state.alternatives.forEach { alt ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Surface)
                                    .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(text = alt.name, style = ForgeTypography.titleMedium, color = TextPrimary)
                                    Text(text = "${alt.equipment} · ${alt.muscleTarget}", style = ForgeTypography.bodySmall, color = TextSecondary)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun DetailSection(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = title, style = ForgeTypography.labelLarge, color = Accent, fontSize = 11.sp)
            Text(text = content, style = ForgeTypography.bodyMedium, color = TextPrimary)
        }
    }
}

@Composable
private fun DetailChip(text: String, color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = text, color = color, style = ForgeTypography.labelSmall, fontSize = 10.sp)
    }
}

