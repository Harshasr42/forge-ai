package com.forge.app.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.data.db.entities.PersonalRecordEntity
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PersonalRecordsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: PersonalRecordsViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val filteredRecords by viewModel.filteredRecords.collectAsState()

    val tabs = listOf(
        PersonalRecordsViewModel.RecordTab.ALL to "ALL",
        PersonalRecordsViewModel.RecordTab.WEIGHT to "WEIGHT",
        PersonalRecordsViewModel.RecordTab.ONE_RM to "1RM"
    )

    val sortedRecords = remember(filteredRecords) {
        filteredRecords.sortedByDescending { it.achievedAt }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "PERSONAL RECORDS",
                    style = ForgeTypography.labelLarge,
                    color = Accent,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Your best lifts ever",
                    style = ForgeTypography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = tabs.indexOfFirst { it.first == selectedTab },
            containerColor = Surface,
            contentColor = Accent,
            indicator = { tabPositions ->
                val idx = tabs.indexOfFirst { it.first == selectedTab }
                if (idx >= 0 && idx < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = Accent
                    )
                }
            }
        ) {
            tabs.forEach { (tab, label) ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { viewModel.selectTab(tab) },
                    text = {
                        Text(
                            text = label,
                            style = ForgeTypography.labelMedium,
                            color = if (selectedTab == tab) Accent else TextSecondary
                        )
                    }
                )
            }
        }

        // Records list
        if (sortedRecords.isEmpty()) {
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
                    Text(text = "🏆", fontSize = 48.sp)
                    Text(
                        text = when (selectedTab) {
                            PersonalRecordsViewModel.RecordTab.ALL -> "No personal records yet.\nComplete workouts to earn them!"
                            PersonalRecordsViewModel.RecordTab.WEIGHT -> "No weight records yet.\nLog heavy sets to set records!"
                            PersonalRecordsViewModel.RecordTab.ONE_RM -> "No 1RM estimates yet.\nLog sets with 1–12 reps to calculate."
                        },
                        style = ForgeTypography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sortedRecords, key = { it.id }) { pr ->
                    PersonalRecordCard(pr = pr)
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
private fun PersonalRecordCard(pr: PersonalRecordEntity) {
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
                    .background(Accent.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
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
