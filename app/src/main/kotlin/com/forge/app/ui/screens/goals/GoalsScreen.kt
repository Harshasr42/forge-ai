package com.forge.app.ui.screens.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
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
import com.forge.app.data.db.entities.GoalEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onAddGoal: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val goals by viewModel.displayedGoals.collectAsStateWithLifecycle()

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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Text(
                text = "GOALS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 3.sp,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onAddGoal) {
                Text(
                    text = "+ Add Goal",
                    color = Accent,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        // Filter tabs
        GoalFilterRow(
            currentFilter = filter,
            onFilterSelected = { viewModel.setFilter(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Content
        if (goals.isEmpty()) {
            GoalEmptyState(filter = filter, onAddGoal = onAddGoal)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(goals, key = { it.id }) { goal ->
                    GoalCard(
                        goal = goal,
                        onMarkComplete = { viewModel.markComplete(goal) },
                        onArchive = { viewModel.archiveGoal(goal) },
                        onDelete = { viewModel.deleteGoal(goal) }
                    )
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
private fun GoalFilterRow(
    currentFilter: GoalsViewModel.GoalFilter,
    onFilterSelected: (GoalsViewModel.GoalFilter) -> Unit
) {
    val filters = listOf(
        "ACTIVE" to GoalsViewModel.GoalFilter.Active,
        "COMPLETED" to GoalsViewModel.GoalFilter.Completed,
        "ALL" to GoalsViewModel.GoalFilter.All
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { (label, f) ->
            val selected = currentFilter::class == f::class
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selected) Accent else SurfaceElevated)
                    .border(
                        width = 1.dp,
                        color = if (selected) Accent else SurfaceBorder,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onFilterSelected(f) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (selected) Color.Black else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun GoalCard(
    goal: GoalEntity,
    onMarkComplete: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    val range = goal.targetValue - goal.startValue
    val progress = if (range > 0f) {
        ((goal.currentValue - goal.startValue) / range).coerceIn(0f, 1f)
    } else 0f
    val progressPct = (progress * 100).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                if (goal.status == "COMPLETED") {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = SuccessGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(SurfaceElevated)
                    ) {
                        if (goal.status == "ACTIVE") {
                            DropdownMenuItem(
                                text = { Text("Mark Complete", color = SuccessGreen) },
                                onClick = { showMenu = false; onMarkComplete() }
                            )
                            DropdownMenuItem(
                                text = { Text("Archive", color = FireAmber) },
                                onClick = { showMenu = false; onArchive() }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Delete", color = DangerRed) },
                            onClick = { showMenu = false; onDelete() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category badge
            CategoryBadge(category = goal.category)

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (goal.status == "COMPLETED") SuccessGreen else Accent,
                trackColor = SurfaceElevated
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Progress text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${formatValue(goal.currentValue)} / ${formatValue(goal.targetValue)} ${goal.unit}",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = "$progressPct%",
                    color = if (goal.status == "COMPLETED") SuccessGreen else Accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Target date
            goal.targetDate?.let { millis ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Target: ${dateFormatter.format(Date(millis))}",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            // Completed badge
            if (goal.status == "COMPLETED") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ACHIEVED",
                        color = SuccessGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBadge(category: String) {
    val (bg, fg) = when (category) {
        "STRENGTH" -> FireAmber.copy(alpha = 0.15f) to FireAmber
        "WEIGHT" -> Accent.copy(alpha = 0.15f) to Accent
        "CONSISTENCY" -> Color(0xFF60A5FA).copy(alpha = 0.15f) to Color(0xFF60A5FA)
        "MEASUREMENT" -> Color(0xFFC084FC).copy(alpha = 0.15f) to Color(0xFFC084FC)
        else -> SurfaceElevated to TextSecondary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = category,
            color = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun GoalEmptyState(
    filter: GoalsViewModel.GoalFilter,
    onAddGoal: () -> Unit
) {
    val (message, showButton) = when (filter) {
        is GoalsViewModel.GoalFilter.Active ->
            "No active goals.\nTap + to set your first goal." to true
        is GoalsViewModel.GoalFilter.Completed ->
            "No completed goals yet.\nKeep training!" to false
        is GoalsViewModel.GoalFilter.All ->
            "No goals yet.\nSet your first fitness target." to false
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = TextMuted,
            fontSize = 16.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 24.sp
        )
        if (showButton) {
            Spacer(modifier = Modifier.height(24.dp))
            ForgeButton(
                text = "+ Set First Goal",
                onClick = onAddGoal,
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }
    }
}

private fun formatValue(v: Float): String =
    if (v == v.toLong().toFloat()) v.toLong().toString() else "%.1f".format(v)
