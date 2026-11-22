package com.forge.app.ui.screens.workout

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.data.db.entities.SetRecordEntity
import com.forge.app.ui.components.*
import com.forge.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    onNavigateBack: () -> Unit,
    onWorkoutFinished: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showFormDialog by remember { mutableStateOf(false) }
    var showAlternativesDialog by remember { mutableStateOf(false) }

    if (state.isFinished) {
        WorkoutCompletionCelebration(
            workoutName = state.workoutName,
            durationSeconds = state.durationSeconds,
            totalSets = state.totalCompletedSets,
            totalVolume = state.totalVolume,
            onClose = onWorkoutFinished
        )
        return
    }

    val currentExercise = state.exercises.getOrNull(state.currentExerciseIndex)

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = state.workoutName,
                            style = ForgeTypography.titleMedium,
                            color = TextPrimary
                        )
                        val durationFormatted = remember(state.durationSeconds) {
                            val mins = state.durationSeconds / 60
                            val secs = state.durationSeconds % 60
                            "%02d:%02d".format(mins, secs)
                        }
                        Text(
                            text = "Duration: $durationFormatted",
                            style = ForgeTypography.labelMedium,
                            color = Accent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.finishWorkout() },
                        colors = ButtonDefaults.textButtonColors(contentColor = FireAmber)
                    ) {
                        Text(
                            text = "FINISH",
                            style = ForgeTypography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            if (state.isRestTimerActive) {
                RestTimerOverlay(
                    secondsRemaining = state.restSecondsRemaining,
                    onAddSeconds = { viewModel.addRestSeconds(it) },
                    onSkip = { viewModel.skipRest() }
                )
            }
        }
    ) { padding ->
        if (state.isLoading || currentExercise == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
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
                // Exercise Progress Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXERCISE ${state.currentExerciseIndex + 1} OF ${state.exercises.size}",
                        style = ForgeTypography.labelLarge,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (currentExercise.instructions != null) {
                            ActionChip(
                                icon = Icons.Default.Info,
                                label = "Form",
                                onClick = { showFormDialog = true }
                            )
                        }
                        if (currentExercise.alternatives.isNotEmpty()) {
                            ActionChip(
                                icon = Icons.Default.SwapHoriz,
                                label = "Alt",
                                onClick = { showAlternativesDialog = true }
                            )
                        }
                    }
                }

                // Exercise Title & Tags
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = currentExercise.exercise.name,
                        style = ForgeTypography.displaySmall,
                        color = TextPrimary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TagBadge(text = currentExercise.exercise.muscleTarget, color = Accent)
                        TagBadge(text = currentExercise.exercise.equipment, color = TextSecondary)
                        TagBadge(text = currentExercise.exercise.exerciseType, color = FireAmber)
                    }
                }

                // Sets Table
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Table Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "SET", style = ForgeTypography.labelSmall, color = TextMuted, modifier = Modifier.weight(0.7f), textAlign = TextAlign.Center)
                            Text(text = "PREVIOUS", style = ForgeTypography.labelSmall, color = TextMuted, modifier = Modifier.weight(1.3f), textAlign = TextAlign.Center)
                            Text(text = "KG", style = ForgeTypography.labelSmall, color = TextMuted, modifier = Modifier.weight(1.4f), textAlign = TextAlign.Center)
                            Text(text = "REPS", style = ForgeTypography.labelSmall, color = TextMuted, modifier = Modifier.weight(1.4f), textAlign = TextAlign.Center)
                            Text(text = "DONE", style = ForgeTypography.labelSmall, color = TextMuted, modifier = Modifier.weight(0.9f), textAlign = TextAlign.Center)
                        }

                        Divider(color = SurfaceBorder, thickness = 1.dp)

                        // Set Rows
                        currentExercise.sets.forEach { setRecord ->
                            val prevSet = currentExercise.previousPerformance.getOrNull(setRecord.setNumber - 1)
                            val prevText = if (prevSet != null) "%.0f × %d".format(prevSet.weightKg, prevSet.reps) else "—"

                            SetRow(
                                setRecord = setRecord,
                                previousText = prevText,
                                onUpdateSet = { w, r -> viewModel.updateSet(setRecord, w, r) },
                                onToggleComplete = { viewModel.toggleSetCompletion(setRecord) }
                            )
                        }

                        // Add Set Button
                        TextButton(
                            onClick = { viewModel.addSetToCurrentExercise() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.textButtonColors(contentColor = Accent)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "ADD SET", style = ForgeTypography.labelLarge)
                        }
                    }
                }

                // Exercise Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ForgeButton(
                        text = "PREVIOUS",
                        onClick = { viewModel.previousExercise() },
                        style = ForgeButtonStyle.SECONDARY,
                        enabled = state.currentExerciseIndex > 0,
                        modifier = Modifier.weight(1f)
                    )
                    ForgeButton(
                        text = if (state.currentExerciseIndex == state.exercises.size - 1) "COMPLETE" else "NEXT EXERCISE",
                        onClick = {
                            if (state.currentExerciseIndex == state.exercises.size - 1) {
                                viewModel.finishWorkout()
                            } else {
                                viewModel.nextExercise()
                            }
                        },
                        style = ForgeButtonStyle.PRIMARY,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Form Instruction Dialog
    if (showFormDialog && currentExercise?.instructions != null) {
        val inst = currentExercise.instructions
        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            containerColor = SurfaceElevated,
            title = {
                Text(
                    text = currentExercise.exercise.name,
                    style = ForgeTypography.titleLarge,
                    color = TextPrimary
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (inst.startingPosition.isNotEmpty()) {
                        Text(text = "SETUP", style = ForgeTypography.labelMedium, color = Accent)
                        Text(text = inst.startingPosition, style = ForgeTypography.bodyMedium, color = TextPrimary)
                    }
                    if (inst.executionSteps.isNotEmpty()) {
                        Text(text = "EXECUTION", style = ForgeTypography.labelMedium, color = Accent)
                        Text(text = inst.executionSteps, style = ForgeTypography.bodyMedium, color = TextPrimary)
                    }
                    if (inst.breathingTips.isNotEmpty()) {
                        Text(text = "BREATHING", style = ForgeTypography.labelMedium, color = FireAmber)
                        Text(text = inst.breathingTips, style = ForgeTypography.bodyMedium, color = TextPrimary)
                    }
                    if (inst.commonMistakes.isNotEmpty()) {
                        Text(text = "COMMON MISTAKES", style = ForgeTypography.labelMedium, color = DangerRed)
                        Text(text = inst.commonMistakes, style = ForgeTypography.bodyMedium, color = TextPrimary)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFormDialog = false }) {
                    Text(text = "GOT IT", color = Accent, style = ForgeTypography.labelLarge)
                }
            }
        )
    }

    // Alternatives Dialog
    if (showAlternativesDialog && currentExercise != null) {
        AlertDialog(
            onDismissRequest = { showAlternativesDialog = false },
            containerColor = SurfaceElevated,
            title = {
                Text(
                    text = "Suggested Alternatives",
                    style = ForgeTypography.titleLarge,
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    currentExercise.alternatives.forEach { alt ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Surface)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = alt.name, style = ForgeTypography.titleMedium, color = TextPrimary)
                                Text(text = "${alt.equipment} · ${alt.muscleTarget}", style = ForgeTypography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAlternativesDialog = false }) {
                    Text(text = "CLOSE", color = Accent, style = ForgeTypography.labelLarge)
                }
            }
        )
    }
}

