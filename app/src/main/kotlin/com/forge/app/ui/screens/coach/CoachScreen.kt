package com.forge.app.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forge.app.domain.CoachInsight
import com.forge.app.domain.InsightCategory
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*

@Composable
fun CoachScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: CoachViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FORGE COACH",
                    style = ForgeTypography.labelLarge,
                    color = Accent,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Personal Fitness Intelligence",
                    style = ForgeTypography.bodySmall,
                    color = TextMuted
                )
            }
            IconButton(onClick = { viewModel.refresh() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = TextSecondary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Quick action cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate("weekly_report") }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Assessment, contentDescription = null, tint = Accent)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Weekly Report", style = ForgeTypography.titleSmall, color = TextPrimary)
                                Text("Performance recap", style = ForgeTypography.labelSmall, color = TextMuted)
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate("recovery") }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Hotel, contentDescription = null, tint = FireAmber)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Recovery Log", style = ForgeTypography.titleSmall, color = TextPrimary)
                                Text("Check-in readiness", style = ForgeTypography.labelSmall, color = TextMuted)
                            }
                        }
                    }
                }
            }

            if (state.isLoading) {
                items(3) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {}
                }
            } else if (state.insights.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "All caught up!",
                                style = ForgeTypography.titleMedium,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Keep training consistently and logging workouts. FORGE Coach will surface evidence-based observations as your data builds.",
                                style = ForgeTypography.bodyMedium,
                                color = TextMuted,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            } else {
                items(state.insights, key = { it.id }) { insight ->
                    InsightCard(insight = insight, onAction = { route -> onNavigate(route) })
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "FORGE Coach generates insights deterministically from your self-reported logs and completed workout records. It does not provide medical diagnosis or replace professional coaching.",
                        style = ForgeTypography.labelSmall,
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightCard(
    insight: CoachInsight,
    onAction: (String) -> Unit
) {
    val categoryColor = when (insight.category) {
        InsightCategory.PROGRESS -> Accent
        InsightCategory.POSITIVE -> SuccessGreen
        InsightCategory.WARNING -> DangerRed
        InsightCategory.RECOVERY -> FireAmber
        InsightCategory.OVERLOAD -> FireAmber
        InsightCategory.CONSISTENCY -> Accent
        InsightCategory.GOALS -> Accent
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(categoryColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = insight.category.name,
                        style = ForgeTypography.labelSmall,
                        color = categoryColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = insight.title,
                style = ForgeTypography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = insight.body,
                style = ForgeTypography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            if (insight.actionLabel != null && insight.actionRoute != null) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { onAction(insight.actionRoute) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "${insight.actionLabel} →",
                        style = ForgeTypography.labelLarge,
                        color = Accent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
