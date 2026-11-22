package com.forge.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ForgeLineChart(
    points: List<Pair<Long, Float>>,   // timestamp → value
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFFA3E635),
    labelColor: Color = Color(0xFF556655),
    showDots: Boolean = true,
    valueFormatter: (Float) -> String = { "%.1f".format(it) },
    dateFormatter: (Long) -> String = { ts ->
        SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(ts))
    }
) {
    if (points.size < 2) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "Not enough data",
                color = labelColor,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        return
    }

    val textMeasurer = rememberTextMeasurer()

    val sortedPoints = remember(points) { points.sortedBy { it.first } }

    Canvas(modifier = modifier) {
        val canvasW = size.width
        val canvasH = size.height

        val paddingLeft = 52f
        val paddingRight = 16f
        val paddingTop = 16f
        val paddingBottom = 36f

        val chartW = canvasW - paddingLeft - paddingRight
        val chartH = canvasH - paddingTop - paddingBottom

        val rawMin = sortedPoints.minOf { it.second }
        val rawMax = sortedPoints.maxOf { it.second }

        val isFlat = rawMin == rawMax
        val range = if (isFlat) 1f else rawMax - rawMin
        val padding10 = range * 0.10f

        val minY = if (isFlat) rawMin - 1f else rawMin - padding10
        val maxY = if (isFlat) rawMax + 1f else rawMax + padding10
        val yRange = maxY - minY

        val minX = sortedPoints.first().first.toFloat()
        val maxX = sortedPoints.last().first.toFloat()
        val xRange = if (maxX == minX) 1f else maxX - minX

        fun xOf(ts: Long): Float = paddingLeft + ((ts - minX) / xRange) * chartW
        fun yOf(v: Float): Float = paddingTop + chartH - ((v - minY) / yRange) * chartH

        // Build path
        val linePath = Path()
        sortedPoints.forEachIndexed { i, (ts, v) ->
            val x = xOf(ts)
            val y = yOf(v)
            if (i == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
        }

        // Filled gradient area
        val fillPath = Path()
        fillPath.addPath(linePath)
        val lastPt = sortedPoints.last()
        val firstPt = sortedPoints.first()
        fillPath.lineTo(xOf(lastPt.first), paddingTop + chartH)
        fillPath.lineTo(xOf(firstPt.first), paddingTop + chartH)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.30f), Color.Transparent),
                startY = paddingTop,
                endY = paddingTop + chartH
            )
        )

        // Line
        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Dots
        if (showDots) {
            sortedPoints.forEach { (ts, v) ->
                drawCircle(
                    color = lineColor,
                    radius = 5.dp.toPx(),
                    center = Offset(xOf(ts), yOf(v))
                )
                drawCircle(
                    color = Color(0xFF101310),
                    radius = 2.5f.dp.toPx(),
                    center = Offset(xOf(ts), yOf(v))
                )
            }
        }

        // Y-axis labels: min, mid, max
        val midY = (minY + maxY) / 2f
        listOf(maxY to paddingTop + 4f, midY to paddingTop + chartH / 2f, minY to paddingTop + chartH).forEach { (value, yPos) ->
            val label = valueFormatter(value)
            val measured = textMeasurer.measure(
                text = label,
                style = TextStyle(fontSize = 10.sp, color = labelColor, fontFamily = FontFamily.Monospace)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = label,
                topLeft = Offset(0f, yPos - measured.size.height / 2f),
                style = TextStyle(fontSize = 10.sp, color = labelColor, fontFamily = FontFamily.Monospace)
            )
        }

        // X-axis date labels
        val firstLabel = dateFormatter(sortedPoints.first().first)
        val lastLabel = dateFormatter(sortedPoints.last().first)

        val firstMeasured = textMeasurer.measure(
            text = firstLabel,
            style = TextStyle(fontSize = 10.sp, color = labelColor, fontFamily = FontFamily.Monospace)
        )
        val lastMeasured = textMeasurer.measure(
            text = lastLabel,
            style = TextStyle(fontSize = 10.sp, color = labelColor, fontFamily = FontFamily.Monospace)
        )

        val xLabelY = paddingTop + chartH + 6f

        drawText(
            textMeasurer = textMeasurer,
            text = firstLabel,
            topLeft = Offset(xOf(sortedPoints.first().first) - firstMeasured.size.width / 2f, xLabelY),
            style = TextStyle(fontSize = 10.sp, color = labelColor, fontFamily = FontFamily.Monospace)
        )
        drawText(
            textMeasurer = textMeasurer,
            text = lastLabel,
            topLeft = Offset(
                (xOf(sortedPoints.last().first) - lastMeasured.size.width / 2f).coerceAtMost(canvasW - lastMeasured.size.width),
                xLabelY
            ),
            style = TextStyle(fontSize = 10.sp, color = labelColor, fontFamily = FontFamily.Monospace)
        )
    }
}
