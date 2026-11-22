package com.forge.app.ui.screens.history

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.data.db.entities.WorkoutSessionEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.components.ForgeButtonStyle
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onStartWorkout: () -> Unit = {},
    onExploreProgramme: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "TRAINING LOG", style = ForgeTypography.labelLarge, color = Accent)
                        Text(text = "Workout History", style = ForgeTypography.titleLarge, color = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
        } else if (state.sessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Surface)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(22.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Accent.copy(alpha = 0.15f))
                                .border(1.5.dp, Accent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = "NO LOGGED SESSIONS YET",
                            style = ForgeTypography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Every rep and set counts toward your streaks and level progression. Start today's workout to forge your history.",
                            style = ForgeTypography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        ForgeButton(
                            text = "START TODAY'S WORKOUT",
                            onClick = onStartWorkout,
                            style = ForgeButtonStyle.PRIMARY,
                            leadingIcon = {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Background)
                            }
                        )

                        ForgeButton(
                            text = "VIEW PROGRAMME",
                            onClick = onExploreProgramme,
                            style = ForgeButtonStyle.SECONDARY,
                            leadingIcon = {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextPrimary)
                            }
                        )
                    }
                }
            }
        } else {
            val totalVolume = remember(state.sessions) {
                state.sessions.sumOf { it.totalVolume.toDouble() }.toFloat()
            }
            val totalSets = remember(state.sessions) {
                state.sessions.sumOf { it.totalSets }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                // Summary Metric Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(SurfaceElevated, Surface)
                                )
                            )
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "${state.sessions.size}", style = ForgeTypography.titleLarge, color = Accent, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                Text(text = "Completed", style = ForgeTypography.bodySmall, color = TextSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "$totalSets", style = ForgeTypography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                Text(text = "Total Sets", style = ForgeTypography.bodySmall, color = TextSecondary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val volumeFormatted = if (totalVolume >= 1000f) "%.1ft".format(totalVolume / 1000f) else "%.0fkg".format(totalVolume)
                                Text(text = volumeFormatted, style = ForgeTypography.titleLarge, color = FireAmber, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                                Text(text = "Total Volume", style = ForgeTypography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "${state.sessions.size} LOGGED SESSIONS",
                        style = ForgeTypography.labelLarge,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                items(state.sessions, key = { it.id }) { session ->
                    SessionHistoryCard(session = session)
                }
            }
        }
    }
}

@Composable
private fun SessionHistoryCard(session: WorkoutSessionEntity) {
    val dateText = remember(session.startedAt) {
        SimpleDateFormat("EEE, d MMM yyyy · HH:mm", Locale.getDefault()).format(Date(session.startedAt))
    }
    val durationText = remember(session.durationSeconds) {
        val mins = session.durationSeconds / 60
        val secs = session.durationSeconds % 60
        "%02d:%02d".format(mins, secs)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = session.workoutName,
                    style = ForgeTypography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Accent.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = durationText,
                        style = ForgeTypography.labelSmall,
                        color = Accent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = dateText,
                style = ForgeTypography.bodySmall,
                color = TextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "${session.totalSets}", style = ForgeTypography.labelLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text(text = "working sets", style = ForgeTypography.bodySmall, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "%.0f kg".format(session.totalVolume), style = ForgeTypography.labelLarge, color = FireAmber, fontWeight = FontWeight.Bold)
                    Text(text = "volume", style = ForgeTypography.bodySmall, color = TextSecondary)
                }
            }
        }
    }
}
