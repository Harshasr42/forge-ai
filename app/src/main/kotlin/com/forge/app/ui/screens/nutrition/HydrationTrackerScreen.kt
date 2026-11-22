package com.forge.app.ui.screens.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.WaterDrop
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
import com.forge.app.data.db.entities.HydrationLogEntity
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HydrationTrackerScreen(
    onNavigateBack: () -> Unit,
    viewModel: HydrationTrackerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showCustomDialog by remember { mutableStateOf(false) }

    val waterColor = Color(0xFF38BDF8)
    val progress = if (state.targetMl > 0) (state.todayTotalMl.toFloat() / state.targetMl).coerceIn(0f, 1f) else 0f

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
                    text = "HYDRATION TRACKER",
                    style = ForgeTypography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Optimal recovery and fluid balance",
                    style = ForgeTypography.labelSmall,
                    color = waterColor
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
            // Main Water Visual Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(waterColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = waterColor,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "${state.todayTotalMl} ml",
                            style = ForgeTypography.displayLarge,
                            color = waterColor,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Daily Target: ${state.targetMl} ml (${(progress * 100).toInt()}%)",
                            style = ForgeTypography.bodyMedium,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = waterColor,
                            trackColor = SurfaceElevated
                        )
                    }
                }
            }

            // Quick Add Section
            item {
                Text(
                    text = "QUICK ADD",
                    style = ForgeTypography.labelMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickAddCard(
                        amount = 250,
                        title = "Glass",
                        subtitle = "250 ml",
                        onClick = { viewModel.logWater(250) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickAddCard(
                        amount = 500,
                        title = "Bottle",
                        subtitle = "500 ml",
                        onClick = { viewModel.logWater(500) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickAddCard(
                        amount = 750,
                        title = "Shaker",
                        subtitle = "750 ml",
                        onClick = { viewModel.logWater(750) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickAddCard(
                        amount = 1000,
                        title = "Jug",
                        subtitle = "1000 ml",
                        onClick = { viewModel.logWater(1000) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                OutlinedButton(
                    onClick = { showCustomDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+ Custom Amount", style = ForgeTypography.labelLarge)
                }
            }

            // Today's Logs
            item {
                Text(
                    text = "TODAY'S LOGS (${state.todayLogs.size})",
                    style = ForgeTypography.labelMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            if (state.todayLogs.isEmpty()) {
                item {
                    Text(
                        text = "No water logs recorded yet today.",
                        style = ForgeTypography.bodyMedium,
                        color = TextMuted
                    )
                }
            } else {
                items(state.todayLogs, key = { it.id }) { log ->
                    WaterLogCard(log = log, onDelete = { viewModel.deleteLog(log.id) })
                }
            }
        }
    }

    if (showCustomDialog) {
        var customAmount by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            containerColor = SurfaceElevated,
            title = { Text("LOG CUSTOM WATER", style = ForgeTypography.titleMedium, color = TextPrimary) },
            text = {
                OutlinedTextField(
                    value = customAmount,
                    onValueChange = { customAmount = it },
                    label = { Text("Amount (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amount = customAmount.toIntOrNull()
                        if (amount != null && amount > 0) {
                            viewModel.logWater(amount)
                            showCustomDialog = false
                        }
                    }
                ) {
                    Text("LOG", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) {
                    Text("CANCEL", color = TextMuted)
                }
            }
        )
    }
}

@Composable
private fun QuickAddCard(
    amount: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = ForgeTypography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, style = ForgeTypography.labelSmall, color = Color(0xFF38BDF8), fontSize = 10.sp)
        }
    }
}

@Composable
private fun WaterLogCard(
    log: HydrationLogEntity,
    onDelete: () -> Unit
) {
    val timeStr = remember(log.loggedAt) {
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(log.loggedAt))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(SurfaceBorder)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${log.amountMl} ml", style = ForgeTypography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "at $timeStr", style = ForgeTypography.labelSmall, color = TextMuted)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(14.dp))
            }
        }
    }
}
