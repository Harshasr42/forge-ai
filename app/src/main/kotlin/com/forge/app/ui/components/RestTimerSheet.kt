package com.forge.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forge.app.ui.theme.*

@Composable
fun RestTimerOverlay(
    secondsRemaining: Int,
    onAddSeconds: (Int) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = "%02d:%02d".format(minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceElevated,
                        Background
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(FireAmber.copy(alpha = 0.5f), SurfaceBorder)
                ),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Drag handle / top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = FireAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "REST PERIOD",
                        color = FireAmber,
                        style = ForgeTypography.labelLarge,
                        letterSpacing = 1.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Surface)
                        .clickable(onClick = onSkip),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Skip Rest",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Timer display
            Text(
                text = timeFormatted,
                style = ForgeTypography.displayLarge,
                color = TextPrimary,
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold
            )

            // Quick add & skip buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickTimeButton(
                    text = "+15s",
                    onClick = { onAddSeconds(15) },
                    modifier = Modifier.weight(1f)
                )
                QuickTimeButton(
                    text = "+30s",
                    onClick = { onAddSeconds(30) },
                    modifier = Modifier.weight(1f)
                )
                QuickTimeButton(
                    text = "SKIP",
                    onClick = onSkip,
                    modifier = Modifier.weight(1f),
                    isHighlight = true
                )
            }
        }
    }
}

@Composable
private fun QuickTimeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isHighlight) SurfaceBorder else Surface)
            .border(
                1.dp,
                if (isHighlight) FireAmber.copy(alpha = 0.5f) else SurfaceBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isHighlight) FireAmber else TextPrimary,
            style = ForgeTypography.labelLarge,
            fontSize = 12.sp
        )
    }
}
