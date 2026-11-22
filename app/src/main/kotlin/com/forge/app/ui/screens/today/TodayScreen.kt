package com.forge.app.ui.screens.today

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.ui.components.*
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodayScreen(
    onStartWorkout: (Long) -> Unit,
    onNavigateToProgramme: () -> Unit,
    onNavigateToExercises: () -> Unit,
    onNavigateToNutrition: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    viewModel: TodayViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val dateFormatted = remember {
        SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date())
    }

    Scaffold(
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Header with greeting and fire streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK",
                        style = ForgeTypography.labelMedium,
                        color = Accent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Ready to build?",
                        style = ForgeTypography.displayMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = dateFormatted,
                        style = ForgeTypography.bodyMedium,
                        color = TextSecondary
                    )
                }

                FireStreakWidget(streakDays = state.consistency?.currentStreak ?: 1)
            }

            // 2. XP & Level Card (Duolingo style)
            val xp = state.consistency?.totalXp ?: 100
            val level = state.consistency?.level ?: 1
            val levelTitle = state.consistency?.levelTitle ?: "Apprentice"
            val targetXpForNext = when (level) {
                1 -> 500
                2 -> 1500
                3 -> 3500
                4 -> 7000
                5 -> 13000
                else -> 25000
            }
            val xpProgress = (xp.toFloat() / targetXpForNext).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Accent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$level",
                                    color = Accent,
                                    style = ForgeTypography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "LEVEL $level · $levelTitle",
                                color = TextPrimary,
                                style = ForgeTypography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "$xp / $targetXpForNext XP",
                            color = Accent,
                            style = ForgeTypography.labelLarge,
                            fontSize = 12.sp
                        )
                    }

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { xpProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Accent,
                        trackColor = SurfaceElevated
                    )
                }
            }

            // 3. Today's Training Card (Core Hero Card)
            val workoutName = state.todayProgrammeDay?.workoutName ?: "Chest & Biceps"
            val muscleGroups = state.todayProgrammeDay?.muscleGroups ?: "Chest, Biceps, Forearms"
            val variant = state.todayProgrammeDay?.workoutVariant ?: "A"
            val isRestDay = state.todayProgrammeDay?.isRestDay == true

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SurfaceElevated,
                                Surface
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Accent.copy(alpha = 0.5f),
                                SurfaceBorder
                            )
                        ),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Accent.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isRestDay) "RECOVERY" else "WORKOUT $variant",
                                color = Accent,
                                style = ForgeTypography.labelLarge,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = if (state.activeSession != null) "IN PROGRESS" else "SCHEDULED",
                            color = if (state.activeSession != null) FireAmber else TextSecondary,
                            style = ForgeTypography.labelLarge,
                            fontSize = 11.sp
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = workoutName,
                            style = ForgeTypography.displaySmall,
                            color = TextPrimary
                        )
                        Text(
                            text = muscleGroups,
                            style = ForgeTypography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    // Stat Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TrainingMetricCard(
                            value = "${state.totalExercises}",
                            label = "Exercises",
                            modifier = Modifier.weight(1f)
                        )
                        TrainingMetricCard(
                            value = "${state.totalWorkingSets}",
                            label = "Sets",
                            modifier = Modifier.weight(1f)
                        )
                        TrainingMetricCard(
                            value = "${state.estimatedMinutes}",
                            label = "Mins",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Start / Resume Button
                    ForgeButton(
                        text = if (state.activeSession != null) "RESUME WORKOUT" else "START WORKOUT",
                        onClick = {
                            val dayId = state.activeSession?.programmeDayId ?: state.todayProgrammeDay?.id ?: 1L
                            onStartWorkout(dayId)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (state.activeSession != null) Icons.Default.PlayArrow else Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = Background,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            }

            // 3.5 Nutrition & Fuel OS Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Surface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToNutrition() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Accent.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, tint = Accent)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("NUTRITION OS", style = ForgeTypography.labelLarge, color = Accent, letterSpacing = 1.sp)
                            Text("Log meals, macros & hydration", style = ForgeTypography.bodyMedium, color = TextPrimary)
                        }
                    }
                    Text("OPEN →", style = ForgeTypography.labelLarge, color = Accent, fontWeight = FontWeight.Bold)
                }
            }

            // 4. Daily Habit & Task Checklist
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "DAILY CHECKLIST",
                    style = ForgeTypography.labelLarge,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                val tasks = listOf(
                    "warmup" to "General Dynamic Warm-up (5 min)",
                    "workout" to "Main Workout Execution",
                    "dead_hang" to "Finisher: Dead Hang (45-60s hold)",
                    "hydration" to "Hydration: Drink 3-4L of water",
                    "stretch" to "Mobility & World's Greatest Stretch"
                )

                tasks.forEach { (id, label) ->
                    val isChecked = state.completedTasks.contains(id)
                    ChecklistItem(
                        text = label,
                        isChecked = isChecked,
                        onClick = { viewModel.toggleTask(id) }
                    )
                }
            }

            // 5. Quick Actions Row
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "QUICK ACTIONS",
                    style = ForgeTypography.labelLarge,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionTile(
                        icon = Icons.Default.CalendarMonth,
                        title = "Programme",
                        subtitle = "6-Day Split",
                        onClick = onNavigateToProgramme,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        icon = Icons.Default.MenuBook,
                        title = "Exercises",
                        subtitle = "51 movements",
                        onClick = onNavigateToExercises,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        icon = Icons.Default.History,
                        title = "History",
                        subtitle = "Past logs",
                        onClick = onNavigateToHistory,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TrainingMetricCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = ForgeTypography.labelLarge,
                fontSize = 18.sp,
                color = Accent,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                style = ForgeTypography.bodySmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun ChecklistItem(
    text: String,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isChecked) SurfaceElevated else Surface)
            .border(
                1.dp,
                if (isChecked) Accent.copy(alpha = 0.4f) else SurfaceBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (isChecked) Accent else Color.Transparent)
                .border(
                    2.dp,
                    if (isChecked) Accent else TextMuted,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Background,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Text(
            text = text,
            style = ForgeTypography.bodyMedium,
            color = if (isChecked) TextPrimary else TextSecondary,
            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun QuickActionTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Accent,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = ForgeTypography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = ForgeTypography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
