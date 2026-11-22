package com.forge.app.ui.screens.recovery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forge.app.data.db.entities.RecoveryCheckInEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecoveryScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()
    val todayCheckIn by viewModel.todayCheckIn.collectAsStateWithLifecycle()
    val recentCheckIns by viewModel.recentCheckIns.collectAsStateWithLifecycle()

    val todayFormatted = remember {
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
    }

    // Track if user has interacted with form (for save button enable logic)
    var hasInteracted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Column {
                Text(
                    text = "RECOVERY",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary,
                    letterSpacing = 3.sp
                )
                Text(
                    text = todayFormatted,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Already checked in banner
            if (isSaved && todayCheckIn != null) {
                TodaySummaryCard(
                    checkIn = todayCheckIn!!,
                    onUpdateRequested = {
                        viewModel.allowUpdate()
                        hasInteracted = true
                    }
                )
            }

            // Readiness label card
            ReadinessCard(label = viewModel.readinessLabel)

            if (!isSaved) {
                // SLEEP
                RecoverySection(title = "SLEEP") {
                    // Hours slider
                    Text(
                        text = "${"%.1f".format(viewModel.sleepHours)} hours",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Slider(
                        value = viewModel.sleepHours,
                        onValueChange = {
                            viewModel.sleepHours = it
                            hasInteracted = true
                        },
                        valueRange = 4f..12f,
                        steps = 15, // 0.5 steps: (12-4)/0.5 - 1 = 15
                        colors = SliderDefaults.colors(
                            thumbColor = Accent,
                            activeTrackColor = Accent,
                            inactiveTrackColor = SurfaceElevated
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Sleep quality stars
                    Text(
                        text = "Sleep Quality",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = if (star <= viewModel.sleepQuality)
                                    Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Star $star",
                                tint = if (star <= viewModel.sleepQuality) Accent else TextMuted,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        viewModel.sleepQuality = star
                                        hasInteracted = true
                                    }
                            )
                        }
                    }
                }

                // ENERGY
                RecoverySection(title = "ENERGY") {
                    val labels = mapOf(1 to "Exhausted", 2 to "Low", 3 to "Normal", 4 to "High", 5 to "Energised")
                    FiveDotSelector(
                        value = viewModel.energyLevel,
                        labels = labels,
                        dotColor = Accent,
                        onValueChange = { viewModel.energyLevel = it; hasInteracted = true }
                    )
                }

                // SORENESS
                RecoverySection(title = "MUSCLE SORENESS") {
                    val labels = mapOf(1 to "None", 2 to "Mild", 3 to "Moderate", 4 to "High", 5 to "Very sore")
                    FiveDotSelector(
                        value = viewModel.muscleSoreness,
                        labels = labels,
                        dotColor = FireAmber,
                        onValueChange = { viewModel.muscleSoreness = it; hasInteracted = true }
                    )
                }

                // MOTIVATION
                RecoverySection(title = "MOTIVATION") {
                    val labels = mapOf(1 to "None", 2 to "Low", 3 to "Normal", 4 to "High", 5 to "Pumped")
                    FiveDotSelector(
                        value = viewModel.motivation,
                        labels = labels,
                        dotColor = SuccessGreen,
                        onValueChange = { viewModel.motivation = it; hasInteracted = true }
                    )
                }

                // FATIGUE
                RecoverySection(title = "FATIGUE") {
                    val labels = mapOf(1 to "Fresh", 2 to "Light", 3 to "Moderate", 4 to "Heavy", 5 to "Drained")
                    FiveDotSelector(
                        value = viewModel.fatigue,
                        labels = labels,
                        dotColor = DangerRed,
                        onValueChange = { viewModel.fatigue = it; hasInteracted = true }
                    )
                }

                // SORE MUSCLE GROUPS
                RecoverySection(title = "SORE MUSCLE GROUPS") {
                    val muscleGroups = listOf("Chest", "Back", "Shoulders", "Arms", "Core", "Legs", "Glutes")
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        muscleGroups.forEach { muscle ->
                            val selected = muscle in viewModel.selectedMuscleGroups
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (selected) Accent else Surface)
                                    .border(
                                        1.dp,
                                        if (selected) Accent else SurfaceBorder,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        viewModel.selectedMuscleGroups = if (selected)
                                            viewModel.selectedMuscleGroups - muscle
                                        else
                                            viewModel.selectedMuscleGroups + muscle
                                        hasInteracted = true
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = muscle,
                                    color = if (selected) Color.Black else TextSecondary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // NOTES
                RecoverySection(title = "NOTES (optional)") {
                    OutlinedTextField(
                        value = viewModel.notes,
                        onValueChange = {
                            if (it.length <= 200) {
                                viewModel.notes = it
                                hasInteracted = true
                            }
                        },
                        placeholder = { Text("How are you feeling today?", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = Accent,
                            focusedContainerColor = SurfaceElevated,
                            unfocusedContainerColor = SurfaceElevated
                        ),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 4
                    )
                    Text(
                        text = "${viewModel.notes.length}/200",
                        color = TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }

                // Save button
                ForgeButton(
                    text = "LOG RECOVERY",
                    onClick = { viewModel.saveCheckIn() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = hasInteracted
                )
            }

            // Historical trends
            if (recentCheckIns.isNotEmpty()) {
                Text(
                    text = "LAST 14 DAYS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recentCheckIns.take(14)) { checkIn ->
                        MiniCheckInCard(checkIn = checkIn)
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun ReadinessCard(label: String) {
    val color = when {
        label.contains("easy", ignoreCase = true) -> FireAmber
        label.contains("Ready", ignoreCase = true) -> SuccessGreen
        label.contains("lighter", ignoreCase = true) -> FireAmber
        else -> Accent
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                color = color,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Based on your inputs only — not a medical assessment.",
                color = TextMuted,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TodaySummaryCard(
    checkIn: RecoveryCheckInEntity,
    onUpdateRequested: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✓ Checked in today",
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                TextButton(onClick = onUpdateRequested) {
                    Text("Update", color = Accent, fontSize = 13.sp)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryItem("Sleep", "${"%.1f".format(checkIn.sleepHours)}h")
                SummaryItem("Energy", "${checkIn.energyLevel}/5")
                SummaryItem("Fatigue", "${checkIn.fatigue}/5")
                SummaryItem("Soreness", "${checkIn.muscleSoreness}/5")
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(text = label, color = TextMuted, fontSize = 11.sp)
    }
}

@Composable
private fun RecoverySection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        content()
    }
}

@Composable
private fun FiveDotSelector(
    value: Int,
    labels: Map<Int, String>,
    dotColor: Color,
    onValueChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { level ->
                val selected = level <= value
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (selected) dotColor else SurfaceElevated)
                        .border(
                            1.dp,
                            if (selected) dotColor else SurfaceBorder,
                            CircleShape
                        )
                        .clickable { onValueChange(level) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.toString(),
                        color = if (selected) Color.Black else TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            Text(
                text = labels[value] ?: "",
                color = dotColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun MiniCheckInCard(checkIn: RecoveryCheckInEntity) {
    val dotColor = when {
        checkIn.energyLevel >= 4 -> SuccessGreen
        checkIn.energyLevel >= 3 -> Accent
        checkIn.energyLevel == 2 -> FireAmber
        else -> DangerRed
    }
    val dateLabel = remember(checkIn.dateKey) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(checkIn.dateKey)
            SimpleDateFormat("MMM d", Locale.getDefault()).format(date ?: Date())
        } catch (e: Exception) {
            checkIn.dateKey
        }
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Text(
            text = dateLabel,
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${checkIn.energyLevel}/5",
            color = dotColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
