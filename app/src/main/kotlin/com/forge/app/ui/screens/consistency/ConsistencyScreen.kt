package com.forge.app.ui.screens.consistency

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.data.db.entities.AchievementEntity
import com.forge.app.ui.components.FireStreakWidget
import com.forge.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsistencyScreen(
    viewModel: ConsistencyViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "CONSISTENCY & REWARDS", style = ForgeTypography.labelLarge, color = Accent)
                        Text(text = "Streaks & Badges", style = ForgeTypography.titleLarge, color = TextPrimary)
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
        } else {
            val cons = state.consistency
            val currentStreak = cons?.currentStreak ?: 1
            val longestStreak = cons?.longestStreak ?: 1
            val totalXp = cons?.totalXp ?: 100
            val level = cons?.level ?: 1
            val levelTitle = cons?.levelTitle ?: "Apprentice"

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Fire Streak Hero Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2B1305),
                                    Surface
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.verticalGradient(listOf(FireAmber, SurfaceBorder)),
                            RoundedCornerShape(22.dp)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FireStreakWidget(streakDays = currentStreak, isPulsing = true)

                        val streakText = if (currentStreak == 1) "1 DAY" else "$currentStreak DAYS"
                        Text(
                            text = streakText,
                            style = ForgeTypography.displayLarge,
                            color = FireAmber,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        val longestText = if (longestStreak == 1) "Longest Streak: 1 Day" else "Longest Streak: $longestStreak Days"
                        Text(
                            text = longestText,
                            style = ForgeTypography.labelLarge,
                            color = TextSecondary
                        )

                        Text(
                            text = "\"Consistency is becoming your strength.\"",
                            style = ForgeTypography.bodyMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // XP & Level Progression Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "LEVEL $level · $levelTitle", style = ForgeTypography.titleMedium, color = Accent, fontWeight = FontWeight.Bold)
                            Text(text = "$totalXp XP TOTAL", style = ForgeTypography.labelLarge, color = TextPrimary)
                        }

                        val targetXp = 500 * level
                        val progress = (totalXp.toFloat() / targetXp).coerceIn(0f, 1f)

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Accent,
                            trackColor = SurfaceElevated
                        )

                        Text(
                            text = "Next rank at $targetXp XP (+100 XP per completed workout)",
                            style = ForgeTypography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Weekly Dynamic Activity Calendar
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "ACTIVITY CALENDAR", style = ForgeTypography.labelLarge, color = TextSecondary, letterSpacing = 1.sp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Surface)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                state.weeklyActivity.forEach { dayActivity ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = dayActivity.dayLabel,
                                            style = ForgeTypography.labelSmall,
                                            color = if (dayActivity.isToday) Accent else TextMuted,
                                            fontWeight = if (dayActivity.isToday) FontWeight.Bold else FontWeight.Normal
                                        )

                                        val bgColor = when {
                                            dayActivity.isCompleted -> Accent
                                            dayActivity.isToday -> SurfaceElevated
                                            dayActivity.isFuture -> Surface
                                            else -> SurfaceElevated.copy(alpha = 0.5f)
                                        }

                                        val borderColor = when {
                                            dayActivity.isCompleted -> Accent
                                            dayActivity.isToday -> Accent
                                            else -> SurfaceBorder
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(bgColor)
                                                .border(
                                                    width = if (dayActivity.isToday) 1.5.dp else 1.dp,
                                                    color = borderColor,
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (dayActivity.isCompleted) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Completed",
                                                    tint = Background,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = "${dayActivity.dayNumber}",
                                                    style = ForgeTypography.labelSmall,
                                                    color = if (dayActivity.isToday) Accent else if (dayActivity.isFuture) TextMuted else TextSecondary,
                                                    fontWeight = if (dayActivity.isToday) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Badges & Achievements
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "ACHIEVEMENTS", style = ForgeTypography.labelLarge, color = TextSecondary, letterSpacing = 1.sp)

                    state.achievements.forEach { ach ->
                        AchievementItem(achievement = ach)
                    }
                }

                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
private fun AchievementItem(achievement: AchievementEntity) {
    val isUnlocked = achievement.isUnlocked

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isUnlocked) SurfaceElevated else Surface)
            .border(
                1.dp,
                if (isUnlocked) Accent.copy(alpha = 0.5f) else SurfaceBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) Accent.copy(alpha = 0.2f) else Surface)
                    .border(1.dp, if (isUnlocked) Accent else TextMuted, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) Accent else TextMuted,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = achievement.title,
                    style = ForgeTypography.titleMedium,
                    color = if (isUnlocked) TextPrimary else TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = achievement.description,
                    style = ForgeTypography.bodySmall,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isUnlocked) Accent.copy(alpha = 0.15f) else Surface)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "+${achievement.xpReward} XP",
                    style = ForgeTypography.labelSmall,
                    color = if (isUnlocked) Accent else TextMuted,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
