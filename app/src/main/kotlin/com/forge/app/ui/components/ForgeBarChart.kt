package com.forge.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgeBarChart(
    bars: List<Pair<String, Float>>,   // label → value
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFFA3E635),
    dimColor: Color = Color(0xFF2A342A),
    maxBars: Int = 10,
    valueFormatter: (Float) -> String = { "%.0f".format(it) }
) {
    val displayBars = remember(bars, maxBars) {
        if (bars.size > maxBars) bars.takeLast(maxBars) else bars
    }

    if (displayBars.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "No data",
                color = Color(0xFF556655),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        return
    }

    val textMeasurer = rememberTextMeasurer()
    val maxValue = displayBars.maxOf { it.second }.coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        val canvasW = size.width
        val canvasH = size.height

        val paddingTop = 24f    // space for value labels
        val paddingBottom = 28f // space for bar labels
        val barAreaH = canvasH - paddingTop - paddingBottom

        val n = displayBars.size
        val totalGap = canvasW * 0.25f
        val gapWidth = totalGap / (n + 1)
        val barWidth = (canvasW - totalGap) / n

        displayBars.forEachIndexed { i, (label, value) ->
            val barLeft = gapWidth * (i + 1) + barWidth * i
            val barHeight = (value / maxValue) * barAreaH
            val barTop = paddingTop + barAreaH - barHeight

            val isMax = value == maxValue
            val fillColor = if (isMax) barColor else dimColor.copy(alpha = 1f).let {
                // Tint dimColor with barColor slightly
                Color(
                    red = it.red * 0.7f + barColor.red * 0.3f,
                    green = it.green * 0.7f + barColor.green * 0.3f,
                    blue = it.blue * 0.7f + barColor.blue * 0.3f,
                    alpha = 1f
                )
            }

            // Rounded top bar
            drawRoundRect(
                color = fillColor,
                topLeft = Offset(barLeft, barTop),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // Value label above bar
            val valueText = valueFormatter(value)
            val valueMeasured = textMeasurer.measure(
                text = valueText,
                style = TextStyle(fontSize = 9.sp, color = if (isMax) barColor else Color(0xFF8A9E8A), fontFamily = FontFamily.Monospace)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = valueText,
                topLeft = Offset(
                    barLeft + barWidth / 2f - valueMeasured.size.width / 2f,
                    barTop - valueMeasured.size.height - 2f
                ),
                style = TextStyle(fontSize = 9.sp, color = if (isMax) barColor else Color(0xFF8A9E8A), fontFamily = FontFamily.Monospace)
            )

            // Label below bar (truncated to 6 chars)
            val truncatedLabel = if (label.length > 6) label.take(6) else label
            val labelMeasured = textMeasurer.measure(
                text = truncatedLabel,
                style = TextStyle(fontSize = 9.sp, color = Color(0xFF556655), fontFamily = FontFamily.Monospace)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = truncatedLabel,
                topLeft = Offset(
                    barLeft + barWidth / 2f - labelMeasured.size.width / 2f,
                    paddingTop + barAreaH + 6f
                ),
                style = TextStyle(fontSize = 9.sp, color = Color(0xFF556655), fontFamily = FontFamily.Monospace)
            )
        }
    }
}
