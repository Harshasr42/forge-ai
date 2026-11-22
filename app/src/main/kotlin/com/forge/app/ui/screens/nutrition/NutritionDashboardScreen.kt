package com.forge.app.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.forge.app.data.db.entities.MealFoodItemEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*

@Composable
fun NutritionDashboardScreen(
    onNavigateBack: () -> Unit = {},
    onAddFood: (mealType: String, dateKey: String) -> Unit,
    onNavigateToHydration: () -> Unit,
    onNavigateToTargets: () -> Unit,
    onNavigateToPlanner: () -> Unit,
    viewModel: NutritionDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val mealTypes = listOf("BREAKFAST", "LUNCH", "DINNER", "SNACKS")

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
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "NUTRITION OS",
                        style = ForgeTypography.labelLarge,
                        color = Accent,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Fuel & Recovery Intelligence",
                        style = ForgeTypography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateToTargets) {
                    Icon(Icons.Default.Tune, contentDescription = "Targets", tint = TextSecondary)
                }
                IconButton(onClick = onNavigateToPlanner) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = "Meal Plan", tint = TextSecondary)
                }
            }
        }

        // Date Navigator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.changeDate(-1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day", tint = TextSecondary)
            }
            Text(
                text = state.displayDate,
                style = ForgeTypography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.setDateToday() }
            )
            IconButton(onClick = { viewModel.changeDate(1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day", tint = TextSecondary)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Calorie & Macro Target Card
            item {
                CalorieMacroCard(
                    consumed = state.totals.totalCalories,
                    target = state.target.targetCalories,
                    protein = state.totals.totalProtein,
                    targetProtein = state.target.targetProtein,
                    carbs = state.totals.totalCarbs,
                    targetCarbs = state.target.targetCarbs,
                    fat = state.totals.totalFat,
                    targetFat = state.target.targetFat,
                    fiber = state.totals.totalFiber,
                    targetFiber = state.target.targetFiber
                )
            }

            // Hydration Card
            item {
                HydrationWidget(
                    currentMl = state.waterIntakeMl,
                    targetMl = state.target.waterTargetMl,
                    onQuickAdd = { amount -> viewModel.quickLogWater(amount) },
                    onOpenHydration = onNavigateToHydration
                )
            }

            // Meals Breakdown
            item {
                Text(
                    text = "MEALS TODAY",
                    style = ForgeTypography.labelMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(mealTypes) { mealType ->
                val mealWithItems = state.meals.find { it.mealLog.mealType == mealType }
                val items = mealWithItems?.foodItems ?: emptyList()
                val mealCalories = items.sumOf { it.calories.toDouble() }.toFloat()
                val mealProtein = items.sumOf { it.protein.toDouble() }.toFloat()

                MealSectionCard(
                    mealType = mealType,
                    calories = mealCalories,
                    protein = mealProtein,
                    items = items,
                    onAddFood = { onAddFood(mealType, state.selectedDateKey) },
                    onDeleteItem = { id -> viewModel.removeFoodItem(id) }
                )
            }
        }
    }
}

@Composable
private fun CalorieMacroCard(
    consumed: Float,
    target: Float,
    protein: Float,
    targetProtein: Float,
    carbs: Float,
    targetCarbs: Float,
    fat: Float,
    targetFat: Float,
    fiber: Float,
    targetFiber: Float
) {
    val remaining = (target - consumed).coerceAtLeast(0f)
    val calProgress = if (target > 0f) (consumed / target).coerceIn(0f, 1f) else 0f

    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${remaining.toInt()}",
                        style = ForgeTypography.displayMedium,
                        color = Accent,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Calories Remaining",
                        style = ForgeTypography.bodySmall,
                        color = TextSecondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${consumed.toInt()} / ${target.toInt()} kcal",
                        style = ForgeTypography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(calProgress * 100).toInt()}% of daily target",
                        style = ForgeTypography.labelSmall,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { calProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Accent,
                trackColor = SurfaceElevated
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Macro Bars Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MacroMiniCard(
                    label = "Protein",
                    current = protein,
                    target = targetProtein,
                    color = Accent,
                    modifier = Modifier.weight(1f)
                )
                MacroMiniCard(
                    label = "Carbs",
                    current = carbs,
                    target = targetCarbs,
                    color = FireAmber,
                    modifier = Modifier.weight(1f)
                )
                MacroMiniCard(
                    label = "Fat",
                    current = fat,
                    target = targetFat,
                    color = Color(0xFF60A5FA),
                    modifier = Modifier.weight(1f)
                )
                MacroMiniCard(
                    label = "Fiber",
                    current = fiber,
                    target = targetFiber,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MacroMiniCard(
    label: String,
    current: Float,
    target: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (target > 0f) (current / target).coerceIn(0f, 1f) else 0f
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevated)
            .padding(8.dp)
    ) {
        Text(text = label, style = ForgeTypography.labelSmall, color = TextMuted, fontSize = 10.sp)
        Text(
            text = "${current.toInt()}g",
            style = ForgeTypography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "/ ${target.toInt()}g",
            style = ForgeTypography.labelSmall,
            color = TextSecondary,
            fontSize = 9.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(1.5.dp)),
            color = color,
            trackColor = Surface
        )
    }
}

@Composable
private fun HydrationWidget(
    currentMl: Int,
    targetMl: Int,
    onQuickAdd: (Int) -> Unit,
    onOpenHydration: () -> Unit
) {
    val progress = if (targetMl > 0) (currentMl.toFloat() / targetMl).coerceIn(0f, 1f) else 0f

    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenHydration() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF38BDF8))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HYDRATION",
                        style = ForgeTypography.labelMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "$currentMl / $targetMl ml",
                    style = ForgeTypography.labelMedium,
                    color = Color(0xFF38BDF8),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF38BDF8),
                trackColor = SurfaceElevated
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick add buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(250, 500, 1000).forEach { amount ->
                    OutlinedButton(
                        onClick = { onQuickAdd(amount) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder))
                    ) {
                        Text("+$amount ml", style = ForgeTypography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun MealSectionCard(
    mealType: String,
    calories: Float,
    protein: Float,
    items: List<MealFoodItemEntity>,
    onAddFood: () -> Unit,
    onDeleteItem: (Long) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = mealType,
                        style = ForgeTypography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${calories.toInt()} kcal • ${protein.toInt()}g protein",
                        style = ForgeTypography.labelSmall,
                        color = TextMuted
                    )
                }

                IconButton(
                    onClick = onAddFood,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Accent.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Food",
                        tint = Accent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.foodName,
                                style = ForgeTypography.bodyMedium,
                                color = TextPrimary
                            )
                            val qtyText = if (item.servingQuantity == 1f) {
                                "${item.servingSize.toInt()} ${item.servingUnit}"
                            } else {
                                "${"%.1f".format(item.servingQuantity)}x (${(item.servingSize * item.servingQuantity).toInt()} ${item.servingUnit})"
                            }
                            Text(
                                text = "$qtyText • ${item.protein.toInt()}g P • ${item.carbs.toInt()}g C • ${item.fat.toInt()}g F",
                                style = ForgeTypography.labelSmall,
                                color = TextMuted
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${item.calories.toInt()} kcal",
                                style = ForgeTypography.bodyMedium,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = { onDeleteItem(item.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Delete",
                                    tint = TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
