package com.forge.app.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forge.app.data.db.entities.BodyMeasurementEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.components.ForgeLineChart
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyMeasurementsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: BodyMeasurementsViewModel = hiltViewModel()
) {
    val measurements by viewModel.measurements.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()

    val weightPoints = remember(measurements) {
        measurements.filter { it.weightKg != null && it.weightKg > 0f }
            .sortedBy { it.recordedAt }
            .map { it.recordedAt to it.weightKg!! }
    }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = Accent,
                contentColor = Background
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Measurement")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BODY MEASUREMENTS",
                    style = ForgeTypography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (weightPoints.size >= 2) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "WEIGHT TREND (KG)",
                                    style = ForgeTypography.labelSmall,
                                    color = Accent,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                ForgeLineChart(
                                    points = weightPoints,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp),
                                    lineColor = Accent
                                )
                            }
                        }
                    }
                }

                if (measurements.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No measurements logged yet",
                                    style = ForgeTypography.titleMedium,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Log weight and body circumference to track your physique progress over time.",
                                    style = ForgeTypography.bodyMedium,
                                    color = TextMuted,
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                ForgeButton(
                                    text = "+ LOG MEASUREMENT",
                                    onClick = { viewModel.openAddDialog() },
                                    modifier = Modifier.width(220.dp)
                                )
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "HISTORY (${measurements.size})",
                            style = ForgeTypography.labelMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(measurements, key = { it.id }) { item ->
                        MeasurementCard(item = item)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMeasurementDialog(
            onDismiss = { viewModel.closeAddDialog() },
            onSave = { weight, chest, waist, arm, shoulder, bodyFat, notes ->
                viewModel.saveMeasurement(weight, chest, waist, arm, shoulder, bodyFat, notes)
            }
        )
    }
}

@Composable
private fun MeasurementCard(item: BodyMeasurementEntity) {
    val dateStr = remember(item.recordedAt) {
        SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault()).format(Date(item.recordedAt))
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
                Text(text = dateStr, style = ForgeTypography.bodySmall, color = TextMuted)
                if (item.weightKg != null && item.weightKg > 0f) {
                    Text(
                        text = "${item.weightKg} kg",
                        style = ForgeTypography.titleMedium,
                        color = Accent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val metrics = mutableListOf<Pair<String, String>>()
            if (item.chestCm != null) metrics.add("Chest" to "${item.chestCm} cm")
            if (item.waistCm != null) metrics.add("Waist" to "${item.waistCm} cm")
            if (item.armCm != null) metrics.add("Arm" to "${item.armCm} cm")
            if (item.shoulderCm != null) metrics.add("Shoulder" to "${item.shoulderCm} cm")
            if (item.bodyFatPercent != null) metrics.add("Body Fat" to "${item.bodyFatPercent}%")

            if (metrics.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    metrics.forEach { (label, value) ->
                        Column {
                            Text(text = label, style = ForgeTypography.labelSmall, color = TextMuted)
                            Text(text = value, style = ForgeTypography.bodyMedium, color = TextPrimary)
                        }
                    }
                }
            }

            if (item.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = item.notes, style = ForgeTypography.bodySmall, color = TextSecondary)
            }
        }
    }
}

@Composable
private fun AddMeasurementDialog(
    onDismiss: () -> Unit,
    onSave: (Float?, Float?, Float?, Float?, Float?, Float?, String) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var chestText by remember { mutableStateOf("") }
    var waistText by remember { mutableStateOf("") }
    var armText by remember { mutableStateOf("") }
    var shoulderText by remember { mutableStateOf("") }
    var bodyFatText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceElevated,
        title = {
            Text(text = "LOG MEASUREMENT", style = ForgeTypography.titleMedium, color = TextPrimary)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Body Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = SurfaceBorder
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = chestText,
                        onValueChange = { chestText = it },
                        label = { Text("Chest (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = SurfaceBorder
                        )
                    )
                    OutlinedTextField(
                        value = waistText,
                        onValueChange = { waistText = it },
                        label = { Text("Waist (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = SurfaceBorder
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = armText,
                        onValueChange = { armText = it },
                        label = { Text("Arm (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = SurfaceBorder
                        )
                    )
                    OutlinedTextField(
                        value = shoulderText,
                        onValueChange = { shoulderText = it },
                        label = { Text("Shoulder (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Accent,
                            unfocusedBorderColor = SurfaceBorder
                        )
                    )
                }
                OutlinedTextField(
                    value = bodyFatText,
                    onValueChange = { bodyFatText = it },
                    label = { Text("Body Fat % (optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = SurfaceBorder
                    )
                )
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = SurfaceBorder
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        weightText.toFloatOrNull(),
                        chestText.toFloatOrNull(),
                        waistText.toFloatOrNull(),
                        armText.toFloatOrNull(),
                        shoulderText.toFloatOrNull(),
                        bodyFatText.toFloatOrNull(),
                        notesText
                    )
                }
            ) {
                Text("SAVE", color = Accent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = TextMuted)
            }
        }
    )
}
