package com.forge.app.ui.screens.programme

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.data.db.entities.ProgrammeDayEntity
import com.forge.app.ui.theme.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammeScreen(
    onSelectDayWorkout: (Long) -> Unit,
    viewModel: ProgrammeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val todayDayOfWeek = remember {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_WEEK)
        if (d == Calendar.SUNDAY) 7 else d - 1
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "TRAINING PROGRAMME",
                            style = ForgeTypography.labelLarge,
                            color = Accent
                        )
                        Text(
                            text = "6-Day Split & Active Recovery",
                            style = ForgeTypography.titleLarge,
                            color = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        if (state.isLoading) {
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "7-DAY WEEKLY SCHEDULE",
                    style = ForgeTypography.labelLarge,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )

                state.days.forEach { day ->
                    ProgrammeDayCard(
                        day = day,
                        isToday = day.dayOfWeek == todayDayOfWeek,
                        onClick = { onSelectDayWorkout(day.id) }
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun ProgrammeDayCard(
    day: ProgrammeDayEntity,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayName = when (day.dayOfWeek) {
        1 -> "MON"
        2 -> "TUE"
        3 -> "WED"
        4 -> "THU"
        5 -> "FRI"
        6 -> "SAT"
        else -> "SUN"
    }

    val isRecovery = day.isRestDay || day.workoutVariant == "Recovery"

    val badgeColor = when {
        isRecovery -> SuccessGreen
        day.workoutVariant == "A" -> Accent
        else -> FireAmber
    }

    val borderColor = when {
        isToday -> Accent
        else -> SurfaceBorder
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isToday) SurfaceElevated else Surface)
            .border(if (isToday) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Day Badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = dayName,
                            style = ForgeTypography.labelLarge,
                            color = badgeColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = day.workoutName,
                            style = ForgeTypography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        // Variant or Recovery Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeColor.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isRecovery) "RECOVERY" else "SPLIT ${day.workoutVariant}",
                                style = ForgeTypography.labelSmall,
                                color = badgeColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Today Badge
                        if (isToday) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Accent)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "TODAY",
                                    style = ForgeTypography.labelSmall,
                                    color = Background,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Text(
                        text = if (day.notes.isNotEmpty()) "${day.muscleGroups} · ${day.notes}" else day.muscleGroups,
                        style = ForgeTypography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2
                    )

                    Text(
                        text = "${day.estimatedMinutes} min estimated session",
                        style = ForgeTypography.labelSmall,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = if (isToday) Accent else TextSecondary,
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 6.dp)
            )
        }
    }
}
