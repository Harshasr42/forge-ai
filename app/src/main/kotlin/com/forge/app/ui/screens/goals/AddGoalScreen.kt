package com.forge.app.ui.screens.goals

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forge.app.ui.components.ForgeButton
import com.forge.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddGoalViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("MMM d, yyyy", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
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
                text = "NEW GOAL",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 3.sp
            )
        }

        // Form content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Goal Title
            FormSection(label = "GOAL TITLE") {
                OutlinedTextField(
                    value = viewModel.title,
                    onValueChange = { viewModel.title = it },
                    placeholder = { Text("e.g. Bench Press 100kg", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = forgeTextFieldColors(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Category selector
            FormSection(label = "CATEGORY") {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.categories.forEach { cat ->
                        val selected = viewModel.category == cat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selected) Accent else SurfaceElevated)
                                .border(
                                    1.dp,
                                    if (selected) Accent else SurfaceBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.category = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                color = if (selected) Color.Black else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // Starting value
            FormSection(label = "STARTING VALUE (optional)") {
                OutlinedTextField(
                    value = viewModel.startValue,
                    onValueChange = { viewModel.startValue = it },
                    placeholder = { Text("e.g. 60", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = forgeTextFieldColors(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Target value
            FormSection(label = "TARGET VALUE *") {
                OutlinedTextField(
                    value = viewModel.targetValue,
                    onValueChange = { viewModel.targetValue = it },
                    placeholder = { Text("e.g. 100", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = forgeTextFieldColors(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    isError = viewModel.targetValue.isNotBlank() && viewModel.targetValue.toFloatOrNull() == null
                )
            }

            // Unit selector
            FormSection(label = "UNIT") {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.units.forEach { u ->
                        val selected = viewModel.unit == u
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selected) Accent else SurfaceElevated)
                                .border(
                                    1.dp,
                                    if (selected) Accent else SurfaceBorder,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.unit = u }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = u,
                                color = if (selected) Color.Black else TextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Target date
            FormSection(label = "TARGET DATE") {
                val calendar = remember { Calendar.getInstance() }
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        calendar.set(year, month, day)
                        viewModel.targetDateMillis = calendar.timeInMillis
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                OutlinedButton(
                    onClick = { datePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SurfaceElevated,
                        contentColor = TextPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = viewModel.targetDateMillis?.let { dateFormatter.format(Date(it)) }
                            ?: "No target date",
                        color = if (viewModel.targetDateMillis != null) TextPrimary else TextMuted
                    )
                }
                if (viewModel.targetDateMillis != null) {
                    TextButton(onClick = { viewModel.targetDateMillis = null }) {
                        Text("Clear date", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Save button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            ForgeButton(
                text = "SAVE GOAL",
                onClick = { viewModel.saveGoal(onNavigateBack) },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.isValid()
            )
        }
    }
}

@Composable
private fun FormSection(
    label: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun forgeTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Accent,
    unfocusedBorderColor = SurfaceBorder,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = Accent,
    focusedContainerColor = SurfaceElevated,
    unfocusedContainerColor = SurfaceElevated,
    errorBorderColor = DangerRed,
    errorTextColor = TextPrimary
)
