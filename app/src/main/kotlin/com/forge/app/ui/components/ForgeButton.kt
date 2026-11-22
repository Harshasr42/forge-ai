package com.forge.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forge.app.ui.theme.*

enum class ForgeButtonStyle {
    PRIMARY,   // High-contrast Lime
    SECONDARY, // Dark Surface with crisp border
    FIRE,      // Amber/Orange streak action
    DANGER     // Red
}

@Composable
fun ForgeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ForgeButtonStyle = ForgeButtonStyle.PRIMARY,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> SurfaceBorder
            isPressed -> when (style) {
                ForgeButtonStyle.PRIMARY -> AccentDim
                ForgeButtonStyle.SECONDARY -> SurfaceBorder
                ForgeButtonStyle.FIRE -> FireOrange
                ForgeButtonStyle.DANGER -> DangerRed.copy(alpha = 0.8f)
            }
            else -> when (style) {
                ForgeButtonStyle.PRIMARY -> Accent
                ForgeButtonStyle.SECONDARY -> SurfaceElevated
                ForgeButtonStyle.FIRE -> FireAmber
                ForgeButtonStyle.DANGER -> DangerRed
            }
        },
        label = "btn_bg"
    )

    val contentColor = when {
        !enabled -> TextMuted
        style == ForgeButtonStyle.PRIMARY -> Background
        style == ForgeButtonStyle.FIRE -> Background
        style == ForgeButtonStyle.SECONDARY -> TextPrimary
        else -> White
    }

    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (style == ForgeButtonStyle.SECONDARY) {
                    Modifier.border(1.dp, SurfaceBorder, shape)
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            leadingIcon?.invoke()
            Text(
                text = text,
                color = contentColor,
                style = ForgeTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}
