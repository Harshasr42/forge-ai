package com.forge.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val ForgeShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

private val ForgeDarkColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Background,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = Accent,
    secondary = FireAmber,
    onSecondary = Background,
    secondaryContainer = SurfaceElevated,
    onSecondaryContainer = FireAmber,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    outlineVariant = TextMuted,
    error = DangerRed,
    onError = White
)

@Composable
fun ForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ForgeDarkColorScheme,
        typography = ForgeTypography,
        shapes = ForgeShapes,
        content = content
    )
}
