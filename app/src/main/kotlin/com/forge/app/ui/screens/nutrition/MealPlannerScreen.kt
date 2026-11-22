package com.forge.app.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.forge.app.data.db.entities.FoodItemEntity
import com.forge.app.data.db.entities.MealPlanDayEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*

@Composable
fun MealPlannerScreen(
    onNavigateBack: () -> Unit,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showGroceryDialog by remember { mutableStateOf(false) }
    var selectedMealTypeForAdd by remember { mutableStateOf<String?>(null) }

    val days = listOf(
        1 to "Mon",
        2 to "Tue",
        3 to "Wed",
        4 to "Thu",
        5 to "Fri",
        6 to "Sat",
        7 to "Sun"
    )

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "MEAL PLANNER",
                        style = ForgeTypography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Weekly Schedule & Grocery Engine",
                        style = ForgeTypography.labelSmall,
                        color = Accent
                    )
                }
            }

            IconButton(onClick = { showGroceryDialog = true }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Grocery List", tint = Accent)
            }
        }

        // Weekday selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            days.forEach { (dayNum, name) ->
                val isSelected = state.selectedDayOfWeek == dayNum
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Accent else Surface)
                        .border(1.dp, if (isSelected) Accent else SurfaceBorder, RoundedCornerShape(8.dp))
                        .clickable { viewModel.selectDay(dayNum) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        style = ForgeTypography.labelSmall,
                        color = if (isSelected) Background else TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Daily Meal Plan Sections
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp)
        ) {
            items(mealTypes) { mealType ->
                val items = state.plannedItems.filter { it.mealType == mealType }
                val totalCals = items.sumOf { it.calories.toDouble() }.toInt()
                val totalProtein = items.sumOf { it.protein.toDouble() }.toInt()

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
                                Text(text = mealType, style = ForgeTypography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                                Text(text = "$totalCals kcal • ${totalProtein}g protein", style = ForgeTypography.labelSmall, color = TextMuted)
                            }

                            TextButton(onClick = { selectedMealTypeForAdd = mealType }) {
                                Text("+ ADD", color = Accent, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (items.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = SurfaceBorder, thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(6.dp))

                            items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.foodName, style = ForgeTypography.bodyMedium, color = TextPrimary)
                                        Text(
                                            text = "${item.servingSize.toInt()} ${item.servingUnit} • ${item.protein.toInt()}g P • ${item.calories.toInt()} kcal",
                                            style = ForgeTypography.labelSmall,
                                            color = TextMuted
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.removePlanItem(item.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Grocery List Dialog
    if (showGroceryDialog) {
        AlertDialog(
            onDismissRequest = { showGroceryDialog = false },
            containerColor = SurfaceElevated,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Accent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WEEKLY GROCERY LIST", style = ForgeTypography.titleMedium, color = TextPrimary)
                }
            },
            text = {
                if (state.groceryList.isEmpty()) {
                    Text("No planned meals yet. Add meals to generate an aggregated shopping list.", style = ForgeTypography.bodyMedium, color = TextMuted)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.groceryList) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Surface)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.foodName, style = ForgeTypography.bodyMedium, color = TextPrimary)
                                Text(
                                    text = "${item.totalQuantity.toInt()} ${item.servingUnit}",
                                    style = ForgeTypography.labelMedium,
                                    color = Accent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGroceryDialog = false }) {
                    Text("DONE", color = Accent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Add Food to Plan Picker Dialog
    selectedMealTypeForAdd?.let { mealType ->
        var query by remember { mutableStateOf("") }
        val filtered = remember(query, state.availableFoods) {
            if (query.isBlank()) state.availableFoods.take(15)
            else state.availableFoods.filter { it.name.contains(query, ignoreCase = true) }
        }

        AlertDialog(
            onDismissRequest = { selectedMealTypeForAdd = null },
            containerColor = SurfaceElevated,
            title = { Text("PLAN FOOD FOR $mealType", style = ForgeTypography.titleMedium, color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search foods...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filtered) { food ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Surface)
                                    .clickable {
                                        viewModel.addFoodToPlan(mealType, food, 1.0f)
                                        selectedMealTypeForAdd = null
                                    }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(food.name, style = ForgeTypography.bodyMedium, color = TextPrimary)
                                    Text("${food.servingSize.toInt()} ${food.servingUnit} • ${food.protein.toInt()}g P", style = ForgeTypography.labelSmall, color = TextMuted)
                                }
                                Text("${food.calories.toInt()} kcal", style = ForgeTypography.labelSmall, color = Accent, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedMealTypeForAdd = null }) {
                    Text("CANCEL", color = TextMuted)
                }
            }
        )
    }
}
