package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val BharatLightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = PureWhite,
    primaryContainer = SaffronContainerLight,
    onPrimaryContainer = OnSaffronContainerLight,
    secondary = NavyDeep,
    onSecondary = PureWhite,
    secondaryContainer = NavyContainerLight,
    onSecondaryContainer = OnNavyContainerLight,
    tertiary = IndiaEmerald,
    onTertiary = PureWhite,
    tertiaryContainer = IndiaEmeraldSoft,
    onTertiaryContainer = ColorTokens.EmeraldDark,
    background = WarmIvory,
    onBackground = NavyDeep,
    surface = PureWhite,
    onSurface = NavyDeep,
    surfaceVariant = SoftSlateSurface,
    onSurfaceVariant = NavyMuted,
    outline = OutlineSubtle,
    outlineVariant = OutlineSubtle
)

private val BharatDarkColorScheme = darkColorScheme(
    primary = SaffronDarkPrimary,
    onPrimary = DarkNavyBackground,
    primaryContainer = SaffronDarkContainer,
    onPrimaryContainer = SaffronSoft,
    secondary = NavyContainerLight,
    onSecondary = NavyDeep,
    secondaryContainer = DarkNavySurfaceElevated,
    onSecondaryContainer = OnDarkText,
    tertiary = IndiaEmeraldSoft,
    onTertiary = ColorTokens.EmeraldDark,
    background = DarkNavyBackground,
    onBackground = OnDarkText,
    surface = DarkNavySurface,
    onSurface = OnDarkText,
    surfaceVariant = DarkNavySurfaceElevated,
    onSurfaceVariant = OnDarkSubtext,
    outline = NavyMuted,
    outlineVariant = DarkNavySurfaceElevated
)

private object ColorTokens {
    val EmeraldDark = androidx.compose.ui.graphics.Color(0xFF0A4D04)
}

val BharatShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun BharatAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) BharatDarkColorScheme else BharatLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BharatTypography,
        shapes = BharatShapes,
        content = content
    )
}
