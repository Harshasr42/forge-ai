package com.forge.app.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forge.app.domain.WeeklyReportData
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeeklyReportScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: WeeklyReportViewModel = hiltViewModel()
) {
    val report by viewModel.report.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val dateRangeStr = remember {
        val now = System.currentTimeMillis()
        val start = now - 7L * 86_400_000L
        val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
        "${fmt.format(Date(start))} – ${fmt.format(Date(now))}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "WEEKLY REPORT",
                    style = ForgeTypography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateRangeStr,
                    style = ForgeTypography.labelSmall,
                    color = Accent
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent)
            }
        } else {
            val data = report
            if (data == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No report data available", style = ForgeTypography.bodyMedium, color = TextMuted)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    // Focus for next week
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Accent)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "COACH FOCUS FOR NEXT WEEK",
                                    style = ForgeTypography.labelSmall,
                                    color = Accent,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = data.focusSuggestion,
                                    style = ForgeTypography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    // Training Volume & Sessions Overview
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "TRAINING ACTIVITY",
                                    style = ForgeTypography.labelSmall,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "${data.sessionsCompleted}/${data.sessionsTargeted}",
                                            style = ForgeTypography.displaySmall,
                                            color = Accent,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "Sessions completed",
                                            style = ForgeTypography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${data.totalVolumeKg.toInt()} kg",
                                            style = ForgeTypography.displaySmall,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        val changeText = if (data.volumeChangePercent >= 0f) {
                                            "+${"%.1f".format(data.volumeChangePercent)}% vs last wk"
                                        } else {
                                            "${"%.1f".format(data.volumeChangePercent)}% vs last wk"
                                        }
                                        Text(
                                            text = changeText,
                                            style = ForgeTypography.labelSmall,
                                            color = if (data.volumeChangePercent >= 0f) SuccessGreen else DangerRed
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Personal Records
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = FireAmber)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "NEW PERSONAL RECORDS (${data.newPrs.size})",
                                        style = ForgeTypography.labelMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                if (data.newPrs.isEmpty()) {
                                    Text(
                                        text = "No new personal records set in the past 7 days.",
                                        style = ForgeTypography.bodyMedium,
                                        color = TextMuted
                                    )
                                } else {
                                    data.newPrs.forEach { pr ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = pr.exerciseName,
                                                style = ForgeTypography.bodyMedium,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = "${pr.weightKg} kg × ${pr.reps}",
                                                style = ForgeTypography.bodyMedium,
                                                color = Accent,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Recovery Summary
                    if (data.avgSleepHours != null || data.avgEnergyLevel != null) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Surface),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Hotel, contentDescription = null, tint = Accent)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "RECOVERY AVERAGES",
                                            style = ForgeTypography.labelMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        if (data.avgSleepHours != null) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "${"%.1f".format(data.avgSleepHours)}h",
                                                    style = ForgeTypography.titleLarge,
                                                    color = TextPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text("Avg Sleep", style = ForgeTypography.labelSmall, color = TextMuted)
                                            }
                                        }
                                        if (data.avgEnergyLevel != null) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "${"%.1f".format(data.avgEnergyLevel)}/5",
                                                    style = ForgeTypography.titleLarge,
                                                    color = Accent,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text("Avg Energy", style = ForgeTypography.labelSmall, color = TextMuted)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Active Goals
                    if (data.activeGoals.isNotEmpty()) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Surface),
                                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Accent)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "GOAL PROGRESS",
                                            style = ForgeTypography.labelMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    data.activeGoals.forEach { goal ->
                                        val progress = if (goal.targetValue > goal.startValue) {
                                            ((goal.currentValue - goal.startValue) / (goal.targetValue - goal.startValue)).coerceIn(0f, 1f)
                                        } else 0f
                                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(goal.title, style = ForgeTypography.bodySmall, color = TextPrimary)
                                                Text(
                                                    "${(progress * 100).toInt()}%",
                                                    style = ForgeTypography.labelSmall,
                                                    color = Accent,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            LinearProgressIndicator(
                                                progress = { progress },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(4.dp)
                                                    .clip(RoundedCornerShape(2.dp)),
                                                color = Accent,
                                                trackColor = SurfaceElevated
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
