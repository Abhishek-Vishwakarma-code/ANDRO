package com.example.adcs.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ADCSDarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = PrimaryPurpleLight,
    secondary = BrandGold,
    onSecondary = TextPrimary,
    secondaryContainer = Color(0xFF3D2E00),
    onSecondaryContainer = BrandGoldLight,
    tertiary = AccentCyan,
    onTertiary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor,
    outlineVariant = DividerColor,
    error = ErrorRed,
    onError = TextOnPrimary,
    inverseSurface = TextPrimary,
    inverseOnSurface = BackgroundDark,
    scrim = Color(0x80000000)
)

@Composable
fun ADCSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ADCSDarkColorScheme,
        typography = Typography,
        content = content
    )
}
