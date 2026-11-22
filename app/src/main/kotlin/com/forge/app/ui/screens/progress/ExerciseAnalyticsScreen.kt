package com.forge.app.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.ui.components.ForgeBarChart
import com.forge.app.ui.components.ForgeLineChart
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExerciseAnalyticsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ExerciseAnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Column {
                Text(
                    text = state.exerciseName.uppercase(),
                    style = ForgeTypography.labelLarge,
                    color = Accent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Exercise Analytics",
                    style = ForgeTypography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
            return@Column
        }

        if (state.historyPoints.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "📊", fontSize = 48.sp)
                    Text(
                        text = "No workout history yet.\nComplete a workout containing this exercise.",
                        style = ForgeTypography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Best stats cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Best Weight
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Surface)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "BEST WEIGHT",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            style = ForgeTypography.labelSmall
                        )
                        Text(
                            text = "%.1f kg".format(state.bestWeight),
                            color = TextPrimary,
                            style = ForgeTypography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "heaviest set",
                            color = TextSecondary,
                            style = ForgeTypography.bodySmall
                        )
                    }

                    // Est. 1RM
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Surface)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "EST. 1RM",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            style = ForgeTypography.labelSmall
                        )
                        Text(
                            text = if (state.bestOneRm > 0f) "~%.1f kg".format(state.bestOneRm) else "N/A",
                            color = Accent,
                            style = ForgeTypography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Epley formula estimate",
                            color = TextSecondary,
                            style = ForgeTypography.bodySmall
                        )
                    }
                }
            }

            // 1RM trend chart
            if (state.oneRmPoints.size >= 2) {
                item {
                    ChartCard(title = "ESTIMATED 1RM TREND") {
                        ForgeLineChart(
                            points = state.oneRmPoints,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            lineColor = Accent,
                            labelColor = TextMuted
                        )
                    }
                }
            }

            // Weight per session bar chart
            item {
                val barData = remember(state.historyPoints) {
                    val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
                    state.historyPoints.map { pt ->
                        sdf.format(Date(pt.startedAt)) to pt.maxWeight
                    }
                }
                ChartCard(title = "MAX WEIGHT PER SESSION") {
                    ForgeBarChart(
                        bars = barData,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        barColor = Accent,
                        dimColor = SurfaceBorder
                    )
                }
            }

            // Recent sessions list
            item {
                Text(
                    text = "RECENT SESSIONS",
                    style = ForgeTypography.labelLarge,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }

            val recentSessions = state.historyPoints.takeLast(10).reversed()
            items(recentSessions) { pt ->
                val dateStr = remember(pt.startedAt) {
                    SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(pt.startedAt))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Surface)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateStr,
                        color = TextSecondary,
                        style = ForgeTypography.bodyMedium
                    )
                    Text(
                        text = "%.1f kg × %d reps".format(pt.maxWeight, pt.maxReps),
                        color = TextPrimary,
                        style = ForgeTypography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun ChartCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = ForgeTypography.labelMedium,
            color = TextMuted,
            letterSpacing = 1.sp
        )
        content()
    }
}