@Composable
private fun SetRow(
    setRecord: SetRecordEntity,
    previousText: String,
    onUpdateSet: (Float, Int) -> Unit,
    onToggleComplete: () -> Unit
) {
    var weightText by remember(setRecord.weightKg) {
        mutableStateOf(if (setRecord.weightKg % 1f == 0f) "%.0f".format(setRecord.weightKg) else "%.1f".format(setRecord.weightKg))
    }
    var repsText by remember(setRecord.reps) {
        mutableStateOf(setRecord.reps.toString())
    }

    val isDone = setRecord.isCompleted

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isDone) SurfaceElevated else Color.Transparent)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Set number
        Text(
            text = "${setRecord.setNumber}",
            style = ForgeTypography.labelLarge,
            color = if (isDone) Accent else TextSecondary,
            modifier = Modifier.weight(0.7f),
            textAlign = TextAlign.Center
        )

        // Previous performance
        Text(
            text = previousText,
            style = ForgeTypography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.weight(1.3f),
            textAlign = TextAlign.Center
        )

        // Weight Input
        Box(
            modifier = Modifier
                .weight(1.4f)
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceElevated)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = weightText,
                onValueChange = { newText ->
                    weightText = newText
                    val newWeight = newText.toFloatOrNull()
                    if (newWeight != null && newWeight >= 0f) {
                        val currentReps = repsText.toIntOrNull() ?: setRecord.reps
                        onUpdateSet(newWeight, currentReps)
                    }
                },
                textStyle = ForgeTypography.labelLarge.copy(
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(Accent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Reps Input
        Box(
            modifier = Modifier
                .weight(1.4f)
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceElevated)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = repsText,
                onValueChange = { newText ->
                    repsText = newText
                    val newReps = newText.toIntOrNull()
                    if (newReps != null && newReps >= 0) {
                        val currentWeight = weightText.toFloatOrNull() ?: setRecord.weightKg
                        onUpdateSet(currentWeight, newReps)
                    }
                },
                textStyle = ForgeTypography.labelLarge.copy(
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(Accent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Checkmark toggle
        Box(
            modifier = Modifier
                .weight(0.9f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (isDone) Accent else SurfaceElevated)
                    .border(1.5.dp, if (isDone) Accent else SurfaceBorder, CircleShape)
                    .clickable(onClick = onToggleComplete),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Background,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TagBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = text, color = color, style = ForgeTypography.labelSmall, fontSize = 10.sp)
    }
}

@Composable
private fun ActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevated)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Accent, modifier = Modifier.size(14.dp))
        Text(text = label, style = ForgeTypography.labelSmall, color = TextPrimary)
    }
}

