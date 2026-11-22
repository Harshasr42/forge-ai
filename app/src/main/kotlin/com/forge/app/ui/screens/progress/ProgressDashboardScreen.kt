package com.forge.app.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProgressDashboardScreen(
    onNavigateToPRs: () -> Unit = {},
    onNavigateToExercise: (Long) -> Unit = {},
    onNavigateToMeasurements: () -> Unit = {},
    onNavigateToWorkout: () -> Unit = {},
    viewModel: ProgressDashboardViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState()
    val selectedRange by viewModel.selectedRange.collectAsState()

    val rangeOptions = listOf(
        "7D" to 7,
        "30D" to 30,
        "90D" to 90,
        "6M" to 180,
        "1Y" to 365
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "PROGRESS",
                        style = ForgeTypography.labelLarge,
                        color = Accent,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Your training analytics",
                        style = ForgeTypography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Range chips
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rangeOptions.forEach { (label, days) ->
                        val isSelected = selectedRange == days
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) Accent else Surface)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Accent else SurfaceBorder,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.setRange(days) }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Background else TextSecondary,
                                style = ForgeTypography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Loading indicator
            if (stats.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Accent)
                    }
                }
            } else if (stats.totalWorkouts == 0) {
                // Empty state
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "🏋️",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "Start training to build\nyour analytics",
                            style = ForgeTypography.bodyLarge,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ForgeButton(
                            text = "GO TRAIN",
                            onClick = { onNavigateToWorkout() },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )
                    }
                }
            } else {
                // Stats cards grid
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                label = "WORKOUTS",
                                value = stats.totalWorkouts.toString(),
                                subtitle = "sessions",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "VOLUME",
                                value = "%.0f kg".format(stats.totalVolumeKg),
                                subtitle = "total lifted",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCard(
                                label = "SETS",
                                value = stats.totalSets.toString(),
                                subtitle = "completed",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "REPS",
                                value = stats.totalReps.toString(),
                                subtitle = "total reps",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Personal Records section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = "PRs",
                                tint = Accent,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "RECENT PRs",
                                style = ForgeTypography.labelLarge,
                                color = TextPrimary
                            )
                        }
                        TextButton(onClick = onNavigateToPRs) {
                            Text(
                                text = "View All",
                                color = Accent,
                                style = ForgeTypography.labelMedium
                            )
                        }
                    }
                }

                if (stats.recentPrs.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Surface)
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Complete workouts to set records",
                                style = ForgeTypography.bodyMedium,
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(stats.recentPrs) { pr ->
                        PRCard(
                            pr = pr,
                            onClick = { onNavigateToExercise(pr.exerciseId) }
                        )
                    }
                }

                // Body measurements shortcut
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "BODY MEASUREMENTS",
                            style = ForgeTypography.labelLarge,
                            color = TextPrimary
                        )
                        TextButton(onClick = onNavigateToMeasurements) {
                            Text(
                                text = "Track",
                                color = Accent,
                                style = ForgeTypography.labelMedium
                            )
                        }
                    }
                }
            }

            // Bottom spacer
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            style = ForgeTypography.labelSmall
        )
        Text(
            text = value,
            color = TextPrimary,
            style = ForgeTypography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = TextSecondary,
            style = ForgeTypography.bodySmall
        )
    }
}

@Composable
private fun PRCard(
    pr: PersonalRecordEntity,
    onClick: () -> Unit
) {
    val dateStr = remember(pr.achievedAt) {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(pr.achievedAt))
    }
    val recordValue = when (pr.recordType) {
        "WEIGHT" -> "%.1f kg".format(pr.weightKg)
        "ONE_RM" -> "~%.1f kg 1RM (est.)".format(pr.estimatedOneRm)
        else -> "%.1f kg".format(pr.weightKg)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = pr.exerciseName,
                color = TextPrimary,
                style = ForgeTypography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dateStr,
                color = TextMuted,
                style = ForgeTypography.bodySmall
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = recordValue,
                color = Accent,
                style = ForgeTypography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Accent.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = pr.recordType,
                    color = Accent,
                    style = ForgeTypography.labelSmall,
                    fontSize = 9.sp
                )
            }
        }
    }
}
