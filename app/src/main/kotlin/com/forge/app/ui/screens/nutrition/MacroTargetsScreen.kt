package com.forge.app.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*

@Composable
fun MacroTargetsScreen(
    onNavigateBack: () -> Unit,
    viewModel: MacroTargetsViewModel = hiltViewModel()
) {
    val target by viewModel.target.collectAsStateWithLifecycle()

    var caloriesStr by remember(target.targetCalories) { mutableStateOf(target.targetCalories.toInt().toString()) }
    var proteinStr by remember(target.targetProtein) { mutableStateOf(target.targetProtein.toInt().toString()) }
    var carbsStr by remember(target.targetCarbs) { mutableStateOf(target.targetCarbs.toInt().toString()) }
    var fatStr by remember(target.targetFat) { mutableStateOf(target.targetFat.toInt().toString()) }
    var fiberStr by remember(target.targetFiber) { mutableStateOf(target.targetFiber.toInt().toString()) }
    var waterStr by remember(target.waterTargetMl) { mutableStateOf(target.waterTargetMl.toString()) }

    val p = proteinStr.toFloatOrNull() ?: 0f
    val c = carbsStr.toFloatOrNull() ?: 0f
    val f = fatStr.toFloatOrNull() ?: 0f
    val computedCals = (p * 4f + c * 4f + f * 9f).toInt()

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "NUTRITION TARGETS",
                    style = ForgeTypography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Personalised Calorie & Macro Intelligence",
                    style = ForgeTypography.labelSmall,
                    color = Accent
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Preset Goals
            item {
                Text(
                    text = "SELECT FITNESS GOAL",
                    style = ForgeTypography.labelMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val presets = listOf(
                        "MAINTENANCE" to "Maintain",
                        "BULK" to "Muscle Gain",
                        "CUT" to "Fat Loss"
                    )
                    presets.forEach { (type, label) ->
                        val isSelected = target.goalType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Accent else Surface)
                                .border(1.dp, if (isSelected) Accent else SurfaceBorder, RoundedCornerShape(10.dp))
                                .clickable { viewModel.applyPreset(type) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = ForgeTypography.labelSmall,
                                color = if (isSelected) Background else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Input Fields
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = caloriesStr,
                            onValueChange = { caloriesStr = it },
                            label = { Text("Daily Calories Target (kcal)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = SurfaceBorder
                            )
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = proteinStr,
                                onValueChange = { proteinStr = it },
                                label = { Text("Protein (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Accent,
                                    unfocusedBorderColor = SurfaceBorder
                                )
                            )
                            OutlinedTextField(
                                value = carbsStr,
                                onValueChange = { carbsStr = it },
                                label = { Text("Carbs (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FireAmber,
                                    unfocusedBorderColor = SurfaceBorder
                                )
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = fatStr,
                                onValueChange = { fatStr = it },
                                label = { Text("Fat (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF60A5FA),
                                    unfocusedBorderColor = SurfaceBorder
                                )
                            )
                            OutlinedTextField(
                                value = fiberStr,
                                onValueChange = { fiberStr = it },
                                label = { Text("Fiber (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SuccessGreen,
                                    unfocusedBorderColor = SurfaceBorder
                                )
                            )
                        }

                        OutlinedTextField(
                            value = waterStr,
                            onValueChange = { waterStr = it },
                            label = { Text("Hydration Target (ml)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = SurfaceBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Macro sum check: 4P + 4C + 9F = $computedCals kcal",
                            style = ForgeTypography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            item {
                ForgeButton(
                    text = "SAVE TARGETS",
                    onClick = {
                        val cals = caloriesStr.toFloatOrNull() ?: 2200f
                        val prot = proteinStr.toFloatOrNull() ?: 140f
                        val carb = carbsStr.toFloatOrNull() ?: 240f
                        val fatVal = fatStr.toFloatOrNull() ?: 65f
                        val fib = fiberStr.toFloatOrNull() ?: 30f
                        val wat = waterStr.toIntOrNull() ?: 3000
                        viewModel.updateValues(cals, prot, carb, fatVal, fib, wat)
                        viewModel.saveTarget(onSaved = onNavigateBack)
                    }
                )
            }
        }
    }
}