@Composable
fun WorkoutCompletionCelebration(
    workoutName: String,
    durationSeconds: Long,
    totalSets: Int,
    totalVolume: Float,
    onClose: () -> Unit
) {
    val durationFormatted = remember(durationSeconds) {
        val mins = durationSeconds / 60
        val secs = durationSeconds % 60
        "%02d:%02d".format(mins, secs)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Accent.copy(alpha = 0.2f))
                    .border(2.dp, Accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(44.dp)
                )
            }

            Text(
                text = "SESSION FORGED",
                style = ForgeTypography.labelLarge,
                color = Accent,
                letterSpacing = 2.sp
            )

            Text(
                text = workoutName,
                style = ForgeTypography.displayMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            // Reward Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = durationFormatted, style = ForgeTypography.labelLarge, fontSize = 20.sp, color = TextPrimary)
                        Text(text = "Duration", style = ForgeTypography.bodySmall, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$totalSets", style = ForgeTypography.labelLarge, fontSize = 20.sp, color = TextPrimary)
                        Text(text = "Sets", style = ForgeTypography.bodySmall, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "%.0f kg".format(totalVolume), style = ForgeTypography.labelLarge, fontSize = 20.sp, color = TextPrimary)
                        Text(text = "Volume", style = ForgeTypography.bodySmall, color = TextSecondary)
                    }
                }
            }

            // XP gain banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Accent)
                Text(text = "+100 XP AWARDED", style = ForgeTypography.labelLarge, color = Accent)
            }

            Spacer(modifier = Modifier.height(12.dp))

            ForgeButton(
                text = "BACK TO DASHBOARD",
                onClick = onClose,
                style = ForgeButtonStyle.PRIMARY
            )
        }
    }
}
